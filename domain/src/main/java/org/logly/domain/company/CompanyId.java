package org.logly.domain.company;

import java.util.Objects;

import lombok.Getter;

@Getter
public class CompanyId {
    private final Long id;

    private CompanyId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("회사 ID는 양수여야 합니다.");
        }

        this.id = id;
    }

    public static CompanyId of(Long value) {
        return new CompanyId(value);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CompanyId companyId = (CompanyId) o;
        return Objects.equals(id, companyId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
