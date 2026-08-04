package com.cyx.system.security;

import com.cyx.result.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SystemSecurityConfig {
    @Bean
    SecurityFilterChain systemSecurityFilterChain(HttpSecurity http, SystemJwtAuthenticationFilter filter,
                                                   ObjectMapper objectMapper) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(a -> a.anyRequest().authenticated())
                .exceptionHandling(e -> e
                        .authenticationEntryPoint((req, res, ex) -> write(res, objectMapper, 401, "请先登录"))
                        .accessDeniedHandler((req, res, ex) -> write(res, objectMapper, 403, "无访问权限")))
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class).build();
    }

    private void write(jakarta.servlet.http.HttpServletResponse response, ObjectMapper objectMapper,
                       int code, String message) throws java.io.IOException {
        response.setStatus(code);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), Result.failure(code, message));
    }
}
