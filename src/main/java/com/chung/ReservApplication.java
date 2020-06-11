package com.chung;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("filepath.properties")
public class ReservApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservApplication.class, args);
	}

}
