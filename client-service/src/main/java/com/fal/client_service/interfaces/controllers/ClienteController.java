package com.fal.client_service.interfaces.controllers;


import com.fal.client_service.application.dto.ClienteDTO;
import com.fal.client_service.application.dto.CreateClienteRequest;
import com.fal.client_service.application.dto.UpdateClienteRequest;
import com.fal.client_service.domain.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<?> crearCliente(@Valid @RequestBody CreateClienteRequest request) {
        try {
            ClienteDTO nuevoCliente = clienteService.crearCliente(request);
            return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerClientePorId(@PathVariable Long id) {
        try {
            Optional<ClienteDTO> cliente = clienteService.obtenerClientePorId(id);
            return cliente.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error al obtener el cliente"));
        }
    }

    @GetMapping("/identificacion/{identificacion}")
    public ResponseEntity<?> obtenerClientePorIdentificacion(@PathVariable String identificacion) {
        try {
            Optional<ClienteDTO> cliente = clienteService.obtenerClientePorIdentificacion(identificacion);
            return cliente.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error al obtener el cliente"));
        }
    }

    @GetMapping("/cliente-id/{clienteId}")
    public ResponseEntity<?> obtenerClientePorClienteId(@PathVariable String clienteId) {
        try {
            Optional<ClienteDTO> cliente = clienteService.obtenerClientePorClienteId(clienteId);
            return cliente.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error al obtener el cliente"));
        }
    }

    @GetMapping
    public ResponseEntity<?> obtenerTodosLosClientes() {
        try {
            List<ClienteDTO> clientes = clienteService.obtenerTodosLosClientes();
            return ResponseEntity.ok(clientes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error al obtener los clientes"));
        }
    }

    @GetMapping("/activos")
    public ResponseEntity<?> obtenerClientesActivos() {
        try {
            List<ClienteDTO> clientesActivos = clienteService.obtenerClientesActivos();
            return ResponseEntity.ok(clientesActivos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error al obtener los clientes activos"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCliente(@PathVariable Long id, @Valid @RequestBody UpdateClienteRequest cliente) {
        try {
            ClienteDTO clienteActualizado = clienteService.actualizarCliente(id, cliente);
            return ResponseEntity.ok(clienteActualizado);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error al actualizar el cliente"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCliente(@PathVariable Long id) {
        try {
            clienteService.eliminarCliente(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error al eliminar el cliente"));
        }
    }

    @PatchMapping("/{id}/activar")
    public ResponseEntity<?> activarCliente(@PathVariable Long id) {
        try {
            ClienteDTO clienteActivado = clienteService.activarCliente(id);
            return ResponseEntity.ok(clienteActivado);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error al activar el cliente"));
        }
    }

    @GetMapping("/verificar-identificacion/{identificacion}")
    public ResponseEntity<?> verificarIdentificacionExistente(@PathVariable String identificacion) {
        try {
            boolean existe = clienteService.existeClientePorIdentificacion(identificacion);
            return ResponseEntity.ok(new VerificacionResponse(existe));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error al verificar la identificación"));
        }
    }

    private ErrorResponse createErrorResponse(String message) {
        return new ErrorResponse(message);
    }

    // Clases internas para respuestas JSON
    public static class ErrorResponse {
        private String error;
        private long timestamp;

        public ErrorResponse(String error) {
            this.error = error;
            this.timestamp = System.currentTimeMillis();
        }

        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    }

    public static class VerificacionResponse {
        private boolean existe;

        public VerificacionResponse(boolean existe) {
            this.existe = existe;
        }

        public boolean isExiste() { return existe; }
        public void setExiste(boolean existe) { this.existe = existe; }
    }
}
