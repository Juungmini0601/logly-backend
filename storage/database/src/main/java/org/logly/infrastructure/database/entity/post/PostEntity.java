package org.logly.infrastructure.database.entity.post;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.logly.infrastructure.database.entity.BaseEntity;

@Entity
@Table(name = "posts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "summary", columnDefinition = "TEXT")
    private String summary;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "thumbnail_url", length = 1000)
    private String thumbnailUrl;

    @Column(name = "original_url", nullable = false, unique = true, length = 1000)
    private String originalUrl;

    @Column(name = "published_at", nullable = false)
    private LocalDateTime publishedAt;

    @Column(name = "view_count")
    private Long viewCount = 0L;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "category_id")
    private Long categoryId;

    @Builder
    public PostEntity(
            String title,
            String summary,
            String content,
            String thumbnailUrl,
            String originalUrl,
            LocalDateTime publishedAt,
            Long viewCount,
            Long companyId,
            Long categoryId) {
        this.title = title;
        this.summary = summary;
        this.content = content;
        this.thumbnailUrl = thumbnailUrl;
        this.originalUrl = originalUrl;
        this.publishedAt = publishedAt;
        this.viewCount = viewCount != null ? viewCount : 0L;
        this.companyId = companyId;
        this.categoryId = categoryId;
    }
}
