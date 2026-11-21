
package com.orderflow.kafka.consumer;

import com.orderflow.kafka.event.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    private static final Logger log =
            LoggerFactory.getLogger(OrderEventListener.class);

    @KafkaListener(
            topics = "${orderflow.kafka.topic.orders-created}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(OrderCreatedEvent event) {
        log.info(
                "Event received: eventId={} orderId={} customerCode={} amount={}",
                event.eventId(),
                event.orderId(),
                event.customerCode(),
                event.amount()
        );
    }
}
