package com.udacity.vehicles.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelfEntity {
    Self self;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class Self {
        String href;
    }
}
