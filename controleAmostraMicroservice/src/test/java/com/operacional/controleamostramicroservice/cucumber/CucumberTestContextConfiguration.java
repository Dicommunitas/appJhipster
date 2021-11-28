package com.operacional.controleamostramicroservice.cucumber;

import com.operacional.controleamostramicroservice.ControleAmostraMicroserviceApp;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = ControleAmostraMicroserviceApp.class)
@WebAppConfiguration
public class CucumberTestContextConfiguration {}
