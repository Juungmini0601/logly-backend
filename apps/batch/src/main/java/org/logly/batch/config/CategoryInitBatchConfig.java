package org.logly.batch.config;

import java.util.Arrays;
import java.util.Iterator;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;

import org.logly.batch.model.CategoryInitBatchRecord;
import org.logly.domain.category.Category;
import org.logly.domain.category.CategoryRepository;

@Configuration
@RequiredArgsConstructor
public class CategoryInitBatchConfig {
    private final CategoryRepository categoryRepository;

    @Bean
    public Job categoryInitJob(JobRepository jobRepository, Step categoryInitStep) {
        return new JobBuilder("InitCategoryJob", jobRepository)
                .start(categoryInitStep)
                .build();
    }

    @Bean
    public Step categoryInitStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("categoryInitStep", jobRepository)
                .<CategoryInitBatchRecord, Category>chunk(5, transactionManager)
                .reader(categoryItemReader())
                .processor(categoryItemProcessor())
                .writer(categoryItemWriter())
                .build();
    }

    @Bean
    public ItemReader<CategoryInitBatchRecord> categoryItemReader() {
        return new ItemReader<>() {
            private final Iterator<String> categoryIterator =
                    Arrays.asList("Frontend", "Backend", "AI", "DevOps", "Etc").iterator();

            @Override
            public CategoryInitBatchRecord read() {
                if (categoryIterator.hasNext()) {
                    return new CategoryInitBatchRecord(categoryIterator.next());
                }
                return null;
            }
        };
    }

    @Bean
    public ItemProcessor<CategoryInitBatchRecord, Category> categoryItemProcessor() {
        return CategoryInitBatchRecord::toCategory;
    }

    @Bean
    public ItemWriter<Category> categoryItemWriter() {
        return items -> items.forEach(categoryRepository::save);
    }
}
