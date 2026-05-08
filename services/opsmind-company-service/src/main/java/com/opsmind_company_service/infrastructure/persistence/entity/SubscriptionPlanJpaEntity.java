package com.opsmind_company_service.infrastructure.persistence.entity;

import com.opsmind_company_service.domain.enums.PlanType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "subscription_plans")
@Getter
@Setter
public class SubscriptionPlanJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private PlanType type;

    @Column(nullable = false)
    private String name;

    @Column(name = "max_users", nullable = false)
    private Integer maxUsers;

    @Column(name = "max_ai_requests_per_month", nullable = false)
    private Integer maxAiRequestsPerMonth;

    @Column(name = "max_storage_gb", nullable = false)
    private Integer maxStorageGb;

    @Column(name = "price_cents", nullable = false)
    private Long priceCents;

    @Column(nullable = false)
    private Boolean active;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
