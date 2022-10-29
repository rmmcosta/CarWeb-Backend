package com.udacity.vehicles.service;

import com.udacity.vehicles.domain.CarExample;
import com.udacity.vehicles.domain.car.Car;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CarServiceTest {
    @Autowired
    CarService carService;

    @Test
    public void carSaveGetListDeleteWithSuccess() {
        int initCount = carService.list().size();
        Car newCar = carService.save(CarExample.getCar());
        assertEquals(initCount + 1, carService.list().size());
        assertTrue(isTheSameCar(newCar, carService.findById(newCar.getId())));
        Car carFromList = carService.list().stream().filter(car -> Objects.equals(car.getId(), newCar.getId())).findFirst().orElseThrow(CarNotFoundException::new);
        assertTrue(isTheSameCar(newCar, carFromList));
        carService.delete(newCar.getId());
        assertEquals(initCount, carService.list().size());
        assertEquals(0, carService.list().stream().filter(car -> Objects.equals(car.getId(), newCar.getId())).count());
    }

    @Test(expected = CarNotFoundException.class)
    public void throwCarNotFoundWhenDelete() {
        carService.delete(333L);
    }

    @Test
    public void priceIsNotNull() {
        Car newCar = carService.save(CarExample.getCar());
        assertNull(newCar.getPrice());
        //only when getting the car the price is calculated
        newCar = carService.findById(newCar.getId());
        assertNotNull(newCar.getPrice());
    }

    @Test
    public void addressIsNotNull() {
        Car newCar = carService.save(CarExample.getCar());
        assertNull(newCar.getLocation().getAddress());
        assertNull(newCar.getLocation().getCity());
        assertNull(newCar.getLocation().getState());
        assertNull(newCar.getLocation().getZip());
        //only when getting the car the address is filled
        newCar = carService.findById(newCar.getId());
        assertNotNull(newCar.getLocation().getAddress());
        assertNotNull(newCar.getLocation().getCity());
        assertNotNull(newCar.getLocation().getState());
        assertNotNull(newCar.getLocation().getZip());
    }

    private boolean isTheSameCar(Car car1, Car car2) {
        boolean isTheSameId = Objects.equals(car1.getId(), car2.getId());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        boolean wasCreateAtTheSameTime = car1.getCreatedAt().format(dateTimeFormatter).equals(car2.getCreatedAt().format(dateTimeFormatter));
        boolean isTheSameModel = Objects.equals(car1.getDetails().getModel(), car2.getDetails().getModel());
        return isTheSameId & wasCreateAtTheSameTime & isTheSameModel;
    }
}
