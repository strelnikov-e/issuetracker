package com.strelnikov.issuetracker.controller;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.lifecycle.Startables;

import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.flyway.clean-disabled=false")
@ContextConfiguration(initializers = AbstractControllerTest.Initializer.class)
public class AbstractControllerTest {

    public static final Network NETWORK = Network.newNetwork();

    public static final MySQLContainer<?> MYSQL =
            new MySQLContainer<>("mysql:8.0.33")
                    .withNetworkAliases("mysql")
                    .withNetwork(NETWORK);

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext context) {
            Startables.deepStart(MYSQL).join();

            context.getEnvironment()
                    .getPropertySources()
                    .addFirst(new MapPropertySource(
                            "testcontainers",
                            Map.of(
                                    "spring.datasource.url", MYSQL.getJdbcUrl(),
                                    "spring.datasource.username", MYSQL.getUsername(),
                                    "spring.datasource.password", MYSQL.getPassword()
                            )
                    ));
        }
    }
}
