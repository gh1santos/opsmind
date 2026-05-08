package com.opsmind_company_service.application.dto.response;

import com.opsmind_company_service.domain.enums.PlanType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlanResponse {

    private Long id;
    private PlanType type;
    private String name;
    private Integer maxUsers;
    private Integer maxAiRequestsPerMonth;
    private Integer maxStorageGb;
    private Long priceCents;
}
