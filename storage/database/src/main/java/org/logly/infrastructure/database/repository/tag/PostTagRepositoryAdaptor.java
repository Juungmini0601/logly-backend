package org.logly.infrastructure.database.repository.tag;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

import org.logly.domain.post.PostId;
import org.logly.domain.tag.PostTag;
import org.logly.domain.tag.PostTagRepository;
import org.logly.domain.tag.TagId;
import org.logly.infrastructure.database.entity.tag.PostTagEntity;

@Repository
@RequiredArgsConstructor
public class PostTagRepositoryAdaptor implements PostTagRepository {

    private final PostTagJpaRepository entityRepository;

    @Override
    public PostTag save(PostTag postTag) {
        PostTagEntity entity = PostTagEntity.builder()
                .postId(postTag.getPostId().getId())
                .tagId(postTag.getTagId().getId())
                .build();

        return entityRepository.save(entity).toDomain();
    }
}
