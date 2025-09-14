package org.logly.infrastructure.database.repository.tag;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.logly.infrastructure.database.entity.tag.TagEntity;

public interface TagJpaRepository extends JpaRepository<TagEntity, Long> {
    Optional<TagEntity> findByName(String name);
}
