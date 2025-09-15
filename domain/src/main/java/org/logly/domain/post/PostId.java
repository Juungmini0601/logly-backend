package org.logly.domain.post;

import java.util.Objects;

import lombok.Getter;

@Getter
public class PostId {
    private final Long id;

    private PostId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("게시글 ID는 양수여야 합니다.");
        }

        this.id = id;
    }

    public static PostId of(Long value) {
        return new PostId(value);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PostId postId = (PostId) o;
        return Objects.equals(id, postId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
