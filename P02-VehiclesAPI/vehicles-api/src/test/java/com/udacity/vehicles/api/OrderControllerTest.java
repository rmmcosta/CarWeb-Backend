package com.udacity.vehicles.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udacity.vehicles.client.maps.MapsClient;
import com.udacity.vehicles.client.prices.PriceClient;
import com.udacity.vehicles.domain.CarExample;
import com.udacity.vehicles.domain.OrderExample;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.manufacturer.Manufacturer;
import com.udacity.vehicles.domain.manufacturer.ManufacturerService;
import com.udacity.vehicles.domain.order.Order;
import com.udacity.vehicles.service.CarService;
import com.udacity.vehicles.service.OrderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Implements testing of the OrderController class.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class OrderControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<Order> json;

    @MockBean
    private OrderService orderService;

    @MockBean
    private CarService carService;

    @MockBean
    private ManufacturerService manufacturerService;

    @MockBean
    private PriceClient priceClient;

    @MockBean
    private MapsClient mapsClient;

    @Autowired
    TestRestTemplate testRestTemplate;

    /**
     * Creates pre-requisites for testing, such as an example car and an example order
     */
    @Before
    public void setup() {
        Car car = CarExample.getCar();
        given(carService.save(any())).willReturn(car);
        given(carService.findById(any())).willReturn(car);
        given(carService.list()).willReturn(Collections.singletonList(car));

        Order order = OrderExample.getOrder();
        given(orderService.save(any())).willReturn(order);
        given(orderService.findById(any())).willReturn(order);
        given(orderService.list()).willReturn(Collections.singletonList(order));
    }

    /**
     * Tests for successful creation of new order in the system
     *
     * @throws Exception when order creation fails in the system
     */
    @Test
    public void createOrder() throws Exception {
        Order order = OrderExample.getOrder();
        mvc.perform(
                        post(new URI("/orders"))
                                .content(json.write(order).getJson())
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());
    }

    /**
     * Tests if the read operation appropriately returns a list of orders.
     *
     * @throws Exception if the read operation of the order list fails
     */
    @Test
    public void listOrders() throws Exception {
        String ordersUri = String.format("http://localhost:%s/orders", port);
        ResponseEntity<OrderResourcesEntity> ordersResponseEntity =
                testRestTemplate.getForEntity(ordersUri, OrderResourcesEntity.class);

        assertEquals(200, ordersResponseEntity.getStatusCodeValue());

        //Retrieve the car.
        OrderResourcesEntity orderResourcesEntity = ordersResponseEntity.getBody();

        assert orderResourcesEntity != null;

        Order order = orderResourcesEntity._embedded.getOrderList().get(0);

        assertTrue(OrderExample.isTheSameOrder(order, OrderExample.getOrder()));

        verify(orderService, times(1)).list();
    }

    /**
     * Tests the read operation for a single order by ID.
     *
     * @throws Exception if the read operation for a single order fails
     */
    @Test
    public void findCar() throws Exception {
        String ordersUri = String.format("http://localhost:%s/orders/%s", port, 1);
        ResponseEntity<Order> orderResponseEntity =
                testRestTemplate.getForEntity(ordersUri, Order.class);

        assertEquals(200, orderResponseEntity.getStatusCodeValue());

        //Retrieve the car.
        Order order = orderResponseEntity.getBody();

        assert order != null;

        assertTrue(OrderExample.isTheSameOrder(order, OrderExample.getOrder()));

        verify(orderService, times(1)).findById(1L);
    }

    /**
     * Tests the deletion of a single order by ID.
     *
     * @throws Exception if the delete operation of an order fails
     */
    @Test
    public void deleteOrder() throws Exception {
        mvc.perform(
                        delete("/orders/1")
                )
                .andExpect(status().isNoContent());

        verify(orderService, times(1)).delete(1L);
    }
}