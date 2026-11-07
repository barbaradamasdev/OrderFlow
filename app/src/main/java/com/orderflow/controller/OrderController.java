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


@RestController 
@RequestMapping("/orders")
public class OrderController {
    private final OrderProcessor orderProcessor;

    public OrderController(OrderProcessor orderProcessor) {
        this.orderProcessor = orderProcessor;
    }

    @PostMapping("/process")
    public ResponseEntity<OrderProcessingResult> processOrders(
            @RequestBody @NotEmpty  List<@Valid CreateOrderRequest> requests) {

        List<Order> orders = requests.stream()
            .map(request -> new Order(
                request.id(), 
                request.customerCode(), 
                request.amount()))
            .toList();

        OrderProcessingResult result = orderProcessor.groupByCustomer(orders);
        return ResponseEntity.ok(result);
    }
    
}
