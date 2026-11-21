
package com.orderflow.controller;

import com.orderflow.dto.CreateOrderRequest;
import com.orderflow.kafka.event.OrderCreatedEvent;
import com.orderflow.kafka.producer.OrderEventProducer;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/orders/events")
public class OrderEventController {

    private final OrderEventProducer producer;
    private final AtomicLong eventSequence = new AtomicLong();

    public OrderEventController(OrderEventProducer producer) {
        this.producer = producer;
    }

    @PostMapping
    public ResponseEntity<Void> publish(
            @Valid @RequestBody CreateOrderRequest request) {

        OrderCreatedEvent event = new OrderCreatedEvent(
                eventSequence.incrementAndGet(),
                request.id(),
                request.customerCode(),
                request.amount()
        );

        producer.publish(event);

        return ResponseEntity.accepted().build();
    }
}
