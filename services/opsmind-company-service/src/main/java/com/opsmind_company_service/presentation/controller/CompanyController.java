package com.opsmind_company_service.presentation.controller;

import com.opsmind_company_service.application.dto.request.CreateCompanyRequest;
import com.opsmind_company_service.application.dto.request.UpdateCompanyRequest;
import com.opsmind_company_service.application.dto.response.CompanyResponse;
import com.opsmind_company_service.application.usecase.CreateCompanyUseCase;
import com.opsmind_company_service.application.usecase.GetCompanyUseCase;
import com.opsmind_company_service.application.usecase.ListPlansUseCase;
import com.opsmind_company_service.application.usecase.UpdateCompanyUseCase;
import com.opsmind_company_service.presentation.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CreateCompanyUseCase createCompanyUseCase;
    private final GetCompanyUseCase getCompanyUseCase;
    private final UpdateCompanyUseCase updateCompanyUseCase;
    private final ListPlansUseCase listPlansUseCase;

    /**
     * POST /api/v1/companies
     * Creates a new company. The authenticated user becomes the OWNER.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CompanyResponse>> create(
            @Valid @RequestBody CreateCompanyRequest request) {
        CompanyResponse response = createCompanyUseCase.execute(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(response));
    }

    /**
     * GET /api/v1/companies/{id}
     * Returns company details. Caller must be a member of the company.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CompanyResponse>> get(@PathVariable Long id) {
        CompanyResponse response = getCompanyUseCase.execute(id);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    /**
     * PATCH /api/v1/companies/{id}
     * Updates company metadata. Requires OWNER or ADMIN role.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<CompanyResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCompanyRequest request) {
        CompanyResponse response = updateCompanyUseCase.execute(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Company updated", response));
    }

    /**
     * GET /api/v1/companies/plans
     * Returns all active subscription plans (public — no auth required for pricing page).
     */
    @GetMapping("/plans")
    public ResponseEntity<ApiResponse<?>> listPlans() {
        return ResponseEntity.ok(ApiResponse.ok(listPlansUseCase.execute()));
    }
}
