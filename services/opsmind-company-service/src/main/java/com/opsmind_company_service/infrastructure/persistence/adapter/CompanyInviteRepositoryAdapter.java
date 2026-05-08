package com.opsmind_company_service.infrastructure.persistence.adapter;

import com.opsmind_company_service.domain.entity.CompanyInvite;
import com.opsmind_company_service.domain.enums.InviteStatus;
import com.opsmind_company_service.domain.repository.CompanyInviteRepository;
import com.opsmind_company_service.infrastructure.persistence.entity.CompanyInviteJpaEntity;
import com.opsmind_company_service.infrastructure.persistence.repository.JpaCompanyInviteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CompanyInviteRepositoryAdapter implements CompanyInviteRepository {

    private final JpaCompanyInviteRepository repository;

    @Override
    public CompanyInvite save(CompanyInvite invite) {
        return toModel(repository.save(toEntity(invite)));
    }

    @Override
    public Optional<CompanyInvite> findByToken(String token) {
        return repository.findByToken(token).map(this::toModel);
    }

    @Override
    public Optional<CompanyInvite> findByCompanyIdAndInvitedEmail(Long companyId, String email) {
        return repository.findByCompanyIdAndInvitedEmail(companyId, email).map(this::toModel);
    }

    @Override
    public List<CompanyInvite> findAllByCompanyIdAndStatus(Long companyId, InviteStatus status) {
        return repository.findAllByCompanyIdAndStatus(companyId, status)
                .stream().map(this::toModel).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateStatus(Long inviteId, InviteStatus status) {
        repository.updateStatus(inviteId, status);
    }

    private CompanyInvite toModel(CompanyInviteJpaEntity e) {
        return CompanyInvite.builder()
                .id(e.getId()).companyId(e.getCompanyId()).tenantId(e.getTenantId())
                .invitedEmail(e.getInvitedEmail()).invitedByEmail(e.getInvitedByEmail())
                .role(e.getRole()).token(e.getToken()).status(e.getStatus())
                .expiresAt(e.getExpiresAt()).acceptedAt(e.getAcceptedAt())
                .createdAt(e.getCreatedAt()).build();
    }

    private CompanyInviteJpaEntity toEntity(CompanyInvite i) {
        CompanyInviteJpaEntity e = new CompanyInviteJpaEntity();
        e.setId(i.getId()); e.setCompanyId(i.getCompanyId()); e.setTenantId(i.getTenantId());
        e.setInvitedEmail(i.getInvitedEmail()); e.setInvitedByEmail(i.getInvitedByEmail());
        e.setRole(i.getRole()); e.setToken(i.getToken()); e.setStatus(i.getStatus());
        e.setExpiresAt(i.getExpiresAt()); e.setAcceptedAt(i.getAcceptedAt());
        e.setCreatedAt(i.getCreatedAt());
        return e;
    }
}
