package com.clap.cucumber;

import com.clap.ClapApplicationApp;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = ClapApplicationApp.class)
@WebAppConfiguration
public class CucumberTestContextConfiguration {}
