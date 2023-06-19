package com.JforexRestful.restful.Controllers;

import com.JforexRestful.restful.Services.CoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ConfigController {

    @Autowired
    private CoreService coreService;

    @PostMapping("/UpdateConf")
    public String updateConfig(@RequestParam String userName, @RequestParam String password) {
        try {
            coreService.updateConfig(userName, password);
            return "Configuration updated successfully and service started.";
        } catch (Exception e) {
            return "Failed to update configuration and start service: " + e.getMessage();
        }
    }
}
