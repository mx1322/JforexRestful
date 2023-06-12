package com.JforexRestful.restful.Controllers;

import com.JforexRestful.restful.Services.AccountService;
import com.JforexRestful.restful.Services.AccountService.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/AccountID")
    public Account getAccountInfo() {
        System.out.println("Account ID: " + accountService.getAccountInfo());
        return accountService.getAccountInfo();
    }
}