package com.deca.gateway.curexrate;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication(scanBasePackages = {"com.deca.gateway"})
public class CurExRateRequestApplication {

	public static void main(String[] args) {
		SpringApplication.run(CurExRateRequestApplication.class, args);
	}

}
