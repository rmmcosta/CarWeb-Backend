package com.udacity.vehicles.client;

import com.udacity.vehicles.client.maps.MapsClient;
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
public class MapsClientTest {
    @Autowired
    MapsClient mapsClient;

    @Test
    public void getAddress() {
        Car car = CarExample.getCar();
        assertNull(car.getLocation().getAddress());
        assertNull(car.getLocation().getCity());
        assertNull(car.getLocation().getState());
        assertNull(car.getLocation().getZip());
        car.setLocation(mapsClient.getAddress(car.getLocation()));
        assertNotNull(car.getLocation().getAddress());
        assertNotNull(car.getLocation().getCity());
        assertNotNull(car.getLocation().getState());
        assertNotNull(car.getLocation().getZip());
    }
}
