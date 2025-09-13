package org.logly.infrastructure.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.logly.infrastructure.database.entity.CompanyEntity;

public interface CompanyJpaRepository extends JpaRepository<CompanyEntity, Long> {}
