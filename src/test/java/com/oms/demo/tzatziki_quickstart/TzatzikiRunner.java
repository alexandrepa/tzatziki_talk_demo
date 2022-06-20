package com.oms.demo.tzatziki_quickstart;

import com.decathlon.tzatziki.steps.KafkaSteps;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.jetbrains.annotations.NotNull;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Map;

@AutoConfigureMockMvc
@RunWith(Cucumber.class)
@ActiveProfiles("cucumber")
@CucumberContextConfiguration
@ContextConfiguration(initializers = TzatzikiRunner.Initializer.class)
@CucumberOptions(plugin = "pretty", extraGlue = {"com.decathlon.tzatziki.steps"}, tags = "not @ignore")
@SpringBootTest(classes = TzatzikiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TzatzikiRunner {
    private static PostgreSQLContainer postgreSQLContainer;

    static {
        postgreSQLContainer = ((PostgreSQLContainer) new PostgreSQLContainer("postgres:12").withTmpFs(Map.of("/var/lib/postgresql/data", "rw"))).withDatabaseName("demo").withUsername("demo").withPassword("demo");
        postgreSQLContainer.start();
    }

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(@NotNull ConfigurableApplicationContext configurableApplicationContext) {
            KafkaSteps.start(Map.of(
                    "transaction.state.log.replication.factor", "1",
                    "transaction.state.log.min.isr", "1",
                    "transaction.state.log.num.partitions", "1"
            ));
            TestPropertyValues.of(
                    "spring.flyway.schemas=demo",
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=demo",
                    "spring.datasource.password=demo",
                    "spring.datasource.driver-class-name=org.postgresql.Driver"
            ).applyTo(configurableApplicationContext);
        }
    }
}