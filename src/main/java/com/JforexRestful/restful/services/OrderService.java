package com.JforexRestful.restful.services;

import com.dukascopy.api.IEngine;
import com.dukascopy.api.IOrder;
import com.dukascopy.api.JFException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private CoreService coreService;

    public OrderService(CoreService coreService) {
        this.coreService = coreService;
    }

    public List<Order> getOpenOrders() throws JFException {
        IEngine engine = coreService.getEngine();
        if (engine == null) {
            return null;
        }

        List<IOrder> orders = engine.getOrders();
        return orders.stream()
                .filter(order -> order.getState() == IOrder.State.FILLED)
                .map(this::fromIOrder)
                .collect(Collectors.toList());
    }

    private Order fromIOrder(IOrder iOrder) {
        Order order = new Order();
        order.setLabel(iOrder.getLabel());
        order.setAmount(iOrder.getAmount());
        // set other order properties as needed
        return order;
    }

    public static class Order {
        private String label;
        private double amount;
        // add other properties as needed

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }
        // add other getter and setter methods as needed
    }
}

