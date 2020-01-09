package com.robi.foodiy;

import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.**")
@SpringBootApplication
public class FoodiyMain {

	public static void main(String[] args) {
		MDC.put("layer", "SYS");
		SpringApplication.run(FoodiyMain.class, args);
	}
}
