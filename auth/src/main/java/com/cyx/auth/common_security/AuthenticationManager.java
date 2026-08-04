package com.cyx.auth.common_security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ProviderManager;

import java.util.List;

@Configuration
public class AuthenticationManager {

    @Bean
    public org.springframework.security.authentication.AuthenticationManager securityAuthenticationManager(
            org.springframework.security.authentication.AuthenticationProvider authenticationProvider) {
        return new ProviderManager(List.of(authenticationProvider));
    }
}
