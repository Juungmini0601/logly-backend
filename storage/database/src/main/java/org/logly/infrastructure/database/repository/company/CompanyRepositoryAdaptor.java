package org.logly.infrastructure.database.repository.company;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

import org.logly.domain.company.Company;
import org.logly.domain.company.CompanyRepository;
import org.logly.infrastructure.database.entity.compnay.CompanyEntity;

@Repository
@RequiredArgsConstructor
public class CompanyRepositoryAdaptor implements CompanyRepository {

    private final CompanyJpaRepository entityRepository;

    @Override
    public Company save(Company company) {
        CompanyEntity entity = CompanyEntity.builder()
                .name(company.getName())
                .iconUrl(company.getIconUrl())
                .blogUrl(company.getBlogUrl())
                .build();

        return entityRepository.save(entity).toDomain();
    }

    @Override
    public Optional<Company> findByName(String name) {
        return entityRepository.findByName(name).map(CompanyEntity::toDomain);
    }
}
