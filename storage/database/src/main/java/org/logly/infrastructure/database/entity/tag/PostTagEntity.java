package org.logly.infrastructure.database.entity.tag;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.logly.domain.post.PostId;
import org.logly.domain.tag.PostTag;
import org.logly.domain.tag.TagId;
import org.logly.infrastructure.database.entity.BaseEntity;

@Entity
@Table(name = "post_tags")
@IdClass(PostTagEntity.PostTagId.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostTagEntity extends BaseEntity {

    @Id
    @Column(name = "post_id")
    private Long postId;

    @Id
    @Column(name = "tag_id")
    private Long tagId;

    @Builder
    public PostTagEntity(Long postId, Long tagId) {
        this.postId = postId;
        this.tagId = tagId;
    }

    public PostTag toDomain() {
        return PostTag.builder()
                .postId(PostId.of(getPostId()))
                .tagId(TagId.of(getTagId()))
                .createdAt(getCreatedAt())
                .updatedAt(getUpdatedAt())
                .build();
    }

    public static class PostTagId implements Serializable {
        private Long postId;
        private Long tagId;

        public PostTagId() {}

        public PostTagId(Long postId, Long tagId) {
            this.postId = postId;
            this.tagId = tagId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PostTagId that = (PostTagId) o;
            return Objects.equals(postId, that.postId) && Objects.equals(tagId, that.tagId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(postId, tagId);
        }
    }
}
