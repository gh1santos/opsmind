package com.opsmind_api_gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Propriedades customizadas do gateway lidas do application.yml.
 * Prefixo: gateway.*
 */
@Data
@Component
@ConfigurationProperties(prefix = "gateway")
public class GatewayProperties {

    /**
     * Lista de paths que não exigem JWT.
     * Suporta padrão /** ao final: ex. /actuator/**
     */
    private List<String> publicPaths = new ArrayList<>();

    /**
     * Verifica se um path é público (não requer autenticação).
     */
    public boolean isPublicPath(String path) {
        return publicPaths.stream().anyMatch(publicPath -> {
            if (publicPath.endsWith("/**")) {
                String prefix = publicPath.substring(0, publicPath.length() - 3);
                return path.startsWith(prefix);
            }
            return path.equals(publicPath);
        });
    }
}
