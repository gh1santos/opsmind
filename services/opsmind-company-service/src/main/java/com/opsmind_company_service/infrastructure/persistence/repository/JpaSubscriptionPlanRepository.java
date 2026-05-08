package com.opsmind_company_service.infrastructure.persistence.repository;

import com.opsmind_company_service.domain.enums.PlanType;
import com.opsmind_company_service.infrastructure.persistence.entity.SubscriptionPlanJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaSubscriptionPlanRepository
        extends JpaRepository<SubscriptionPlanJpaEntity, Long> {

    Optional<SubscriptionPlanJpaEntity> findByType(PlanType type);

    List<SubscriptionPlanJpaEntity> findAllByActiveTrue();
}
