package com.clap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@SpringBootApplication
@EnableNeo4jRepositories("com.clap.repository")
public class ClapApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClapApplication.class, args);
	}
}
