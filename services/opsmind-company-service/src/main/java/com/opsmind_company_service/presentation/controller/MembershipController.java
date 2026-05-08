package com.opsmind_company_service.presentation.controller;

import com.opsmind_company_service.application.dto.response.MembershipResponse;
import com.opsmind_company_service.application.usecase.GetMembersUseCase;
import com.opsmind_company_service.application.usecase.RemoveMemberUseCase;
import com.opsmind_company_service.presentation.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/companies/{companyId}/members")
@RequiredArgsConstructor
public class MembershipController {

    private final GetMembersUseCase getMembersUseCase;
    private final RemoveMemberUseCase removeMemberUseCase;

    /**
     * GET /api/v1/companies/{companyId}/members
     * Lists all active members of the company.
     * Caller must be a member (any role).
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<MembershipResponse>>> list(
            @PathVariable Long companyId) {
        List<MembershipResponse> members = getMembersUseCase.execute(companyId);
        return ResponseEntity.ok(ApiResponse.ok(members));
    }

    /**
     * DELETE /api/v1/companies/{companyId}/members/{memberEmail}
     * Removes a member from the company (soft-delete / deactivate).
     * Requires OWNER or ADMIN role. OWNER cannot be removed.
     * Only OWNER can remove another ADMIN.
     */
    @DeleteMapping("/{memberEmail}")
    public ResponseEntity<ApiResponse<Void>> remove(
            @PathVariable Long companyId,
            @PathVariable String memberEmail) {
        removeMemberUseCase.execute(companyId, memberEmail);
        return ResponseEntity.ok(ApiResponse.noContent());
    }
}
