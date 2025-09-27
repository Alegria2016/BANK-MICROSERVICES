package com.fal.account_service.application.mapper;

import com.fal.account_service.domain.events.ClienteCreadoEvent;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EventoMapper {

    private static final Logger logger = LoggerFactory.getLogger(EventoMapper.class);
    private final ObjectMapper objectMapper;

    public EventoMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ClienteCreadoEvent toClienteCreadoEvent(String jsonMessage) {
        try {
            return objectMapper.readValue(jsonMessage, ClienteCreadoEvent.class);
        } catch (JsonProcessingException e) {
            logger.error("Error al deserializar mensaje JSON a ClienteCreadoEvent: {}", e.getMessage());
            throw new IllegalArgumentException("Mensaje JSON inválido para ClienteCreadoEvent", e);
        }
    }

    public String toJson(ClienteCreadoEvent event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            logger.error("Error al serializar ClienteCreadoEvent a JSON: {}", e.getMessage());
            throw new RuntimeException("Error al serializar evento", e);
        }
    }

    public ClienteCreadoEvent createFromData(String clienteId, String nombre, String identificacion,
                                             String genero, Integer edad, String direccion, String telefono) {
        return ClienteCreadoEvent.builder()
                .clienteId(clienteId)
                .nombre(nombre)
                .identificacion(identificacion)
                .genero(genero)
                .edad(edad)
                .direccion(direccion)
                .telefono(telefono)
                .build();
    }
}
