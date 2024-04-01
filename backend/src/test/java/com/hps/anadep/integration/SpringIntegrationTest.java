package com.hps.anadep.integration;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features/vulnerability.feature",
        plugin = {"pretty", "html:target/cucumber/anadep"}
)
public class SpringIntegrationTest {

}
