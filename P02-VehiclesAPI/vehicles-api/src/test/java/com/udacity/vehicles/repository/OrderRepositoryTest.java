package com.udacity.vehicles.repository;

import com.udacity.vehicles.domain.CarExample;
import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.OrderExample;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.CarRepository;
import com.udacity.vehicles.domain.order.Order;
import com.udacity.vehicles.domain.order.OrderRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderRepositoryTest {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    CarRepository carRepository;

    @Test
    public void orderRepositoryCrud() {
        long initCount = orderRepository.count();
        Order order = OrderExample.getOrder();
        for (Car car : order.getCarSet()) {
            car.setPlate(String.valueOf(LocalDateTime.now()));
            carRepository.save(car);
        }
        order = orderRepository.save(order);
        assertNotNull(order);
        assertNotNull(order.getId());
        assertEquals(initCount + 1, orderRepository.count());
        orderRepository.delete(order);
        assertEquals(initCount, orderRepository.count());
    }
}
