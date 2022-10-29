package com.udacity.pricing;

import com.udacity.pricing.domain.price.Price;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PricingServiceIntegrationTests {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void getPriceWithSuccess() {
        webTestClient
                .get()
                .uri("/services/price?vehicleId=1")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Price.class);
    }

    @Test
    public void getPriceNotFound() {
        webTestClient
                .get()
                .uri("/services/price?vehicleId=33")
                .exchange()
                .expectStatus()
                .isNotFound();
    }
}
