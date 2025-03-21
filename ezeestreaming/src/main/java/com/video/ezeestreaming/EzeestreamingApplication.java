package com.video.ezeestreaming;

import org.springframework.boot.SpringApplication;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
 

@SpringBootApplication
@EnableJpaRepositories  
public class EzeestreamingApplication {

	public static void main(String[] args) {
		SpringApplication.run(EzeestreamingApplication.class, args);
	}

}
