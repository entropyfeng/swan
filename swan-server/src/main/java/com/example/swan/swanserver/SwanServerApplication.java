package com.example.swan.swanserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author feng
 * SpringBoot 入口类
 */
@SpringBootApplication
public class SwanServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SwanServerApplication.class, args);
    }

}
