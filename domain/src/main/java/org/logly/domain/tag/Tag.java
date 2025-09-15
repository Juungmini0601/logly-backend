package org.logly.domain.tag;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Tag {
    private TagId id;
    private String name;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
