package com.udacity.vehicles.service;

import com.udacity.vehicles.client.maps.MapsClient;
import com.udacity.vehicles.client.prices.PriceClient;
import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.CarRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

/**
 * Implements the car service create, read, update or delete
 * information about vehicles, as well as gather related
 * location and price data when desired.
 */
@Service
public class CarService {

    private final CarRepository repository;
    private final MapsClient mapsClient;
    private final PriceClient priceClient;

    public CarService(CarRepository repository, MapsClient mapsClient, PriceClient priceClient) {
        this.repository = repository;
        this.mapsClient = mapsClient;
        this.priceClient = priceClient;
    }

    /**
     * Gathers a list of all vehicles
     *
     * @return a list of all vehicles in the CarRepository
     */
    public List<Car> list() {
        return repository.findAll();
    }

    /**
     * Gets car information by ID (or throws exception if non-existent)
     *
     * @param id the ID number of the car to gather information on
     * @return the requested car's information, including location and price
     */
    public Car findById(Long id) {
        Car car = repository.findById(id).orElseThrow(CarNotFoundException::new);
        car.setPrice(priceClient.getPrice(car.getPlate()));
        car.setLocation(mapsClient.getAddress(car.getLocation()));

        return car;
    }

    /**
     * Either creates or updates a vehicle, based on prior existence of car
     *
     * @param car A car object, which can be either new or existing
     * @return the new/updated car is stored in the repository
     */
    public Car save(Car car) {
        //check if a car is not registered with the same plate yet
        List<Car> cars = repository.findAll();
        boolean carWithSamePLate = cars.stream().anyMatch(currCar -> Objects.equals(currCar.getPlate(), car.getPlate()) && !Objects.equals(currCar.getId(), car.getId()));
        if (carWithSamePLate) {
            throw new CarWithSamePlateException();
        }
        if (car.getId() != null) {
            return repository.findById(car.getId())
                    .map(carToBeUpdated -> {
                        carToBeUpdated.setDetails(car.getDetails());
                        carToBeUpdated.setLocation(car.getLocation());
                        carToBeUpdated.setOrderStatus(car.getOrderStatus());
                        return repository.save(carToBeUpdated);
                    }).orElseThrow(CarNotFoundException::new);
        }

        return repository.save(car);
    }

    /**
     * Deletes a given car by ID
     *
     * @param id the ID number of the car to delete
     */
    public void delete(Long id) {
        Car car = repository.findById(id).orElseThrow(CarNotFoundException::new);
        repository.delete(car);
    }
}
