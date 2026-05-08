package com.opsmind_company_service.application.service;

import com.opsmind_company_service.application.dto.response.PlanResponse;
import com.opsmind_company_service.application.usecase.ListPlansUseCase;
import com.opsmind_company_service.domain.repository.SubscriptionPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ListPlansService implements ListPlansUseCase {

    private final SubscriptionPlanRepository planRepository;

    @Override
    public List<PlanResponse> execute() {
        return planRepository.findAllActive()
                .stream()
                .map(p -> PlanResponse.builder()
                        .id(p.getId()).type(p.getType()).name(p.getName())
                        .maxUsers(p.getMaxUsers()).maxAiRequestsPerMonth(p.getMaxAiRequestsPerMonth())
                        .maxStorageGb(p.getMaxStorageGb()).priceCents(p.getPriceCents()).build())
                .collect(Collectors.toList());
    }
}
