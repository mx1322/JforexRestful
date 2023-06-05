package com.JforexRestful.restful.Controller;

import com.JforexRestful.restful.services.AccountService;
import com.JforexRestful.restful.services.AccountService.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/account")
    public Account getAccountInfo() {
        System.out.println("Account ID: " + accountService.getAccountInfo());
        return accountService.getAccountInfo();
    }
}