package org.logly.batch.model;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;

import org.logly.domain.company.CompanyId;
import org.logly.domain.post.Post;

@Data
@Builder
public class CrawledPostItem {
    private Long id;
    private String title;
    private String summary;
    private String content;
    private String originalUrl;
    private String thumbnailUrl;
    private LocalDateTime publishedAt;

    private List<String> tagNames;
    private Long companyId;

    @Override
    public String toString() {
        return String.format("[title: %s, originalUrl: %s, publishedAt: %s]", title, originalUrl, publishedAt);
    }

    public Post toDomain() {
        return Post.builder()
                .title(title)
                .summary(summary)
                .content(content)
                .thumbnailUrl(thumbnailUrl)
                .originalUrl(originalUrl)
                .publishedAt(publishedAt)
                .companyId(CompanyId.of(companyId))
                .build();
    }
}
