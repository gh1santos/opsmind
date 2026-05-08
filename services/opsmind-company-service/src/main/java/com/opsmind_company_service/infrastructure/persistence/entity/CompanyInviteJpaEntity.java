package com.opsmind_company_service.infrastructure.persistence.entity;

import com.opsmind_company_service.domain.enums.InviteStatus;
import com.opsmind_company_service.domain.enums.MemberRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "company_invites")
@Getter
@Setter
public class CompanyInviteJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "invited_email", nullable = false)
    private String invitedEmail;

    @Column(name = "invited_by_email", nullable = false)
    private String invitedByEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole role;

    @Column(nullable = false, unique = true, length = 500)
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InviteStatus status;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "accepted_at")
    private LocalDateTime acceptedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
