package com.opsmind_company_service.presentation.controller;

import com.opsmind_company_service.application.dto.request.InviteUserRequest;
import com.opsmind_company_service.application.dto.response.InviteResponse;
import com.opsmind_company_service.application.dto.response.MembershipResponse;
import com.opsmind_company_service.application.usecase.AcceptInviteUseCase;
import com.opsmind_company_service.application.usecase.InviteUserUseCase;
import com.opsmind_company_service.application.usecase.RevokeInviteUseCase;
import com.opsmind_company_service.presentation.response.ApiResponse;
import com.opsmind_company_service.security.UserContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class InviteController {

    private final InviteUserUseCase inviteUserUseCase;
    private final AcceptInviteUseCase acceptInviteUseCase;
    private final RevokeInviteUseCase revokeInviteUseCase;

    /**
     * POST /api/v1/companies/{companyId}/invites
     * Sends an invite to an external email.
     * Requires OWNER or ADMIN role.
     * Validates plan user limits before issuing.
     */
    @PostMapping("/api/v1/companies/{companyId}/invites")
    public ResponseEntity<ApiResponse<InviteResponse>> invite(
            @PathVariable Long companyId,
            @Valid @RequestBody InviteUserRequest request) {
        InviteResponse response = inviteUserUseCase.execute(companyId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(response));
    }

    /**
     * POST /api/v1/invites/{token}/accept
     * Accepts an invite by token.
     * Public endpoint — user is identified by the X-User-Email header propagated
     * by the gateway (the invitee must be authenticated when accepting).
     * Email must match the invited email in the invite record.
     */
    @PostMapping("/api/v1/invites/{token}/accept")
    public ResponseEntity<ApiResponse<MembershipResponse>> accept(
            @PathVariable String token) {
        String acceptingUserEmail = UserContext.getEmail();
        MembershipResponse membership = acceptInviteUseCase.execute(token, acceptingUserEmail);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Invite accepted", membership));
    }

    /**
     * DELETE /api/v1/companies/{companyId}/invites/{inviteId}
     * Revokes a PENDING invite.
     * Requires OWNER or ADMIN role.
     */
    @DeleteMapping("/api/v1/companies/{companyId}/invites/{inviteId}")
    public ResponseEntity<ApiResponse<Void>> revoke(
            @PathVariable Long companyId,
            @PathVariable Long inviteId) {
        revokeInviteUseCase.execute(companyId, inviteId);
        return ResponseEntity.ok(ApiResponse.noContent());
    }
}
