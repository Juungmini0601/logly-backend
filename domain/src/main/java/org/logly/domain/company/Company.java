package org.logly.domain.company;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Company {
    private CompanyId id;
    private String name;
    private String iconUrl;
    private String blogUrl;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
