package com.orderflow.service;

import com.orderflow.model.Order;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderProcessorTest {

    @Test
    void shouldGroupOrdersByCustomerAndIgnoreDuplicateIds() {
        // Arrange: preparar os dados.
        List<Order> orders = List.of(
            new Order(101L, 10L, new BigDecimal("50.00")),
            new Order(102L, 20L, new BigDecimal("80.00")),
            new Order(103L, 10L, new BigDecimal("30.00")),
            new Order(101L, 10L, new BigDecimal("50.00")),
            new Order(104L, 20L, new BigDecimal("20.00"))
        );

        OrderProcessor processor = new OrderProcessor();

        // Act: executar o comportamento.
        Map<Long, List<Order>> result =
                processor.groupByCustomer(orders);

        // Assert: conferir os grupos e seus pedidos.
        assertEquals(2, result.size());

        assertEquals(
            List.of(101L, 103L),
            result.get(10L).stream().map(Order::getId).toList()
        );

        assertEquals(
            List.of(102L, 104L),
            result.get(20L).stream().map(Order::getId).toList()
        );
    }
}