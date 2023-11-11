package com.dbdb.dbdb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DbdbApplication {

	public static void main(String[] args) {
		SpringApplication.run(DbdbApplication.class, args);
	}

}
