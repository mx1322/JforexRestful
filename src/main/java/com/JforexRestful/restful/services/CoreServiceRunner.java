package com.JforexRestful.restful.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

// Spring Boot ApplicationRunner implementation to start CoreService in a new thread
@Component
public class CoreServiceRunner implements ApplicationRunner {

    @Autowired
    private CoreService coreService;

    @Override
    public void run(ApplicationArguments args) {
        Thread thread = new Thread(() -> {
            try {
                coreService.start();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
    }
}
