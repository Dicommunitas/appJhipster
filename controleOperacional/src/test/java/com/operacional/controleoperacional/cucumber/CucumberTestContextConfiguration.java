package com.operacional.controleoperacional.cucumber;

import com.operacional.controleoperacional.ControleOperacionalApp;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = ControleOperacionalApp.class)
@WebAppConfiguration
public class CucumberTestContextConfiguration {}
