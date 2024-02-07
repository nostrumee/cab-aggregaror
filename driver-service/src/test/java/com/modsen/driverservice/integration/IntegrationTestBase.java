package com.modsen.driverservice.integration;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import static com.modsen.driverservice.util.TestUtils.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@Sql(
        scripts = {
                "classpath:sql/delete-data.sql",
                "classpath:sql/insert-data.sql"
        },
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
)
public abstract class IntegrationTestBase {

    protected static PostgreSQLContainer<?> postgres;
    protected static KafkaContainer kafka;
    protected static final KeycloakContainer keycloak;

    static {
        postgres = new PostgreSQLContainer<>(POSTGRES_IMAGE_NAME);
        kafka = new KafkaContainer(
                DockerImageName.parse(KAFKA_IMAGE_NAME)
        );
        keycloak = new KeycloakContainer(KEYCLOAK_IMAGE_NAME)
                        .withRealmImportFile(REALM_IMPORT_FILE_NAME);

        postgres.start();
        kafka.start();
        keycloak.start();
    }

    @DynamicPropertySource
    private static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("postgresql.driver", postgres::getDriverClassName);
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri",
                () -> keycloak.getAuthServerUrl() + REALM_URL);
    }
}
