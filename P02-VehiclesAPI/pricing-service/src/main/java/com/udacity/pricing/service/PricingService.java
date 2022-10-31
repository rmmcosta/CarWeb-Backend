package com.udacity.pricing.service;

import com.udacity.pricing.domain.price.Price;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * Implements the pricing service to get prices for each vehicle.
 */
@Service
public class PricingService {

    /**
     * Holds {ID: Price} pairings (current implementation allows for 20 vehicles)
     */
    private Map<String, Price> prices;

    public PricingService() {
        prices = new HashMap<>();
    }

    /**
     * If a valid vehicle ID, gets the price of the vehicle from the stored array.
     * @param vehiclePlate Plate of the vehicle the price is requested for.
     * @return price of the requested vehicle
     * @throws PriceException vehicleID was not found
     */
    public Price getPrice(String vehiclePlate) throws PriceException {

        if (!prices.containsKey(vehiclePlate)) {
            prices.put(vehiclePlate, new Price("USD", randomPrice(), vehiclePlate));
        }

        return prices.get(vehiclePlate);
    }

    /**
     * Gets a random price to fill in for a given vehicle ID.
     * @return random price for a vehicle
     */
    private static BigDecimal randomPrice() {
        return new BigDecimal(ThreadLocalRandom.current().nextDouble(1, 5))
                .multiply(new BigDecimal(5000d)).setScale(2, RoundingMode.HALF_UP);
    }

}
