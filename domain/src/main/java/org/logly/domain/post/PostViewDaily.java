package org.logly.domain.post;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostViewDaily {
    private PostId postId;
    private LocalDate date;
    private Long views;
}
