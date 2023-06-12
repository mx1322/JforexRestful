package com.JforexRestful.restful.Services;

import com.dukascopy.api.IEngine;
import com.dukascopy.api.IOrder;
import com.dukascopy.api.Instrument;
import com.dukascopy.api.JFException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private CoreService coreService;

    public OrderService(CoreService coreService) {
        this.coreService = coreService;
    }

    public List<Order> getOpenOrders() {
        IEngine engine = null;

        try {
            engine = coreService.getEngine();
            if (engine == null) {
                System.out.println("CoreService returned null engine. Please check the CoreService implementation.");
                return null;
            }
            List<IOrder> orders = engine.getOrders();
            return orders.stream()
                    .filter(order -> order.getState() == IOrder.State.FILLED)
                    .map(this::fromIOrder)
                    .collect(Collectors.toList());
        } catch (NullPointerException | JFException e) {
            System.out.println("Exception occurred while getting open orders. Please check the CoreService implementation.");
            return null;
        }
    }
    public void openOrder(String label, Instrument instrument, IEngine.OrderCommand command, double amount, double price, double slippage) throws JFException {
        coreService.getEngine().submitOrder(label, instrument, command, amount, price, slippage);
    }

    public void closeOrder(String orderId) throws JFException {
        for (IOrder order : coreService.getEngine().getOrders()) {
            if (order.getId().equals(orderId)) {
                order.close();
                break;
            }
        }
    }

    public void mergeOrders(String label, List<String> orderIds) throws JFException {
        List<IOrder> ordersToMerge = new ArrayList<>();
        for (IOrder order : coreService.getEngine().getOrders()) {
            if (orderIds.contains(order.getId())) {
                ordersToMerge.add(order);
            }
        }
        coreService.getEngine().mergeOrders(label, ordersToMerge.toArray(new IOrder[0]));
    }

    private Order fromIOrder(IOrder iOrder) {
        Order order = new Order();
        order.setId(iOrder.getId());
        order.setLabel(iOrder.getLabel());
        order.setInstrument(iOrder.getInstrument().toString());
        order.setAmount(iOrder.getAmount());
        order.setOrderCommand(iOrder.getOrderCommand().toString());
        // set other order properties as needed
        return order;
    }

    public static class Order {
        private String id;
        private String label;
        private String instrument;
        private double amount;
        private String orderCommand;
        // add other properties as needed

        // Your getters and setters go here

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

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

        public String getInstrument() {
            return instrument;
        }

        public void setInstrument(String instrument) {
            this.instrument = instrument;
        }

        public String getOrderCommand() {
            return orderCommand;
        }

        public void setOrderCommand(String orderCommand) {
            this.orderCommand = orderCommand;
        }
        // add other getter and setter methods as needed
    }
}



