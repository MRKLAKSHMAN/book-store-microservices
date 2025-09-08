package com.lakshman.bookstore.order;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RabbitMQConfig {
    private final ApplicationProperties properties;


    RabbitMQConfig(ApplicationProperties properties) {
        this.properties = properties;
    }

    @Bean
    DirectExchange exchange(){
        return new DirectExchange(properties.orderEventsExchange());
    }

    @Bean
    Queue newOrdersQueue(){
        return QueueBuilder.durable(properties.newOrderQueue()).build();
    }

    @Bean
    Binding newOrdersQueueBinding(){
        //Binding Queue with exchange
        return BindingBuilder.bind(newOrdersQueue())
                .to(exchange())
                .with("new-orders-key");//We can also keep it as .with(properties.newOrderQueue());

    }

    @Bean
    Queue deliveredOrdersQueue(){
        return QueueBuilder.durable(properties.deliveredOrdersQueue()).build();
    }
}
