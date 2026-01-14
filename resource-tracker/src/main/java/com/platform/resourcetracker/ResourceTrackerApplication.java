package com.platform.resourcetracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ResourceTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResourceTrackerApplication.class, args);
    }

}