package com.opsmind_auth_service.domain.entity;

import com.opsmind_auth_service.domain.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    private Long id;

    private UserRole name;

    private String description;
}
