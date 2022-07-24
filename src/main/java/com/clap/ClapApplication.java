package com.clap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.clap.services.MyUserDetailsService;

@SpringBootApplication
@EnableNeo4jRepositories("com.clap.repository")
@Configuration
public class ClapApplication implements CommandLineRunner{

	@Autowired
	MyUserDetailsService myUserDetailsService;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

	public static void main(String[] args) {
		SpringApplication.run(ClapApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
