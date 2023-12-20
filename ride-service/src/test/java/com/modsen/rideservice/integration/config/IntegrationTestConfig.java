package com.modsen.rideservice.integration.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static com.modsen.rideservice.util.TestUtils.*;

@TestConfiguration
public class IntegrationTestConfig {

    @Bean
    public WireMockServer wiremockServer() {
        WireMockServer wireMockServer = new WireMockServer(
                WireMockConfiguration.wireMockConfig().port(WIREMOCK_PORT)
        );
        wireMockServer.start();
        WireMock.configureFor(wireMockServer.getOptions().bindAddress(), wireMockServer.port());
        return wireMockServer;
    }
}
