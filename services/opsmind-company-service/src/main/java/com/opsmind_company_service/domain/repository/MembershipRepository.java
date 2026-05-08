package com.opsmind_company_service.domain.repository;

import com.opsmind_company_service.domain.entity.Membership;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MembershipRepository {

    Membership save(Membership membership);

    Optional<Membership> findByCompanyIdAndUserEmail(Long companyId, String userEmail);

    List<Membership> findAllByCompanyId(Long companyId);

    List<Membership> findAllByUserEmail(String userEmail);

    int countActiveByCompanyId(Long companyId);

    void deactivate(Long membershipId);
}
