package io.github.andersori.message.publisher.config;

import lombok.Getter;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Getter
    @Value("${spring.rabbitmq.queue.name}")
    private String queueName;
    @Getter
    @Value("${spring.rabbitmq.exchange.name}")
    private String exchangeName;
    @Getter
    @Value("${spring.rabbitmq.route-key.id}")
    private String routeKey;

    @Bean
    Queue queue(){
        return new Queue(queueName);
    }

    @Bean
    TopicExchange topicExchange(){
        return new TopicExchange(exchangeName);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(routeKey);
    }

}
