package com.orderflow.model;

import java.math.BigDecimal;

public class Order {
    private final Long id;
    private final Long customerCode;
    private final BigDecimal amount;

    public Order(Long id, Long customerCode, BigDecimal amount) {
        this.id = id;
        this.customerCode = customerCode;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public Long getCustomerCode() {
        return customerCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
