package com.xinsteel.epaike;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.xinsteel.epaike.dao")
public class EpaikeApplication {

    public static void main(String[] args) {
        SpringApplication.run(EpaikeApplication.class, args);
    }

}
