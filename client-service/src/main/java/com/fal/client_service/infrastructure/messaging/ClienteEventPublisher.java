package com.fal.client_service.infrastructure.messaging;
import com.fal.client_service.domain.service.events.ClienteCreadoEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ClienteEventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(ClienteEventPublisher.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.exchange.cliente-events:cliente.events}")
    private String exchangeName;

    @Value("${app.rabbitmq.routing-key.cliente-creado:cliente.creado}")
    private String routingKey;

    public void publicarClienteCreado(ClienteCreadoEvent event) {
        try {
            rabbitTemplate.convertAndSend(exchangeName, routingKey, event);
            logger.info("✅ Evento ClienteCreado publicado: {} - {}",
                    event.getClienteId(), event.getNombre());
        } catch (Exception e) {
            logger.error("❌ Error publicando evento ClienteCreado: {}", e.getMessage());
            // No lanzar excepción para no afectar la creación del cliente
        }
    }
}