package com.opsmind_company_service.application.usecase;

import com.opsmind_company_service.application.dto.response.PlanResponse;

import java.util.List;

public interface ListPlansUseCase {
    List<PlanResponse> execute();
}
