package com.cyx.enterpriseterminal;

import com.cyx.exception.GlobalExceptionHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.context.annotation.Import;

/**
 * 施工企业端服务启动入口。
 */
@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
@MapperScan("com.cyx.enterpriseterminal.mapper")
@EnableMethodSecurity
@Import(GlobalExceptionHandler.class)
public class EnterpriseTerminalApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnterpriseTerminalApplication.class, args);
    }

}
