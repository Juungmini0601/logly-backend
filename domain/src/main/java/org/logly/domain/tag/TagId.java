package org.logly.domain.tag;

import java.util.Objects;

import lombok.Getter;

@Getter
public class TagId {
    private final Long id;

    private TagId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("태그 ID는 양수여야 합니다.");
        }

        this.id = id;
    }

    public static TagId of(Long value) {
        return new TagId(value);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TagId tagId = (TagId) o;
        return Objects.equals(id, tagId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
