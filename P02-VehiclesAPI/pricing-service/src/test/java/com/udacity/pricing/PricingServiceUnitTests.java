package com.udacity.pricing;

import com.udacity.pricing.api.PricingController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PricingController.class)
public class PricingServiceUnitTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getPriceWithSuccess() throws Exception {
        mockMvc.perform(get("/services/price?vehicleId=1"))
                .andExpect(status().isOk());
    }

    @Test
    public void getPriceNotFound() throws Exception {
        mockMvc.perform(get("/services/price?vehicleId=33"))
                .andExpect(status().isNotFound());
    }
}
