package com.udacity.boogle.maps.api;

import com.udacity.boogle.maps.repository.Address;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MapsControllerIntegrationTests {
    @LocalServerPort
    private int port;
    @Autowired
    TestRestTemplate testRestTemplate;

    @Test
    public void after1stCallAlwaysReturnsSameAddress() {
        String lat = "40.73061";
        String lon = "-73.935242";
        String mapsUri = String.format("http://localhost:%s/maps?lat=%s&lon=%s", port, lat, lon);
        ResponseEntity<Address> addressResponseEntity =
                testRestTemplate.getForEntity(mapsUri, Address.class);

        assertEquals(200, addressResponseEntity.getStatusCodeValue());

        //Retrieve the car.
        Address address = addressResponseEntity.getBody();

        assert address != null;

        addressResponseEntity =
                testRestTemplate.getForEntity(mapsUri, Address.class);

        assertEquals(200, addressResponseEntity.getStatusCodeValue());

        //Retrieve the car.
        Address address2ndCall = addressResponseEntity.getBody();

        assert address2ndCall != null;

        assertTrue(isTheSameAddress(address, address2ndCall));
    }

    private boolean isTheSameAddress(Address address1, Address address2) {
        boolean isTheSameAddress = address1.getAddress().equals(address2.getAddress());
        boolean isTheSameCity = address1.getCity().equals(address2.getCity());
        boolean isTheSameZip = address1.getZip().equals(address2.getZip());
        boolean isTheSameState = address1.getState().equals(address2.getState());
        return isTheSameState && isTheSameZip && isTheSameCity && isTheSameAddress;
    }
}
