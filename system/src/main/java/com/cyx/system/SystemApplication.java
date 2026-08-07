package com.cyx.system;

import com.cyx.exception.GlobalExceptionHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.context.annotation.Import;

@MapperScan("com.cyx.system.mapper")
@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
@EnableMethodSecurity
@Import(GlobalExceptionHandler.class)
public class SystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class, args);
    }
}
