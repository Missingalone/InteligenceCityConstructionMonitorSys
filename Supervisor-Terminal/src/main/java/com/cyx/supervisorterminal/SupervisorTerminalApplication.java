package com.cyx.supervisorterminal;

import com.cyx.exception.GlobalExceptionHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.context.annotation.Import;

@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
@MapperScan("com.cyx.supervisorterminal.mapper")
@EnableMethodSecurity
@Import(GlobalExceptionHandler.class)
public class SupervisorTerminalApplication {

    public static void main(String[] args) {
        SpringApplication.run(SupervisorTerminalApplication.class, args);
    }

}
