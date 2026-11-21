package com.orderflow.kafka.event;

import java.math.BigDecimal;

public record OrderCreatedEvent(
    Long eventId,
    Long orderId,
    Long customerCode,
    BigDecimal amount
) {}