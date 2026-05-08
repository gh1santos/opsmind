package com.opsmind_company_service.application.service;

import com.opsmind_company_service.application.dto.response.MembershipResponse;
import com.opsmind_company_service.application.exception.BusinessException;
import com.opsmind_company_service.application.usecase.GetMembersUseCase;
import com.opsmind_company_service.domain.repository.CompanyRepository;
import com.opsmind_company_service.domain.repository.MembershipRepository;
import com.opsmind_company_service.security.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetMembersService implements GetMembersUseCase {

    private final CompanyRepository companyRepository;
    private final MembershipRepository membershipRepository;

    @Override
    public List<MembershipResponse> execute(Long companyId) {

        companyRepository.findById(companyId)
                .orElseThrow(() -> new BusinessException("Company not found"));

        // Valida que o usuário é membro da empresa
        String userEmail = UserContext.getEmail();
        membershipRepository.findByCompanyIdAndUserEmail(companyId, userEmail)
                .orElseThrow(() -> new BusinessException("Access denied"));

        return membershipRepository.findAllByCompanyId(companyId)
                .stream()
                .map(m -> MembershipResponse.builder()
                        .id(m.getId())
                        .userEmail(m.getUserEmail())
                        .role(m.getRole())
                        .joinedAt(m.getJoinedAt())
                        .build())
                .collect(Collectors.toList());
    }
}
