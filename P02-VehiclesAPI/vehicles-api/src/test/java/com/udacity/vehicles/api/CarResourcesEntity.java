package com.udacity.vehicles.api;

import com.udacity.vehicles.domain.car.Car;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class CarResourcesEntity {
    CarList _embedded;
    SelfEntity _links;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class CarList {
        List<Car> carList;
    }
}
