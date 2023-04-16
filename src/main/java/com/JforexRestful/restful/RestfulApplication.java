package com.JforexRestful.restful;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.JforexRestful.restful"})
public class RestfulApplication {
    public static void main(String[] args) {
        SpringApplication.run(RestfulApplication.class, args);
    }
}
