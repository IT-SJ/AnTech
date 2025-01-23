package com.ans.antech;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.ans.antech.mapper")
public class AntechApplication {

	public static void main(String[] args) {
		SpringApplication.run(AntechApplication.class, args);
	}

}