package com.fal.client_service.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${app.rabbitmq.exchange.cliente-events:cliente.events}")
    private String exchangeName;

    @Value("${app.rabbitmq.queue.cliente-creado:cliente.creado.queue}")
    private String queueName;

    @Value("${app.rabbitmq.routing-key.cliente-creado:cliente.creado}")
    private String routingKey;

    // Exchange para eventos de cliente
    @Bean
    public TopicExchange clienteEventsExchange() {
        return new TopicExchange(exchangeName);
    }

    // Queue para eventos de creación de cliente
    @Bean
    public Queue clienteCreadoQueue() {
        return new Queue(queueName, true, false, false);
    }

    // Binding entre exchange y queue
    @Bean
    public Binding clienteCreadoBinding() {
        return BindingBuilder.bind(clienteCreadoQueue())
                .to(clienteEventsExchange())
                .with(routingKey);
    }

    // Configuración para JSON
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // RabbitTemplate configurado
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                System.out.println("✅ Mensaje confirmado por broker");
            } else {
                System.out.println("❌ Mensaje NO confirmado: " + cause);
            }
        });
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnsCallback(returned -> {
            System.out.println("❌ Mensaje devuelto: " + returned.getReplyText());
        });
        return rabbitTemplate;
    }
}