package org.logly.domain.company;

import java.util.Optional;

public interface CompanyRepository {
    Company save(Company company);

    Optional<Company> findByName(String name);
}
