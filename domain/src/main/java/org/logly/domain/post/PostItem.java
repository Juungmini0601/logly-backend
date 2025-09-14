package org.logly.domain.post;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

import org.logly.domain.company.Company;

@Getter
@Builder
public class PostItem {
    private Long id;
    private String title;
    private String summary;
    private String content;
    private String originalUrl;
    private String thumbnailUrl;
    private LocalDateTime publishedAt;

    private Company company;

    public void updateContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return String.format("[title: %s, originalUrl: %s, publishedAt: %s]", title, originalUrl, publishedAt);
    }
}
