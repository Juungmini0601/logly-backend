package org.logly.infrastructure.database.repository.compnay;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.logly.infrastructure.database.entity.compnay.CompanyEntity;

public interface CompanyJpaRepository extends JpaRepository<CompanyEntity, Long> {
    Optional<CompanyEntity> findByName(String name);
}
