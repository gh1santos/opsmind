package com.opsmind_company_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.opsmind_company_service.infrastructure.repository")
@EnableConfigurationProperties
public class OpsmindCompanyServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpsmindCompanyServiceApplication.class, args);
    }
}
