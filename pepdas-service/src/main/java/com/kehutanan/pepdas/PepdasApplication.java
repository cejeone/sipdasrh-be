package com.kehutanan.pepdas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@EnableCaching
@OpenAPIDefinition(
    info = @Info(
        title = "RH Service API",
        version = "1.0",
        description = "API Documentation for RH Service"
    )
)
public class PepdasApplication {
    public static void main(String[] args) {
        SpringApplication.run(PepdasApplication.class, args);
    }
}