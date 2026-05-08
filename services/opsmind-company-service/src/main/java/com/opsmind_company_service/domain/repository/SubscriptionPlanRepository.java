package com.opsmind_company_service.domain.repository;

import com.opsmind_company_service.domain.entity.SubscriptionPlan;
import com.opsmind_company_service.domain.enums.PlanType;

import java.util.List;
import java.util.Optional;

public interface SubscriptionPlanRepository {

    Optional<SubscriptionPlan> findById(Long id);

    Optional<SubscriptionPlan> findByType(PlanType type);

    List<SubscriptionPlan> findAllActive();
}
