package com.orderflow.model;

import java.math.BigDecimal;
import java.util.Objects;

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

    @Override
    public boolean equals(Object other) {
        // 1. Se this e other forem a mesma instância, retorne true.
        if (this == other) {
            return true;
        }

        // 2. Se other for null ou tiver uma classe diferente, retorne false.
        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        // 3. Converta other para Order.
        Order otherOrder = (Order) other;

        // 4. Compare os IDs usando Objects.equals(...).
        return Objects.equals(this.id, otherOrder.id);
    }

    @Override
    public int hashCode() {
        // Retorne Objects.hashCode(...) passando o ID.
        return Objects.hashCode(id);
    }
}
