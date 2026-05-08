package com.opsmind_company_service.application.dto.request;

import com.opsmind_company_service.domain.enums.CompanyIndustry;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateCompanyRequest {

    @NotBlank(message = "Company name is required")
    @Size(min = 2, max = 150, message = "Name must be between 2 and 150 characters")
    private String name;

    @Size(max = 20, message = "Document must have at most 20 characters")
    private String document;

    @NotNull(message = "Industry is required")
    private CompanyIndustry industry;
}
