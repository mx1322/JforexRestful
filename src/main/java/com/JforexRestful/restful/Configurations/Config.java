package com.JforexRestful.restful.Configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Value("${jnlpUrl}")
    private String jnlpUrl;

    private String userName;

    private String password;

    public String getJnlpUrl() {
        return jnlpUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
