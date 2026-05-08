package com.opsmind_auth_service.security.filter;

import com.opsmind_auth_service.security.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader =
                request.getHeader("Authorization");

        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        String jwt =
                authHeader.substring(7);

        if (!jwtService.isTokenValid(jwt)) {

            filterChain.doFilter(request, response);
            return;
        }

        String email =
                jwtService.extractUsername(jwt);

        String role =
                jwtService.extractRole(jwt);

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        List.of(
                                new SimpleGrantedAuthority(
                                        "ROLE_" + role
                                )
                        )
                );

        authToken.setDetails(
                new WebAuthenticationDetailsSource()
                        .buildDetails(request)
        );

        SecurityContextHolder
                .getContext()
                .setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
