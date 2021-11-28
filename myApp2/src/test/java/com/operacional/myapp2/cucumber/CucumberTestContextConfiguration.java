package com.operacional.myapp2.cucumber;

import com.operacional.myapp2.MyApp2App;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = MyApp2App.class)
@WebAppConfiguration
public class CucumberTestContextConfiguration {}
