package com.company;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NewsBlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewsBlogApplication.class, args);
	}



}
