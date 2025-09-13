package org.logly.domain.company;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CompanyInitBatchRecord {
    private String name;
    private String blogUrl;

    public Company toCompany() {
        return Company.builder().name(name).blogUrl(blogUrl).build();
    }
}
