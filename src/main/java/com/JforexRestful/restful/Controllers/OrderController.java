package com.JforexRestful.restful.Controllers;

import com.JforexRestful.restful.Services.CoreService;
import com.JforexRestful.restful.Services.OrderService;
import com.dukascopy.api.JFException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CoreService.class);
    @Autowired
    private OrderService orderService;

    @GetMapping("/open")
    public List<OrderService.Order> getOpenOrders() throws JFException {
        orderService.toString();
        LOGGER.info("Client got order list with: "+orderService.getOpenOrders());
        return orderService.getOpenOrders();
    }
}
