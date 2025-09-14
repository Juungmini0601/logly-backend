package org.logly.infrastructure.database.repository.category;

import org.springframework.data.jpa.repository.JpaRepository;

import org.logly.infrastructure.database.entity.category.CategoryEntity;

public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, Long> {}
