package com.modsen.passengerservice.integration.component;

import com.modsen.passengerservice.integration.TestcontainersBase;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;


@RunWith(Cucumber.class)
@CucumberContextConfiguration
@SpringBootTest
@CucumberOptions(
        plugin = {"pretty"},
        features = "classpath:features"
)
public class ComponentTest extends TestcontainersBase {
}
