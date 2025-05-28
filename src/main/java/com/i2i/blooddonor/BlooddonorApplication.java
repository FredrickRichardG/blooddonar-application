package com.i2i.blooddonor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
@EnableCaching
public class BlooddonorApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlooddonorApplication.class, args);
	}

}
