package org.logly.batch.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.logly.batch.exception.CrawlingException;
import org.logly.batch.model.CrawledPostItem;
import org.logly.batch.validation.BlogPageJobParameterValidator;
import org.logly.domain.company.Company;
import org.logly.domain.company.CompanyRepository;
import org.logly.domain.post.Post;
import org.logly.domain.post.PostRepository;
import org.logly.domain.tag.PostTag;
import org.logly.domain.tag.PostTagRepository;
import org.logly.domain.tag.Tag;
import org.logly.domain.tag.TagRepository;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.LoadState;

// 11 ~

@Slf4j
@Configuration
@RequiredArgsConstructor
public class NaverD2PostInitBatchConfig {

    private final CompanyRepository companyRepository;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;

    private static final String NAVER_D2_BASE_URL = "https://d2.naver.com";
    private static final String NAVER_D2_BLOG_URL = "https://d2.naver.com/home";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
    private static final int CHUNK_SIZE = 1; // 페이지별 처리를 위해 1로 설정

    private static final String COMPNAY_NAME = "네이버 D2";

    @Bean
    public Job naverD2PostInitJob(
            JobRepository jobRepository,
            Step naverD2CrawlStep,
            BlogPageJobParameterValidator blogPageJobParameterValidator) {
        return new JobBuilder("naverD2PostInitJob", jobRepository)
                .validator(blogPageJobParameterValidator)
                .start(naverD2CrawlStep)
                .build();
    }

