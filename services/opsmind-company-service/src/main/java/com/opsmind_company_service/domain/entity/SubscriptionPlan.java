package com.opsmind_company_service.domain.entity;

import com.opsmind_company_service.domain.enums.PlanType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionPlan {

    private Long id;

    private PlanType type;

    private String name;

    /** -1 = ilimitado */
    private Integer maxUsers;

    /** -1 = ilimitado */
    private Integer maxAiRequestsPerMonth;

    /** -1 = ilimitado. Em GB. */
    private Integer maxStorageGb;

    /** Em centavos (BRL). 0 = gratuito. */
    private Long priceCents;

    private Boolean active;

    private LocalDateTime createdAt;
}
