package com.udacity.vehicles.repository;

import com.udacity.vehicles.domain.CarExample;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.CarRepository;
import com.udacity.vehicles.domain.manufacturer.Manufacturer;
import com.udacity.vehicles.domain.manufacturer.ManufacturerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CarRepositoryTest {
    @Autowired
    CarRepository carRepository;

    @Autowired
    ManufacturerRepository manufacturerRepository;

    @Test
    public void carRepositoryCrud() {
        long initCount = carRepository.count();
        Car car = carRepository.save(CarExample.getCar());
        assertNotNull(car);
        assertNotNull(car.getId());
        assertNotNull(car.getDetails());
        assertNotNull(car.getDetails().getManufacturer());
        assertEquals(initCount + 1, carRepository.count());
        car.getDetails().setModel("MyModel");
        car = carRepository.save(car);
        assertEquals("MyModel", car.getDetails().getModel());
        Manufacturer manufacturer = new Manufacturer(333, "Manuf333");
        manufacturerRepository.save(manufacturer);

        car.getDetails().setManufacturer(manufacturer);
        car = carRepository.save(car);
        assertEquals(333, car.getDetails().getManufacturer().getCode().longValue());
        assertEquals("Manuf333", car.getDetails().getManufacturer().getName());
    }
}
