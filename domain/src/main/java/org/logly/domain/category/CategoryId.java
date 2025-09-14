package org.logly.domain.category;

import java.util.Objects;

import lombok.Getter;

@Getter
public class CategoryId {
    private final Long id;

    private CategoryId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("카테고리 ID는 양수여야 합니다.");
        }

        this.id = id;
    }

    public static CategoryId of(Long value) {
        return new CategoryId(value);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CategoryId categoryId = (CategoryId) o;
        return Objects.equals(id, categoryId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
