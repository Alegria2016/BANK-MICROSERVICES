package com.fal.account_service.application.events;

import com.fal.account_service.application.dto.CreateCuentaRequest;
import com.fal.account_service.domain.events.ClienteCreadoEvent;
import com.fal.account_service.domain.service.CuentaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ClienteEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(ClienteEventConsumer.class);

    @Autowired
    private CuentaService cuentaService;

    @RabbitListener(queues = "${app.rabbitmq.queue.cliente-creado:cliente.creado.queue}")
    public void handleClienteCreado(ClienteCreadoEvent event) {
        try {
            logger.info("📩 Evento ClienteCreado recibido: {} - {}",
                    event.getClienteId(), event.getNombre());

            // Validar evento
            if (event.getClienteId() == null || event.getClienteId().trim().isEmpty()) {
                logger.error("❌ Evento inválido: clienteId es nulo o vacío");
                return;
            }

            // Crear cuenta por defecto
            crearCuentaPorDefecto(event);

            logger.info("✅ Cuenta por defecto creada para cliente: {}", event.getClienteId());

        } catch (Exception e) {
            logger.error("❌ Error procesando evento ClienteCreado: {}", e.getMessage(), e);
            // No relanzar para evitar reintentos infinitos
        }
    }

    private void crearCuentaPorDefecto(ClienteCreadoEvent event) {
        try {
            // Lógica para crear cuenta por defecto
            String numeroCuenta = generarNumeroCuenta(event.getClienteId());
            CreateCuentaRequest request = new CreateCuentaRequest();
            request.setClienteId(event.getClienteId());
            request.setNumeroCuenta(numeroCuenta);
            request.setTipoCuenta("AHORROS");
            request.setSaldoInicial(new BigDecimal("2000.00"));
            cuentaService.crearCuenta(request);

            // Usar cuentaService para crear la cuenta
            // cuentaService.crearCuentaPorDefecto(event.getClienteId(), numeroCuenta);

            logger.info("🏦 Cuenta {} creada para cliente: {}", numeroCuenta, event.getClienteId());

        } catch (Exception e) {
            logger.error("❌ Error creando cuenta por defecto: {}", e.getMessage());
            throw e; // Relanzar para que RabbitMQ maneje el reintento
        }
    }

    private String generarNumeroCuenta(String clienteId) {
        return "AH" + clienteId.substring(Math.max(0, clienteId.length() - 6)) +
                System.currentTimeMillis() % 10000;
    }
}