package com.opsmind_company_service.infrastructure.persistence.adapter;

import com.opsmind_company_service.domain.entity.SubscriptionPlan;
import com.opsmind_company_service.domain.enums.PlanType;
import com.opsmind_company_service.domain.repository.SubscriptionPlanRepository;
import com.opsmind_company_service.infrastructure.persistence.entity.SubscriptionPlanJpaEntity;
import com.opsmind_company_service.infrastructure.persistence.repository.JpaSubscriptionPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class SubscriptionPlanRepositoryAdapter implements SubscriptionPlanRepository {

    private final JpaSubscriptionPlanRepository repository;

    @Override
    public Optional<SubscriptionPlan> findById(Long id) {
        return repository.findById(id).map(this::toModel);
    }

    @Override
    public Optional<SubscriptionPlan> findByType(PlanType type) {
        return repository.findByType(type).map(this::toModel);
    }

    @Override
    public List<SubscriptionPlan> findAllActive() {
        return repository.findAllByActiveTrue()
                .stream().map(this::toModel).collect(Collectors.toList());
    }

    private SubscriptionPlan toModel(SubscriptionPlanJpaEntity e) {
        return SubscriptionPlan.builder()
                .id(e.getId()).type(e.getType()).name(e.getName())
                .maxUsers(e.getMaxUsers()).maxAiRequestsPerMonth(e.getMaxAiRequestsPerMonth())
                .maxStorageGb(e.getMaxStorageGb()).priceCents(e.getPriceCents())
                .active(e.getActive()).createdAt(e.getCreatedAt()).build();
    }
}
