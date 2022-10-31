package com.udacity.pricing;

import com.udacity.pricing.domain.price.Price;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PricingServiceIntegrationTests {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void getPriceWithSuccess() {
        webTestClient
                .get()
                .uri("/services/price?vehiclePlate=59-13-UM")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Price.class);
    }

    @Test
    public void getDiffPriceForDiffPlate() {
        WebTestClient.BodySpec priceBodySpec = webTestClient
                .get()
                .uri("/services/price?vehiclePlate=59-13-UM")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Price.class);
        Price price1 = (Price) priceBodySpec.returnResult().getResponseBody();

        priceBodySpec = webTestClient
                .get()
                .uri("/services/price?vehiclePlate=96-99-NE")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Price.class);
        Price price2 = (Price) priceBodySpec.returnResult().getResponseBody();

        assert price1 != null;
        assert price2 != null;
        assertNotEquals(price1.getPrice(), price2.getPrice());
    }

    @Test
    public void getSamePriceForSamePlate() {
        WebTestClient.BodySpec priceBodySpec = webTestClient
                .get()
                .uri("/services/price?vehiclePlate=59-13-UM")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Price.class);
        Price price1 = (Price) priceBodySpec.returnResult().getResponseBody();

        priceBodySpec = webTestClient
                .get()
                .uri("/services/price?vehiclePlate=59-13-UM")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Price.class);
        Price price2 = (Price) priceBodySpec.returnResult().getResponseBody();

        assert price1 != null;
        assert price2 != null;
        assertEquals(price1.getPrice(), price2.getPrice());
        assertEquals(price1.getCurrency(), price2.getCurrency());
    }
}
