package org.logly.infrastructure.database.repository;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

import org.logly.domain.company.Company;
import org.logly.domain.company.CompanyId;
import org.logly.domain.company.CompanyRepository;
import org.logly.infrastructure.database.entity.CompanyEntity;

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

        CompanyEntity savedEntity = entityRepository.save(entity);

        return Company.builder()
                .id(CompanyId.of(savedEntity.getId()))
                .name(savedEntity.getName())
                .iconUrl(savedEntity.getIconUrl())
                .blogUrl(savedEntity.getBlogUrl())
                .build();
    }
}
