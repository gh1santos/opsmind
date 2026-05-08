package com.opsmind_company_service.application.dto.response;

import com.opsmind_company_service.domain.enums.CompanyIndustry;
import com.opsmind_company_service.domain.enums.CompanyStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class CompanyResponse {

    private Long id;
    private UUID tenantId;
    private String name;
    private String slug;
    private String document;
    private CompanyIndustry industry;
    private CompanyStatus status;
    private String ownerEmail;
    private LocalDateTime trialEndsAt;
    private LocalDateTime createdAt;
}
