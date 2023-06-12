package com.JforexRestful.restful.Controllers;

import com.JforexRestful.restful.Services.OrderService;
import com.dukascopy.api.JFException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/open")
    public List<OrderService.Order> getOpenOrders() throws JFException {
        return orderService.getOpenOrders();
    }
}
