package com.cyx.supervisorterminal;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.cyx.supervisorterminal.mapper")
public class SupervisorTerminalApplication {

    public static void main(String[] args) {
        SpringApplication.run(SupervisorTerminalApplication.class, args);
    }

}
