package com.operacional.controleamostragateway.cucumber;

import com.operacional.controleamostragateway.ControleAmostraGatewayApp;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = ControleAmostraGatewayApp.class)
@WebAppConfiguration
public class CucumberTestContextConfiguration {}
