package com.opsmind_company_service.application.dto.request;

import com.opsmind_company_service.domain.enums.CompanyIndustry;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateCompanyRequest {

    @Size(min = 2, max = 150, message = "Name must be between 2 and 150 characters")
    private String name;

    @Size(max = 20)
    private String document;

    private CompanyIndustry industry;
}
