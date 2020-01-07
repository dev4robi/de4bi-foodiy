package com.robi.foodiy;

import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		MDC.put("layer", "SYS");
		SpringApplication.run(DemoApplication.class, args);
	}

}
