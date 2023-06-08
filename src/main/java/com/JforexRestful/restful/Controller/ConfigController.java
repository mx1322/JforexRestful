package com.JforexRestful.restful.Controller;

import com.JforexRestful.restful.services.CoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/config")
public class ConfigController {

    @Autowired
    private CoreService coreService;

    @PostMapping("/update")
    public String updateConfig(@RequestParam String userName, @RequestParam String password) {
        try {
            coreService.updateConfig(userName, password);
            return "Configuration updated successfully and service started.";
        } catch (Exception e) {
            return "Failed to update configuration and start service: " + e.getMessage();
        }
    }
}
