package com.opsmind_company_service.infrastructure.persistence.adapter;

import com.opsmind_company_service.domain.entity.Membership;
import com.opsmind_company_service.domain.repository.MembershipRepository;
import com.opsmind_company_service.infrastructure.persistence.entity.MembershipJpaEntity;
import com.opsmind_company_service.infrastructure.persistence.repository.JpaMembershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class MembershipRepositoryAdapter implements MembershipRepository {

    private final JpaMembershipRepository repository;

    @Override
    public Membership save(Membership membership) {
        return toModel(repository.save(toEntity(membership)));
    }

    @Override
    public Optional<Membership> findByCompanyIdAndUserEmail(Long companyId, String userEmail) {
        return repository.findByCompanyIdAndUserEmail(companyId, userEmail).map(this::toModel);
    }

    @Override
    public List<Membership> findAllByCompanyId(Long companyId) {
        return repository.findAllByCompanyIdAndActiveTrue(companyId)
                .stream().map(this::toModel).collect(Collectors.toList());
    }

    @Override
    public List<Membership> findAllByUserEmail(String userEmail) {
        return repository.findAllByUserEmailAndActiveTrue(userEmail)
                .stream().map(this::toModel).collect(Collectors.toList());
    }

    @Override
    public int countActiveByCompanyId(Long companyId) {
        return repository.countActiveByCompanyId(companyId);
    }

    @Override
    @Transactional
    public void deactivate(Long membershipId) {
        repository.deactivateById(membershipId);
    }

    private Membership toModel(MembershipJpaEntity e) {
        return Membership.builder()
                .id(e.getId()).companyId(e.getCompanyId()).tenantId(e.getTenantId())
                .userEmail(e.getUserEmail()).role(e.getRole()).active(e.getActive())
                .joinedAt(e.getJoinedAt()).updatedAt(e.getUpdatedAt()).build();
    }

    private MembershipJpaEntity toEntity(Membership m) {
        MembershipJpaEntity e = new MembershipJpaEntity();
        e.setId(m.getId()); e.setCompanyId(m.getCompanyId()); e.setTenantId(m.getTenantId());
        e.setUserEmail(m.getUserEmail()); e.setRole(m.getRole()); e.setActive(m.getActive());
        e.setJoinedAt(m.getJoinedAt()); e.setUpdatedAt(m.getUpdatedAt());
        return e;
    }
}
