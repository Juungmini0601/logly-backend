package org.logly.infrastructure.database.entity.compnay;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.logly.domain.company.Company;
import org.logly.domain.company.CompanyId;
import org.logly.infrastructure.database.entity.BaseEntity;

@Entity
@Table(name = "companies")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompanyEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 200)
    private String name;

    @Column(name = "icon_url", length = 1000)
    private String iconUrl;

    @Column(name = "blog_url", length = 1000)
    private String blogUrl;

    @Builder
    public CompanyEntity(String name, String iconUrl, String blogUrl) {
        this.name = name;
        this.iconUrl = iconUrl;
        this.blogUrl = blogUrl;
    }

    public Company toDomain() {
        return Company.builder()
                .id(CompanyId.of(getId()))
                .name(getName())
                .iconUrl(getIconUrl())
                .blogUrl(getBlogUrl())
                .createdAt(getCreatedAt())
                .updatedAt(getUpdatedAt())
                .build();
    }
}
