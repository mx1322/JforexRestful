package com.JforexRestful.restful.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

// Spring Boot ApplicationRunner implementation to start CoreService in a new thread
@Component
public class CoreServiceRunner implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(CoreServiceRunner.class);

    @Autowired
    private CoreService coreService;

    public CoreServiceRunner(CoreService coreService) {
        this.coreService = coreService;
    }

    @Override
    public void run(ApplicationArguments args) {
        Thread thread = new Thread(() -> {
            try {
                coreService.start();
            } catch (Exception e) {
                LOGGER.error("Connection error: " + e.getMessage());
                // take appropriate action here
            }
        });
        thread.start();
    }

}
