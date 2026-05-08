package com.opsmind_api_gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Data
@Component
@ConfigurationProperties(prefix = "gateway")
public class GatewayProperties {

    private List<String> publicPaths = new ArrayList<>();

    /**
     * Checks if a request path matches any configured public path pattern.
     * <p>
     * Supported patterns:
     * <ul>
     *   <li>{@code /api/v1/auth/login} — exact match</li>
     *   <li>{@code /actuator/**} — prefix match (everything under /actuator/)</li>
     *   <li>{@code /api/v1/invites/*/accept} — single segment wildcard</li>
     * </ul>
     */
    public boolean isPublicPath(String path) {
        return publicPaths.stream().anyMatch(pattern -> matchesPattern(pattern, path));
    }

    private boolean matchesPattern(String pattern, String path) {
        // Prefix wildcard: /foo/bar/**
        if (pattern.endsWith("/**")) {
            String prefix = pattern.substring(0, pattern.length() - 3);
            return path.startsWith(prefix);
        }
        // Single-segment wildcard: /foo/*/bar
        if (pattern.contains("/*")) {
            String regex = pattern.replace("/", "\\/")
                                  .replace("*", "[^/]+");
            return Pattern.matches(regex, path);
        }
        // Exact match
        return path.equals(pattern);
    }
}
