package com.udacity.vehicles.service;

import com.udacity.vehicles.domain.OrderStatus;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.order.Order;
import com.udacity.vehicles.domain.order.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CarService carService;

    public List<Order> list() {
        return orderRepository.findAll();
    }

    public Order findById(Long id) {
        return orderRepository.findById(id).orElseThrow(OrderNotFoundException::new);
    }

    public Order save(Order order) {
        Order orderBeforeSave = null;
        if (order.getId() != null) {
            Optional<Order> optionalOrder = orderRepository.findById(order.getId());
            if (optionalOrder.isPresent()) {
                orderBeforeSave = optionalOrder.get();
            }
        }
        Order savedOrder = orderRepository.save(order);
        if (orderBeforeSave == null || (!orderBeforeSave.isFinalized() && savedOrder.isFinalized())) {
            for (Car car : savedOrder.getCarSet()) {
                car.setOrderStatus(orderBeforeSave == null ? OrderStatus.RESERVED : OrderStatus.SOLD);
                carService.save(car);
            }
        }
        return savedOrder;
    }

    public void delete(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(OrderNotFoundException::new);
        orderRepository.delete(order);
    }
}
