package org.logly.infrastructure.database.repository.tag;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

import org.logly.domain.tag.Tag;
import org.logly.domain.tag.TagId;
import org.logly.domain.tag.TagRepository;
import org.logly.infrastructure.database.entity.tag.TagEntity;

@Repository
@RequiredArgsConstructor
public class TagRepositoryAdaptor implements TagRepository {

    private final TagJpaRepository entityRepository;

    @Override
    public Tag save(Tag tag) {
        TagEntity entity = TagEntity.builder().name(tag.getName()).build();

        TagEntity savedEntity = entityRepository.save(entity);

        return Tag.builder()
                .id(TagId.of(savedEntity.getId()))
                .name(savedEntity.getName())
                .createdAt(savedEntity.getCreatedAt())
                .updatedAt(savedEntity.getUpdatedAt())
                .build();
    }

    @Override
    public Optional<Tag> findByName(String name) {
        return entityRepository.findByName(name).map(TagEntity::toDomain);
    }
}
