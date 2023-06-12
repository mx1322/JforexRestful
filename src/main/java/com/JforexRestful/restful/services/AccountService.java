package com.JforexRestful.restful.Services;

import com.dukascopy.api.IAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements Runnable {

    @Autowired
    private CoreService coreService;

    public Account getAccountInfo() {
        IAccount iAccount = null;

        try {
            iAccount = coreService.getAccount();
        } catch (NullPointerException e) {
            System.out.println("CoreService returned null Account. Please check the CoreService implementation.");
            return null;
        }

        String accountId = iAccount.getAccountId();
        String accountCurrency = iAccount.getAccountCurrency().toString();
        int leverage = (int) iAccount.getLeverage();
        double usedMargin = iAccount.getUsedMargin();
        double useOfLeverage = iAccount.getUseOfLeverage();

        // Create and return the Account instance
        return new Account(accountId, accountCurrency, leverage, usedMargin, useOfLeverage);
    }

    @Override
    public void run() {
        Account account = getAccountInfo();
        if (account != null) {
            System.out.println("Account ID: " + account.getAccountId());
            System.out.println("Account Currency: " + account.getAccountCurrency());
            System.out.println("Leverage: " + account.getLeverage());
            System.out.println("Used Margin: " + account.getUsedMargin());
            System.out.println("Use of Leverage: " + account.getUseOfLeverage());
        } else {
            System.out.println("Failed to retrieve account information.");
        }
    }

    // Account class to store account information
    public static class Account {
        private String accountId;
        private String accountCurrency;
        private double leverage;
        private double usedMargin;
        private double useOfLeverage;

        public Account(String accountId, String accountCurrency, double leverage, double usedMargin, double useOfLeverage) {
            this.accountId = accountId;
            this.accountCurrency = accountCurrency;
            this.leverage = leverage;
            this.usedMargin = usedMargin;
            this.useOfLeverage = useOfLeverage;
        }

        public String getAccountId() {
            return accountId;
        }

        public String getAccountCurrency() {
            return accountCurrency;
        }

        public double getLeverage() {
            return leverage;
        }

        public double getUsedMargin() {
            return usedMargin;
        }

        public double getUseOfLeverage() {
            return useOfLeverage;
        }
    }
}
