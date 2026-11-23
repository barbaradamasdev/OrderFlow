package com.orderflow.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.orderflow.model.Order;
import com.orderflow.model.OrderProcessingResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service 
public class OrderProcessor {
    private static final Logger log =
        LoggerFactory.getLogger(OrderProcessor.class);
    
    public OrderProcessingResult groupByCustomer(List<Order> orders) {

        log.info("Starting order grouping. receivedCount={}",
                orders.size());

        Set<Long> seenOrderIds = new HashSet<>();
        Map<Long, List<Order>> ordersByCustomer = new HashMap<>();
        Map<Long, BigDecimal> totalsByCustomer = new HashMap<>();

        int duplicates = 0;

        for (Order order : orders) {

            if (!seenOrderIds.add(order.getId())) {
                duplicates++;

                log.debug("Duplicate order ignored. orderId={}",
                        order.getId());

                continue;
            }

            ordersByCustomer
                    .computeIfAbsent(
                            order.getCustomerCode(),
                            key -> new ArrayList<>()
                    )
                    .add(order);

            totalsByCustomer.merge(
                    order.getCustomerCode(),
                    order.getAmount(),
                    BigDecimal::add
            );
        }

        log.info(
                "Order grouping completed. uniqueOrders={} duplicates={} customers={}",
                seenOrderIds.size(),
                duplicates,
                ordersByCustomer.size()
        );

        return new OrderProcessingResult(
                ordersByCustomer,
                totalsByCustomer
        );
    }

    public OrderProcessingResult groupByCustomerWithStreams(List<Order> orders) {
        List<Order> uniqueOrders = orders.stream()
            .distinct()
            .toList();

        Map<Long, List<Order>> ordersByCustomer = uniqueOrders.stream()
            .collect(Collectors.groupingBy(Order::getCustomerCode));

        Map<Long, BigDecimal> totalsByCustomer = uniqueOrders.stream()
            .collect(Collectors.toMap(
                Order::getCustomerCode,
                Order::getAmount,
                BigDecimal::add
            ));

        return new OrderProcessingResult(
            ordersByCustomer,
            totalsByCustomer
        );
    }
}
