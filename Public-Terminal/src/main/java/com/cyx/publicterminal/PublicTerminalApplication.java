package com.cyx.publicterminal;

import com.cyx.exception.GlobalExceptionHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Import;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** 公众查询与反馈服务启动入口。 */
@SpringBootApplication
@MapperScan("com.cyx.publicterminal.mapper")
@Import(GlobalExceptionHandler.class)
public class PublicTerminalApplication {

    public static void main(String[] args) {
        SpringApplication.run(PublicTerminalApplication.class, args);
    }

}
