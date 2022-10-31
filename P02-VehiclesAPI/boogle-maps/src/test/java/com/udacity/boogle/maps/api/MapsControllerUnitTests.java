package com.udacity.boogle.maps.api;


import com.udacity.boogle.maps.service.MockAddressService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(MapsController.class)
public class MapsControllerUnitTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    MockAddressService mockAddressService;

    @Test
    public void getAddress() throws Exception {
        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.put("lat", List.of("40.73061"));
        requestParams.put("lon", List.of("-73.935242"));
        mockMvc.perform(get("/maps").params(requestParams))
                .andExpect(status().isOk());
    }
}
