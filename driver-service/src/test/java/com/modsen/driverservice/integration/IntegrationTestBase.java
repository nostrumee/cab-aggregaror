package com.modsen.driverservice.integration;

import com.modsen.driverservice.util.PropertiesUtil;
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

    private static final String POSTGRES_IMAGE_NAME;
    private static final String KAFKA_IMAGE_NAME;

    protected static PostgreSQLContainer<?> postgres;
    protected static KafkaContainer kafka;

    static {
        POSTGRES_IMAGE_NAME = PropertiesUtil.get(POSTGRES_IMAGE_NAME_KEY);
        KAFKA_IMAGE_NAME = PropertiesUtil.get(KAFKA_IMAGE_NAME_KEY);

        postgres = new PostgreSQLContainer<>(
                POSTGRES_IMAGE_NAME
        );
        kafka = new KafkaContainer(
                DockerImageName.parse(KAFKA_IMAGE_NAME)
        );

        postgres.start();
        kafka.start();
    }

    @DynamicPropertySource
    private static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("postgresql.driver", postgres::getDriverClassName);
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }
}
