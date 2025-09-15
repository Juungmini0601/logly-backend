package org.logly.infrastructure.database.repository.tag;

import org.springframework.data.jpa.repository.JpaRepository;

import org.logly.infrastructure.database.entity.tag.PostTagEntity;

public interface PostTagJpaRepository extends JpaRepository<PostTagEntity, PostTagEntity.PostTagId> {}
