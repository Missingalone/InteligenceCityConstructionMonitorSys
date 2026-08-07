package com.cyx.adminterminal.security;

import com.cyx.result.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 管理员端无状态 JWT 安全配置。
 */
@Configuration
public class AdminSecurityConfig {
    /**
     * 配置管理员端认证、统一异常返回和 JWT 过滤器。
     */
    @Bean
    SecurityFilterChain adminSecurityFilterChain(HttpSecurity h, AdminJwtAuthenticationFilter f, ObjectMapper o) throws Exception {
        return h.csrf(AbstractHttpConfigurer::disable).sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authorizeHttpRequests(a -> a.anyRequest().authenticated()).exceptionHandling(e -> e.authenticationEntryPoint((q, r, x) -> write(r, o, 401, "请先登录")).accessDeniedHandler((q, r, x) -> write(r, o, 403, "无访问权限"))).addFilterBefore(f, UsernamePasswordAuthenticationFilter.class).build();
    }

    /**
     * 输出统一错误结果。
     */
    private void write(jakarta.servlet.http.HttpServletResponse r, ObjectMapper o, int c, String m) throws java.io.IOException {
        r.setStatus(c);
        r.setContentType(MediaType.APPLICATION_JSON_VALUE);
        r.setCharacterEncoding("UTF-8");
        o.writeValue(r.getWriter(), Result.failure(c, m));
    }
}
