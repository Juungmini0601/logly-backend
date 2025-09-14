package org.logly.batch.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.logly.domain.company.Company;

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
