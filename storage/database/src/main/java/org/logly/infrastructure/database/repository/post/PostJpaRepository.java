package org.logly.infrastructure.database.repository.post;

import org.springframework.data.jpa.repository.JpaRepository;

import org.logly.infrastructure.database.entity.post.PostEntity;

public interface PostJpaRepository extends JpaRepository<PostEntity, Long> {}
