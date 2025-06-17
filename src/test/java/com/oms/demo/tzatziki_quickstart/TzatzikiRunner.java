package com.oms.demo.tzatziki_quickstart;

import com.decathlon.tzatziki.steps.KafkaSteps;
import com.decathlon.tzatziki.utils.MockFaster;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.jetbrains.annotations.NotNull;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.util.Map;

@RunWith(Cucumber.class)
@CucumberContextConfiguration
@ContextConfiguration(initializers = TzatzikiRunner.Initializer.class)
@CucumberOptions(plugin = "pretty", extraGlue = {"com.decathlon.tzatziki.steps"}, tags = "not @ignore")
@SpringBootTest(classes = TzatzikiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TzatzikiRunner {
    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15").withTmpFs(Map.of("/var/lib/postgresql/data", "rw")).waitingFor(Wait.forListeningPort());

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(@NotNull ConfigurableApplicationContext configurableApplicationContext) {
            KafkaSteps.start();
            postgres.start();
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgres.getJdbcUrl(),
                    "spring.datasource.username=" + postgres.getUsername(),
                    "spring.datasource.password=" + postgres.getPassword(),
                    "default-properties.gateway_url=" + MockFaster.url(),
                    "default-properties.kafka_bootstrap_url=" + KafkaSteps.bootstrapServers(),
                    "default-properties.kafka_schema_registry_url=" + KafkaSteps.schemaRegistryUrl()
            ).applyTo(configurableApplicationContext);
        }
    }
}