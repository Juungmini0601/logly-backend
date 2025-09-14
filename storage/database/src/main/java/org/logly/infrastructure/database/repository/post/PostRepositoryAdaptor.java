package org.logly.infrastructure.database.repository.post;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

import org.logly.domain.category.CategoryId;
import org.logly.domain.company.CompanyId;
import org.logly.domain.post.Post;
import org.logly.domain.post.PostId;
import org.logly.domain.post.PostRepository;
import org.logly.infrastructure.database.entity.post.PostEntity;

@Repository
@RequiredArgsConstructor
public class PostRepositoryAdaptor implements PostRepository {

    private final PostJpaRepository entityRepository;

    @Override
    public Post save(Post post) {
        PostEntity entity = PostEntity.builder()
                .title(post.getTitle())
                .summary(post.getSummary())
                .content(post.getContent())
                .thumbnailUrl(post.getThumbnailUrl())
                .originalUrl(post.getOriginalUrl())
                .publishedAt(post.getPublishedAt())
                .viewCount(post.getViewCount())
                .companyId(post.getCompanyId().getId())
                .categoryId(post.getCategoryId() != null ? post.getCategoryId().getId() : null)
                .build();

        PostEntity savedEntity = entityRepository.save(entity);

        return Post.builder()
                .id(PostId.of(savedEntity.getId()))
                .title(savedEntity.getTitle())
                .summary(savedEntity.getSummary())
                .content(savedEntity.getContent())
                .thumbnailUrl(savedEntity.getThumbnailUrl())
                .originalUrl(savedEntity.getOriginalUrl())
                .publishedAt(savedEntity.getPublishedAt())
                .viewCount(savedEntity.getViewCount())
                .companyId(CompanyId.of(savedEntity.getCompanyId()))
                .categoryId(savedEntity.getCategoryId() != null ? CategoryId.of(savedEntity.getCategoryId()) : null)
                .createdAt(savedEntity.getCreatedAt())
                .updatedAt(savedEntity.getUpdatedAt())
                .build();
    }
}
