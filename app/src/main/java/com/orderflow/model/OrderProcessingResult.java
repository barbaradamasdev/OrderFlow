package com.orderflow.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class OrderProcessingResult {
    private final Map<Long, List<Order>> ordersByCustomer;
    private final Map<Long, BigDecimal> totalsByCustomer;

    public OrderProcessingResult(
            Map<Long, List<Order>> ordersByCustomer,
            Map<Long, BigDecimal> totalsByCustomer) {
        this.ordersByCustomer = ordersByCustomer;
        this.totalsByCustomer = totalsByCustomer;
    }

    public Map<Long, List<Order>> getOrdersByCustomer() {
        return ordersByCustomer;
    }

    public Map<Long, BigDecimal> getTotalsByCustomer() {
        return totalsByCustomer;
    }
}