package com.opsmind_company_service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuração de segurança do company-service.
 *
 * A autenticação real é feita pelo GatewaySecurityFilter que lê
 * os headers do API Gateway. O Spring Security apenas protege os
 * endpoints exigindo que a autenticação esteja presente no contexto.
 */
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final GatewaySecurityFilter gatewaySecurityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/api/v1/invites/*/accept").permitAll() // aceite de convite pode ser público
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        gatewaySecurityFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                .build();
    }
}
