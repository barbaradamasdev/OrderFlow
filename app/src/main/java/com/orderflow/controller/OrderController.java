package com.orderflow.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orderflow.dto.CreateOrderRequest;
import com.orderflow.model.Order;
import com.orderflow.model.OrderProcessingResult;
import com.orderflow.service.OrderProcessor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController 
@RequestMapping("/orders")
public class OrderController {
    private final OrderProcessor orderProcessor;
    private static final Logger log =
        LoggerFactory.getLogger(OrderController.class);

    public OrderController(OrderProcessor orderProcessor) {
        this.orderProcessor = orderProcessor;
    }

    @PostMapping("/process")
    public ResponseEntity<OrderProcessingResult> processOrders(
            @RequestBody @NotEmpty List<@Valid CreateOrderRequest> requests) {

        log.info("Order processing request received. orderCount={}",
                requests.size());

        List<Order> orders = requests.stream()
                .map(request -> new Order(
                        request.id(),
                        request.customerCode(),
                        request.amount()))
                .toList();

        OrderProcessingResult result =
                orderProcessor.groupByCustomer(orders);

        log.info("Order processing completed. customerCount={}",
                result.getOrdersByCustomer().size());

        return ResponseEntity.ok(result);
    }
}
