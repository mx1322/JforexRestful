package com.JforexRestful.restful.Services;

import com.JforexRestful.restful.Configurations.Config;
import com.dukascopy.api.*;
import com.dukascopy.api.indicators.IIndicator;
import com.dukascopy.api.system.ClientFactory;
import com.dukascopy.api.system.IClient;
import com.dukascopy.api.system.ISystemListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.HashSet;
import java.util.Set;

@Component
public class CoreService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CoreService.class);

    private int lightReconnects = 3;
    private IClient client;
    private IEngine engine;
    private IContext context;
    private IAccount account;
    private IIndicators indicators;
    @Autowired
    private Config config;

    public void setContext(IContext context) {
        this.context = context;
    }

    public void setEngine(IEngine engine) {
        this.engine = engine;
    }

    public void start() {
        try {
            LOGGER.info("Starting Dukascopy connection...");
            client = client == null ? ClientFactory.getDefaultInstance() : client;
            setSystemListener();
            tryToConnect();
            subscribeToInstruments();
            LOGGER.info("Dukascopy connection started successfully");
        } catch (Exception e) {
            LOGGER.error("Failed to start Dukascopy connection: " + e.getMessage());
            // Handle the exception here, e.g., return an error response to the client
        }
    }

    @PreDestroy
    public void stop() {
        try {
            if (client != null && client.isConnected()) {
                LOGGER.info("Stopping Dukascopy connection...");
                client.disconnect();
                LOGGER.info("Dukascopy connection stopped successfully");
            }
        } catch (Exception e) {
            LOGGER.error("Failed to stop Dukascopy connection: " + e.getMessage());
        }
    }

    private void startStrategy() {
        LOGGER.info("Starting strategy");
        try {
            BarboneStrategy strategy = new BarboneStrategy(this);
            client.startStrategy(strategy);

            // wait for the strategy to start
            while (client.getStartedStrategies().size() == 0) {
                Thread.sleep(1000);
            }

            // get the instance of the strategy
            while (strategy.getContext() == null || strategy.getAccount() == null) {
                LOGGER.info("Waiting for strategy to initialize...");
                Thread.sleep(1000);
            }

            // access the variables from the strategy
            context = strategy.getContext();
            account = strategy.getAccount();
            indicators = strategy.getIndicators(); // initialize the indicators field

            LOGGER.info("=============Main============ Account ID: " + account.getAccountId());

        } catch (Exception e) {
            LOGGER.info("An error occurred while running the strategy");
            LOGGER.error("An error occurred while running the strategy", e);
        }
    }

    public IClient getClient() {
        return client;
    }

    public IEngine getEngine() {
        return engine;
    }

    public IContext getContext() {
        return context;
    }

    public IAccount getAccount() {
        return account;
    }

    public IIndicators getIndicators(){
        return indicators;
    }

    public void updateConfig(String userName, String password) {
        try {
            config.setUserName(userName);
            config.setPassword(password);
            start();
            startStrategy();
        } catch (Exception e) {
            LOGGER.error("Failed to update config: " + e.getMessage());
        }
    }

    private void setSystemListener() {
        client.setSystemListener(new ISystemListener() {
            @Override
            public void onStart(long processId) {
                LOGGER.info("Strategy started: " + processId);
            }

            @Override
            public void onStop(long processId) {
                LOGGER.info("Strategy stopped: " + processId);
            }

            @Override
            public void onConnect() {
                LOGGER.info("Connected");
                lightReconnects = 3;
            }

            @Override
            public void onDisconnect() {
                tryToReconnect();
            }
        });
    }

    private void tryToConnect() {
        LOGGER.info("Connecting with user " + config.getUserName());

        if (client == null) {
            LOGGER.debug("Client is null");
            throw new NullPointerException("Client is null");
        }

        try {
            client.connect(config.getJnlpUrl(), config.getUserName(), config.getPassword());
        } catch (JFException e) {
            if (e.getMessage().contains("Account is expired")) {
                LOGGER.info("Failed to connect to Dukascopy server: Account is expired");
                LOGGER.debug("Failed to connect to Dukascopy server: " + e.getMessage(), e);
                throw new RuntimeException("Failed to connect to Dukascopy server: Account is expired");
            }
            LOGGER.info("Failed to connect to Dukascopy server");
            LOGGER.debug("Failed to connect to Dukascopy server: " + e.getMessage(), e);
            throw new RuntimeException("Failed to connect to Dukascopy server", e);
        } catch (Exception e) {
            LOGGER.info("Error occurred while connecting to Dukascopy server");
            LOGGER.debug("Error occurred while connecting to Dukascopy server: " + e.getMessage(), e);
            e.printStackTrace();
        }

        // wait for it to connect
        int i = 10; // wait max ten seconds
        while (i > 0 && !client.isConnected()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                LOGGER.debug("Sleep was interrupted: " + e.getMessage(), e);
            }
            i--;
        }

        if (!client.isConnected()) {
            LOGGER.info("Failed to connect to Dukascopy server");
            throw new RuntimeException("Failed to connect to Dukascopy server");
        }
    }


    private void tryToReconnect() {
        Runnable runnable = () -> {
            if (lightReconnects > 0) {
                client.reconnect();
                --lightReconnects;
            } else {
                do {
                    try {
                        Thread.sleep(60 * 1000);
                    } catch (InterruptedException e) {
                        LOGGER.debug(e.getMessage(), e);
                    }
                    if (client.isConnected()) {
                        break;
                    }
                    try {
                        client.connect(config.getJnlpUrl(), config.getUserName(), config.getPassword());
                    } catch (Exception e) {
                        LOGGER.info("Error during reconnection");
                        LOGGER.debug("Exception during reconnection", e);
                        throw new RuntimeException(e);
                    }

                } while (!client.isConnected());
            }
        };
        new Thread(runnable).start();
    }

    private void subscribeToInstruments() {
        Set<Instrument> instruments = new HashSet<>();
        instruments.add(Instrument.EURUSD);
        LOGGER.info("Subscribing instruments...");
        client.setSubscribedInstruments(instruments);
    }


    private static class BarboneStrategy implements IStrategy {
        private IEngine engine;
        private IConsole console;
        private IContext context;
        private IAccount account;
        private IIndicators indicators;
        private CoreService coreService;

        public BarboneStrategy(CoreService coreService) {
            this.coreService = coreService; // set reference in constructor
        }

        @Override
        public void onStart(IContext context) throws JFException {
            this.engine = context.getEngine();
            this.console = context.getConsole();
            this.context = context;
            this.indicators = context.getIndicators();

            coreService.setContext(this.context);
            coreService.setEngine(this.engine);// Use the engine, console, context fields here
        }

        @Override
        public void onTick(Instrument instrument, ITick tick) throws JFException {
            //LOGGER.info("Got a new ticket...");
        }

        @Override
        public void onBar(Instrument instrument, Period period, IBar askBar, IBar bidBar) throws JFException {
        }

        @Override
        public void onMessage(IMessage message) throws JFException {
        }

        @Override
        public void onAccount(IAccount account) throws JFException {
            this.account = account;
            LOGGER.info("Account ID: " + account.getAccountId());
        }

        @Override
        public void onStop() throws JFException {
        }

        // Getter and setter methods for the private fields
        public IEngine getEngine() {
            return engine;
        }

        public void setEngine(IEngine engine) {
            this.engine = engine;
        }

        public IConsole getConsole() {
            return console;
        }

        public void setConsole(IConsole console) {
            this.console = console;
        }

        public IContext getContext() {
            return context;
        }

        public void setContext(IContext context) {
            this.context = context;
        }

        public IAccount getAccount() {
            return account;
        }

        public IIndicators getIndicators() {
            return indicators;
        }

        public void setAccount(IAccount account) {
            this.account = account;
        }
    }

}
