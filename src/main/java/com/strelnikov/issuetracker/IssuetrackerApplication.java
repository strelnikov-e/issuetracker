package com.strelnikov.issuetracker;

import com.strelnikov.issuetracker.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class IssuetrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(IssuetrackerApplication.class, args);
	}

}
