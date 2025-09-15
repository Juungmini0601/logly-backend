package org.logly.domain.tag;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

import org.logly.domain.post.PostId;

@Getter
@Builder
public class PostTag {
    private PostId postId;
    private TagId tagId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PostTag of(PostId postId, TagId tagId) {
        return PostTag.builder()
                .postId(postId)
                .tagId(tagId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
