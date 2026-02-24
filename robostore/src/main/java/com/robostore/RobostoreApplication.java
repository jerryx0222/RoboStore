package com.robostore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@SpringBootApplication
public class RobostoreApplication {

	public static void main(String[] args) {
		// http://127.0.0.1:8080/
		SpringApplication.run(RobostoreApplication.class, args);

	}

	@GetMapping("/")
	public String index(){
		return "Hello World Spring boot!";
	}
	

}
