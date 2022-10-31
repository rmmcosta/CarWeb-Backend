package com.udacity.vehicles.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@EnableSwagger2
@Configuration
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false);
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Location API",
                "This API returns a list of vehicles, it's price and it's location",
                "1.0",
                "https://github.com/rmmcosta/CarWeb-Backend",
                new Contact("Ricardo Costa", "https://github.com/rmmcosta/CarWeb-Backend", "ricardocosta101085@gmail.com"),
                "License of API", "https://github.com/rmmcosta/CarWeb-Backend", Collections.emptyList());
    }

}
