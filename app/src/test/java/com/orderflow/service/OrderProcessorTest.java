package com.orderflow.service;

import com.orderflow.model.Order;
import com.orderflow.model.OrderProcessingResult;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

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
        OrderProcessingResult result =
                processor.groupByCustomer(orders);

        // Assert: conferir os grupos e seus pedidos.
        assertEquals(2, result.getOrdersByCustomer().size());

        assertEquals(
            new BigDecimal("80.00"),
            result.getTotalsByCustomer().get(10L)
        );

        assertEquals(
            new BigDecimal("100.00"),
            result.getTotalsByCustomer().get(20L)
        );

        assertEquals(
            2,
            result.getOrdersByCustomer().get(10L).size()
        );

        assertEquals(
            2,
            result.getOrdersByCustomer().get(20L).size()
        );

        List<Long> customer10OrderIds = result.getOrdersByCustomer()
                .get(10L)
                .stream()
                .map(Order::getId)
                .toList();

        assertEquals(
            List.of(101L, 103L),
            customer10OrderIds
        );
    }

    @Test
    void shouldIgnoreOrdersWithDuplicateIds() {
        // Arrange
        List<Order> orders = List.of(
            new Order(101L, 10L, new BigDecimal("50.00")),
            new Order(101L, 10L, new BigDecimal("50.00")),
            new Order(102L, 10L, new BigDecimal("30.00"))
        );

        OrderProcessor processor = new OrderProcessor();

        // Act
        OrderProcessingResult result =
                processor.groupByCustomer(orders);

        // Assert
        List<Order> customerOrders =
                result.getOrdersByCustomer().get(10L);

        assertEquals(2, customerOrders.size());

        assertEquals(
            new BigDecimal("80.00"),
            result.getTotalsByCustomer().get(10L)
        );
    }

    @Test
    void shouldIgnoreDuplicateIdEvenWithDifferentCustomer() {
        // Arrange
        List<Order> orders = List.of(
            new Order(101L, 10L, new BigDecimal("50.00")),
            new Order(101L, 20L, new BigDecimal("90.00")),
            new Order(102L, 20L, new BigDecimal("30.00"))
        );

        OrderProcessor processor = new OrderProcessor();

        // Act
        OrderProcessingResult result =
                processor.groupByCustomer(orders);

        // Assert
        assertEquals(
            new BigDecimal("50.00"),
            result.getTotalsByCustomer().get(10L)
        );

        assertEquals(
            new BigDecimal("30.00"),
            result.getTotalsByCustomer().get(20L)
        );

        assertEquals(
            1,
            result.getOrdersByCustomer().get(10L).size()
        );

        assertEquals(
            1,
            result.getOrdersByCustomer().get(20L).size()
        );
    }

    @Test
    void shouldGroupOrdersByCustomerAndIgnoreDuplicateIdsWithStreams() {
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
        OrderProcessingResult result =
                processor.groupByCustomerWithStreams(orders);

        // Assert: conferir os grupos e seus pedidos.
        assertEquals(2, result.getOrdersByCustomer().size());

        assertEquals(
            new BigDecimal("80.00"),
            result.getTotalsByCustomer().get(10L)
        );

        assertEquals(
            new BigDecimal("100.00"),
            result.getTotalsByCustomer().get(20L)
        );

        assertEquals(
            2,
            result.getOrdersByCustomer().get(10L).size()
        );

        assertEquals(
            2,
            result.getOrdersByCustomer().get(20L).size()
        );

        List<Long> customer10OrderIds = result.getOrdersByCustomer()
                .get(10L)
                .stream()
                .map(Order::getId)
                .toList();

        assertEquals(
            List.of(101L, 103L),
            customer10OrderIds
        );
    }

    @Test
    void shouldIgnoreOrdersWithDuplicateIdsWithStreams() {
        // Arrange
        List<Order> orders = List.of(
            new Order(101L, 10L, new BigDecimal("50.00")),
            new Order(101L, 10L, new BigDecimal("50.00")),
            new Order(102L, 10L, new BigDecimal("30.00"))
        );

        OrderProcessor processor = new OrderProcessor();

        // Act
        OrderProcessingResult result =
                processor.groupByCustomerWithStreams(orders);

        // Assert
        List<Order> customerOrders =
                result.getOrdersByCustomer().get(10L);

        assertEquals(2, customerOrders.size());

        assertEquals(
            new BigDecimal("80.00"),
            result.getTotalsByCustomer().get(10L)
        );
    }

    @Test
    void shouldIgnoreDuplicateIdEvenWithDifferentCustomerWithStreams() {
        // Arrange
        List<Order> orders = List.of(
            new Order(101L, 10L, new BigDecimal("50.00")),
            new Order(101L, 20L, new BigDecimal("90.00")),
            new Order(102L, 20L, new BigDecimal("30.00"))
        );

        OrderProcessor processor = new OrderProcessor();

        // Act
        OrderProcessingResult result =
                processor.groupByCustomerWithStreams(orders);

        // Assert
        assertEquals(
            new BigDecimal("50.00"),
            result.getTotalsByCustomer().get(10L)
        );

        assertEquals(
            new BigDecimal("30.00"),
            result.getTotalsByCustomer().get(20L)
        );

        assertEquals(
            1,
            result.getOrdersByCustomer().get(10L).size()
        );

        assertEquals(
            1,
            result.getOrdersByCustomer().get(20L).size()
        );
    }
}