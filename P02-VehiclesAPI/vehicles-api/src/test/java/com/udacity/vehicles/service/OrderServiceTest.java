package com.udacity.vehicles.service;

import com.udacity.vehicles.domain.OrderExample;
import com.udacity.vehicles.domain.OrderStatus;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.order.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderServiceTest {
    @Autowired
    OrderService orderService;

    @Test
    public void orderSaveGetListDeleteWithSuccess() {
        int initCount = orderService.list().size();
        Order orderExample = OrderExample.getOrder();
        Order newOrder = orderService.save(orderExample);
        assertEquals(initCount + 1, orderService.list().size());
        assertTrue(isTheSameOrder(newOrder, orderService.findById(newOrder.getId())));
        Order orderFromList = orderService
                .list()
                .stream()
                .filter(order -> Objects.equals(order.getId(),
                        newOrder.getId())
                ).findFirst().orElseThrow(OrderNotFoundException::new);
        assertTrue(isTheSameOrder(newOrder, orderFromList));
        //check car order status after saving order
        for (Car car : newOrder.getCarSet()) {
            assertEquals(OrderStatus.RESERVED, car.getOrderStatus());
        }
        //check car order status after getting order from db
        Order orderFromDB = orderService.findById(newOrder.getId());
        for (Car car : orderFromDB.getCarSet()) {
            assertEquals(OrderStatus.RESERVED, car.getOrderStatus());
        }
        orderService.delete(newOrder.getId());
        assertEquals(initCount, orderService.list().size());
        assertEquals(0, orderService.list()
                .stream()
                .filter(order -> Objects.equals(order.getId(),
                        newOrder.getId())
                ).count());
    }

    @Test
    public void finalizeOrderCarsSold() {
        Order orderExample = OrderExample.getOrder();
        Order newOrder = orderService.save(orderExample);
        assertFalse(newOrder.isFinalized());
        for (Car car : newOrder.getCarSet()) {
            assertEquals(OrderStatus.RESERVED, car.getOrderStatus());
        }
        Order orderFromDB = orderService.findById(newOrder.getId());
        for (Car car : orderFromDB.getCarSet()) {
            assertEquals(OrderStatus.RESERVED, car.getOrderStatus());
        }
        newOrder.setFinalized(true);
        newOrder = orderService.save(newOrder);
        for (Car car : newOrder.getCarSet()) {
            assertEquals(OrderStatus.SOLD, car.getOrderStatus());
        }
        orderFromDB = orderService.findById(newOrder.getId());
        for (Car car : orderFromDB.getCarSet()) {
            assertEquals(OrderStatus.SOLD, car.getOrderStatus());
        }
    }

    private boolean isTheSameOrder(Order order1, Order order2) {
        String pattern = "dd/MM/yyyy HH:mm:ss";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        boolean isTheSameDate = order1.getCreatedAt()
                .format(dateTimeFormatter)
                .equals(order2.getCreatedAt()
                        .format(dateTimeFormatter)
                );
        boolean isTheSameBuyer = order1.getBuyer().equals(order2.getBuyer());
        return isTheSameBuyer && isTheSameDate;
    }
}
