package com.JforexRestful.restful.services;

import com.dukascopy.api.IAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements Runnable {

    @Autowired
    private CoreService coreService;

    public Account getAccountInfo() {
        IAccount iAccount = coreService.getAccount();

        if (iAccount == null) {
            return null;
        }

        String accountId = iAccount.getAccountId();
        // Retrieve other account information as needed

        // Create and return the Account instance
        return new Account(accountId);
    }

    @Override
    public void run() {
        Account account = getAccountInfo();
            if (account != null) {
            System.out.println("Account ID: " + account.getAccountId());
        } else {
            System.out.println("Failed to retrieve account information.");
        }
    }

    // Account class to store account information
    public static class Account {
        private String accountId;

        public Account(String accountId) {
            this.accountId = accountId;
        }

        // Getter and setter methods for accountId
        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }
    }
}
