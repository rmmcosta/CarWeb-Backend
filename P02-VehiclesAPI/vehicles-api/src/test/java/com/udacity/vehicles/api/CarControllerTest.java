package com.udacity.vehicles.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.udacity.vehicles.client.maps.MapsClient;
import com.udacity.vehicles.client.prices.PriceClient;
import com.udacity.vehicles.domain.CarExample;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.manufacturer.Manufacturer;
import com.udacity.vehicles.domain.manufacturer.ManufacturerService;
import com.udacity.vehicles.service.CarService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Implements testing of the CarController class.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class CarControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<Car> json;

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
     * Creates pre-requisites for testing, such as an example car.
     */
    @Before
    public void setup() {
        Car car = CarExample.getCar();
        given(carService.save(any())).willReturn(car);
        given(carService.findById(any())).willReturn(car);
        given(carService.list()).willReturn(Collections.singletonList(car));
    }

    /**
     * Tests for successful creation of new car in the system
     *
     * @throws Exception when car creation fails in the system
     */
    @Test
    public void createCar() throws Exception {
        Car car = CarExample.getCar();
        mvc.perform(
                        post(new URI("/cars"))
                                .content(json.write(car).getJson())
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());
    }

    /**
     * Tests if the read operation appropriately returns a list of vehicles.
     *
     * @throws Exception if the read operation of the vehicle list fails
     */
    @Test
    public void listCars() throws Exception {
        String carsUri = String.format("http://localhost:%s/cars", port);
        ResponseEntity<CarResourcesEntity> carsResponseEntity =
                testRestTemplate.getForEntity(carsUri, CarResourcesEntity.class);

        assertEquals(200, carsResponseEntity.getStatusCodeValue());

        //Retrieve the car.
        CarResourcesEntity carResourcesEntity = carsResponseEntity.getBody();

        assert carResourcesEntity != null;

        Car car = carResourcesEntity._embedded.getCarList().get(0);

        assertEquals(car.getDetails().getModel(), CarExample.getCar().getDetails().getModel());
        assertEquals(car.getDetails().getManufacturer().getCode(), CarExample.getCar().getDetails().getManufacturer().getCode());

        verify(carService, times(1)).list();
    }

    /**
     * Tests the read operation for a single car by ID.
     *
     * @throws Exception if the read operation for a single car fails
     */
    @Test
    public void findCar() throws Exception {
        String carsUri = String.format("http://localhost:%s/cars/%s", port, 1);
        ResponseEntity<Car> carResponseEntity =
                testRestTemplate.getForEntity(carsUri, Car.class);

        assertEquals(200, carResponseEntity.getStatusCodeValue());

        //Retrieve the car.
        Car car = carResponseEntity.getBody();

        assert car != null;

        assertEquals(car.getDetails().getModel(), CarExample.getCar().getDetails().getModel());
        assertEquals(car.getDetails().getManufacturer().getCode(), CarExample.getCar().getDetails().getManufacturer().getCode());

        verify(carService, times(1)).findById(1L);
    }

    /**
     * Tests the deletion of a single car by ID.
     *
     * @throws Exception if the delete operation of a vehicle fails
     */
    @Test
    public void deleteCar() throws Exception {
        mvc.perform(
                        delete("/cars/1")
                )
                .andExpect(status().isNoContent());

        verify(carService, times(1)).delete(1L);

        /*
        String carsUri = String.format("http://localhost:%s/cars/%s", port, 1);
        ResponseEntity<Car> carResponseEntity =
                testRestTemplate.getForEntity(carsUri, Car.class);

        assertEquals(200, carResponseEntity.getStatusCodeValue());

        //Retrieve the car.
        Car car = carResponseEntity.getBody();

        assert car != null;

        testRestTemplate.delete(carsUri);

        carResponseEntity =
                testRestTemplate.getForEntity(carsUri, Car.class);

        assertEquals(404, carResponseEntity.getStatusCodeValue());*/
    }

    @Test
    public void updateCar() throws Exception {
        Car updateCar = CarExample.getCar();
        updateCar.getDetails().setModel("TheModel");
        Manufacturer updateManufacturer = new Manufacturer(444, "Manuf444");
        updateCar.getDetails().setManufacturer(updateManufacturer);

        given(carService.save(any())).willReturn(updateCar);
        //given(manufacturerService.findById(444)).willReturn(updateManufacturer);

        String carsUri = String.format("http://localhost:%s/cars/%s", port, 1);

        HttpHeaders headers = new HttpHeaders();
        Map<String, String> param;
        ObjectMapper objectMapper = new ObjectMapper();
        param = objectMapper.convertValue(updateCar, Map.class);
        HttpEntity<Car> requestEntity = new HttpEntity<>(updateCar, headers);

        ResponseEntity<Car> carResponseEntity = testRestTemplate.exchange(carsUri, HttpMethod.PUT, requestEntity, Car.class, param);

        assertEquals(200, carResponseEntity.getStatusCodeValue());

        //Retrieve the car.
        Car car = carResponseEntity.getBody();

        assert car != null;

        assertEquals("TheModel", car.getDetails().getModel());
        assertEquals(444, car.getDetails().getManufacturer().getCode().longValue());
        assertEquals("Manuf444", car.getDetails().getManufacturer().getName());
    }
}