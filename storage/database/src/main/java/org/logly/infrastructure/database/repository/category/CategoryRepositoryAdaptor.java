package org.logly.infrastructure.database.repository.category;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

import org.logly.domain.category.Category;
import org.logly.domain.category.CategoryId;
import org.logly.domain.category.CategoryRepository;
import org.logly.infrastructure.database.entity.category.CategoryEntity;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryAdaptor implements CategoryRepository {

    private final CategoryJpaRepository entityRepository;

    @Override
    public Category save(Category category) {
        CategoryEntity entity = CategoryEntity.builder().name(category.getName()).build();
        return entityRepository.save(entity).toDomain();
    }
}
