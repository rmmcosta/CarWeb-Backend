package com.udacity.vehicles.domain;

import com.udacity.vehicles.domain.order.Order;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class OrderExample {
    public static Order getOrder() {
        Order order = new Order();
        order.setBuyer("Ricardo Costa");
        order.setCreatedAt(LocalDateTime.now());
        order.setDeliveryLocation(new Location(0d, 0d));
        order.setCarSet(Set.of(CarExample.getCar()));
        return order;
    }
    public static boolean isTheSameOrder(Order order1, Order order2) {
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