    @Bean
    public Step naverD2CrawlStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            ItemReader<Integer> naverD2ItemReader,
            ItemProcessor<Integer, List<CrawledPostItem>> naverD2ItemProcessor,
            ItemWriter<List<CrawledPostItem>> naverD2ItemWriter) {
        return new StepBuilder("naverD2CrawlStep", jobRepository)
                .<Integer, List<CrawledPostItem>>chunk(CHUNK_SIZE, transactionManager)
                .reader(naverD2ItemReader)
                .processor(naverD2ItemProcessor)
                .writer(naverD2ItemWriter)
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<Integer> naverD2ItemReader(
            @Value("#{jobParameters['startPage']}") Integer startPage,
            @Value("#{jobParameters['endPage']}") Integer endPage) {
        List<Integer> pageNumbers = new ArrayList<>();

        for (int i = startPage; i <= endPage; i++) {
            pageNumbers.add(i);
        }

        log.info("Naver D2 Blog Crawling for page numbers [{}, {}]", startPage, endPage);

        return new ListItemReader<>(pageNumbers);
    }

    @Bean
    public ItemProcessor<Integer, List<CrawledPostItem>> naverD2ItemProcessor() {
        return pageNumber -> {
            log.info("페이지 {} 처리 시작", pageNumber);

            List<CrawledPostItem> pagePostItems = new ArrayList<>();

            try (Playwright playwright = Playwright.create();
                    Browser browser = playwright.chromium().launch()) {

                Page page = browser.newPage();

                // 해당 페이지의 게시글 목록 크롤링
                String url = String.format("%s/?page=%d", NAVER_D2_BLOG_URL, pageNumber);
                page.navigate(url);
                page.waitForLoadState(LoadState.NETWORKIDLE);

                List<ElementHandle> articles = page.querySelectorAll(".post_article");

                // 먼저 모든 게시글의 기본 정보만 수집
                for (ElementHandle article : articles) {
                    try {
                        String title =
                                article.querySelector("h2 a").textContent().trim();
                        String link = NAVER_D2_BASE_URL
                                + article.querySelector("h2 a").getAttribute("href");
                        String summary =
                                article.querySelector(".post_txt").textContent().trim();
                        String imageUrl = NAVER_D2_BASE_URL
                                + article.querySelector(".cont_img img").getAttribute("src");
                        String publishDateString = article.querySelectorAll("dl dd")
                                .getFirst()
                                .textContent()
                                .trim();
                        LocalDateTime publishDate =
                                LocalDate.parse(publishDateString, formatter).atStartOfDay();

                        CrawledPostItem postItem = CrawledPostItem.builder()
                                .title(title)
                                .summary(summary)
                                .originalUrl(link)
                                .thumbnailUrl(imageUrl)
                                .publishedAt(publishDate)
                                .build();

                        pagePostItems.add(postItem);

                    } catch (Exception e) {
                        log.error("Naver D2 게시글 리스트 크롤링 실패: {} page", pageNumber, e);
                        throw new CrawlingException("Naver D2 페이지 리스트 크롤링 실패: {} " + pageNumber + " 페이지");
                    }
                }

                log.info("Naver D2 페이지 {} 게시글 리스트 수집 완료: {}개", pageNumber, pagePostItems.size());

                // 각 게시글의 상세 내용 크롤링
                for (int i = 0; i < pagePostItems.size(); i++) {
                    CrawledPostItem postItem = pagePostItems.get(i);
                    try {
                        page.navigate(postItem.getOriginalUrl());
                        // 네이버 D2 페이지에 PDF View 같은 데이터가 있는 페이지는 타임아웃으로 빼고 넘긴다.
                        page.waitForLoadState(
                                LoadState.NETWORKIDLE, new Page.WaitForLoadStateOptions().setTimeout(3000));

                        String content = page.querySelector(".con_view").innerHTML();
                        postItem.setContent(content);
                        // 태그 크롤링
                        List<String> tagNames = new ArrayList<>();
                        List<ElementHandle> tagElements = page.querySelectorAll(".tag_list a span");

                        for (ElementHandle tagElement : tagElements) {
                            String tagName = tagElement.textContent().trim();

                            if (!tagName.isEmpty()) {
                                tagNames.add(tagName);
                            }
                        }
                        postItem.setTagNames(tagNames);

                        log.info(
                                "title: {} 크롤링 완료 ({}/{}, 페이지 {})",
                                postItem.getTitle(),
                                i + 1,
                                pagePostItems.size(),
                                pageNumber);
                    } catch (Exception e) {
                        log.error("상세 크롤링 실패: {} (페이지 {})", postItem.getOriginalUrl(), pageNumber, e);
                    }
                }
            }

            log.info("페이지 {} 처리 완료: {}개 게시글", pageNumber, pagePostItems.size());
            return pagePostItems.stream()
                    .filter(item ->
                            item.getContent() != null && !item.getContent().isEmpty())
                    .toList();
        };
    }

    @Bean
    public ItemWriter<List<CrawledPostItem>> naverD2ItemWriter() {
        return chunk -> {
            Company naverD2 = companyRepository
                    .findByName(COMPNAY_NAME)
                    .orElseThrow(() -> new RuntimeException(COMPNAY_NAME + " company not found"));

            Long companyId = naverD2.getId().getId();
            log.info("네이버 D2 회사 ID 조회 완료: {}", companyId);

            for (List<CrawledPostItem> postList : chunk) {
                for (CrawledPostItem item : postList) {
                    // Company ID 설정
                    item.setCompanyId(companyId);

                    // 포스트 저장
                    Post savedPost = postRepository.save(item.toDomain());
                    log.info(
                            "포스트 저장 완료: {} (ID: {})",
                            savedPost.getTitle(),
                            savedPost.getId().getId());

                    // 태그 처리 및 PostTag 연관관계 생성
                    List<String> tagNames = item.getTagNames();
                    if (tagNames != null && !tagNames.isEmpty()) {
                        for (String tagName : tagNames) {
                            // 태그가 이미 존재하는지 확인
                            Tag tag = tagRepository.findByName(tagName).orElseGet(() -> {
                                // 태그가 없으면 새로 생성
                                Tag newTag = Tag.builder().name(tagName).build();
                                return tagRepository.save(newTag);
                            });

                            // PostTag 연관관계 생성
                            PostTag postTag = PostTag.of(savedPost.getId(), tag.getId());
                            postTagRepository.save(postTag);
                        }
                    }
                }
            }
        };
    }
}
