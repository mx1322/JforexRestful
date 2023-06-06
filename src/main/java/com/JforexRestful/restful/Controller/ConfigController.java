package com.JforexRestful.restful.Controller;

import com.JforexRestful.restful.services.CoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/config")
public class ConfigController {
    @Autowired
    CoreService coreService;

    @PutMapping("/updateCredentials")
    public String updateCredentials(@RequestParam String username, @RequestParam String password) {
        try {
            coreService.updateConfig(username, password);
            return "Credentials updated successfully!";
        } catch (Exception e) {
            return "An error occurred while updating the credentials: " + e.getMessage();
        }
    }
}


