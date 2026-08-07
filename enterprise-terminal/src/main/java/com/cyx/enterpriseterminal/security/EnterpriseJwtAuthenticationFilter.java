package com.cyx.enterpriseterminal.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.*;

/**
 * 校验 auth 模块签发的 JWT，并恢复企业端方法权限。
 */
@Component
public class EnterpriseJwtAuthenticationFilter extends OncePerRequestFilter {
    private final SecretKey key;

    public EnterpriseJwtAuthenticationFilter(@Value("${auth.jwt.secret}") String secret) {
        key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
    }

    /**
     * 从 Bearer Token 建立 Spring Security 认证上下文。
     */
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        String h = req.getHeader("Authorization");
        if (h != null && h.startsWith("Bearer ") && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                Claims c = Jwts.parser().verifyWith(key).build().parseSignedClaims(h.substring(7)).getPayload();
                List<SimpleGrantedAuthority> a = list(c, "roles").stream().map(v -> new SimpleGrantedAuthority("ROLE_" + v)).collect(java.util.stream.Collectors.toList());
                a.addAll(list(c, "permissions").stream().map(SimpleGrantedAuthority::new).toList());
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(c.getSubject(), null, a));
            } catch (JwtException | IllegalArgumentException exception) {
                org.slf4j.LoggerFactory.getLogger(getClass()).debug("JWT 解析失败: {}", exception.getMessage());
                SecurityContextHolder.clearContext();
            }
        }
        chain.doFilter(req, res);
    }

    /**
     * 将 JWT 数组 claim 转换为字符串列表。
     */
    private List<String> list(Claims c, String k) {
        List<?> v = c.get(k, List.class);
        return v == null ? List.of() : v.stream().map(Object::toString).toList();
    }
}
