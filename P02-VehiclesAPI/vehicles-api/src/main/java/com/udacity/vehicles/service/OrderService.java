package com.udacity.vehicles.service;

import com.udacity.vehicles.client.maps.MapsClient;
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

    @Autowired
    private MapsClient mapsClient;

    public List<Order> list() {
        return orderRepository.findAll();
    }

    public Order findById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(OrderNotFoundException::new);
        order.setDeliveryLocation(mapsClient.getAddress(order.getDeliveryLocation()));
        return order;
    }

    public Order save(Order order) {
        boolean orderIsNew = order.getId() == null;
        if (orderIsNew) {
            for (Car car : order.getCarSet()) {
                Car carFromDB = carService.findById(car.getId());
                if (carFromDB.getOrderStatus() != OrderStatus.FOR_SALE) {
                    throw new CarNotForSaleException();
                }
            }
        }
        boolean orderWasFinalized = false;
        if (order.getId() != null) {
            Optional<Order> optionalOrder = orderRepository.findById(order.getId());
            if (optionalOrder.isPresent()) {
                orderWasFinalized = optionalOrder.get().isFinalized();
            }
        }
        order.setDeliveryLocation(mapsClient.getAddress(order.getDeliveryLocation()));
        Order savedOrder = orderRepository.save(order);
        if (orderIsNew || (!orderWasFinalized && savedOrder.isFinalized())) {
            for (Car car : savedOrder.getCarSet()) {
                car.setOrderStatus(orderIsNew ? OrderStatus.RESERVED : OrderStatus.SOLD);
                carService.save(car);
            }
        }
        return savedOrder;
    }

    public void delete(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(OrderNotFoundException::new);
        orderRepository.delete(order);
        for (Car car : order.getCarSet()) {
            car.setOrderStatus(OrderStatus.FOR_SALE);
            carService.save(car);
        }
    }
}
