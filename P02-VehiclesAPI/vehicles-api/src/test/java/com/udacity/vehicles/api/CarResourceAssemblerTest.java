package com.udacity.vehicles.api;


import com.udacity.vehicles.domain.CarExample;
import com.udacity.vehicles.domain.Condition;
import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.Details;
import com.udacity.vehicles.domain.manufacturer.Manufacturer;
import org.junit.Before;
import org.junit.Test;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class CarResourceAssemblerTest {
    private CarResourceAssembler carResourceAssembler;

    @Before
    public void setup() {
        carResourceAssembler = new CarResourceAssembler();
    }

    @Test
    public void carToResourceWithSuccess() {
        Resource<Car> carResource = carResourceAssembler.toResource(CarExample.getCar());
        System.out.println(carResource);
    }

    @Test
    public void carToResourceEmptyCarNoException() {
        Resource<Car> carResource = carResourceAssembler.toResource(new Car());
        System.out.println(carResource);
    }

    @Test
    public void carToResourceValidResponse() {
        Car car = CarExample.getCar();
        car.setId(3L);//without the id the expand() function will raise an exception
        Resource<Car> carResource = carResourceAssembler.toResource(car);
        try {
            ResponseEntity.created(new URI(carResource.getId().expand().getHref())).body(carResource);
        } catch (URISyntaxException e) {
            fail();
        }
    }
}
