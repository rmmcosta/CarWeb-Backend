package com.udacity.vehicles.client;

import com.udacity.vehicles.client.prices.PriceClient;
import com.udacity.vehicles.domain.CarExample;
import com.udacity.vehicles.domain.car.Car;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PriceClientTest {
    @Autowired
    PriceClient priceClient;

    @Test
    public void getPrice() {
        Car car = CarExample.getCar();
        car.setId(1L);
        assertNull(car.getPrice());
        car.setPrice(priceClient.getPrice(car.getPlate()));
        assertNotNull(car.getPrice());
    }
}
