package com.orderflow.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.orderflow.model.Order;
import com.orderflow.model.OrderProcessingResult;

public class OrderProcessor {
    
    public OrderProcessingResult groupByCustomer(List<Order> orders) {
        Set<Long> seenOrderIds = new HashSet<>();
        Map<Long, List<Order>> ordersByCustomer = new HashMap<>();
        Map<Long, BigDecimal> totalsByCustomer = new HashMap<>();

        for (Order order : orders) {
            if (!seenOrderIds.add(order.getId())) continue;

            if(!ordersByCustomer.containsKey(order.getCustomerCode())) {
                ordersByCustomer.put(order.getCustomerCode(), new ArrayList<>());
            }

            ordersByCustomer.get(order.getCustomerCode()).add(order);

            BigDecimal valorTotal = totalsByCustomer.get(order.getCustomerCode());
            if (valorTotal == null) valorTotal = BigDecimal.ZERO;

            valorTotal = valorTotal.add(order.getAmount());
            totalsByCustomer.put(order.getCustomerCode(), valorTotal);
        }

        return new OrderProcessingResult(
            ordersByCustomer,
            totalsByCustomer
        );
    }
}
