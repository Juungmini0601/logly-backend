package org.logly.domain.category;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Category {
    private CategoryId id;
    private String name;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
