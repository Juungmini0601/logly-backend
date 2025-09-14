package org.logly.domain.post;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

import org.logly.domain.category.CategoryId;
import org.logly.domain.company.CompanyId;

@Getter
@Builder
public class Post {
    private PostId id;
    private String title;
    private String summary;
    private String content;
    private String thumbnailUrl;
    private String originalUrl;
    private LocalDateTime publishedAt;
    private Long viewCount;

    private CompanyId companyId;
    private CategoryId categoryId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
