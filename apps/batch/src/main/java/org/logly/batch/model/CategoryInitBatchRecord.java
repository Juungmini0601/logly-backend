package org.logly.batch.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.logly.domain.category.Category;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryInitBatchRecord {
    private String name;

    public Category toCategory() {
        return Category.builder().name(name).build();
    }
}
