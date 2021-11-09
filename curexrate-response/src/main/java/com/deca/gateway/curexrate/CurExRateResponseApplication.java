package com.deca.gateway.curexrate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.deca.gateway"})
public class CurExRateResponseApplication {

	public static void main(String[] args) {
		SpringApplication.run(CurExRateResponseApplication.class, args);
	}

}
