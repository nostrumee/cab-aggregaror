package com.modsen.passengerservice.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class TestcontainersConfig {

    @Bean
    @ServiceConnection
    public PostgreSQLContainer<?> postgreSQLContainer(DynamicPropertyRegistry registry) {
        var postgres = new PostgreSQLContainer<>("postgres:15-alpine");
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("postgresql.driver", postgres::getDriverClassName);
        return postgres;
    }

    @Bean
    @ServiceConnection
    public KafkaContainer kafkaContainer(DynamicPropertyRegistry registry) {
        var kafka = new KafkaContainer(
                DockerImageName.parse("confluentinc/cp-kafka:7.3.3")
        );
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
        return kafka;
    }
}
