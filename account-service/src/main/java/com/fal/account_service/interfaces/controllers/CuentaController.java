package com.fal.account_service.interfaces.controllers;

import com.fal.account_service.application.dto.*;
import com.fal.account_service.domain.service.CuentaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cuentas")
public class CuentaController {

    private final CuentaService cuentaService;

    @Autowired
    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @PostMapping
    public ResponseEntity<?> crearCuenta(@Valid @RequestBody CreateCuentaRequest request) {
        try {
            CuentaDTO nuevaCuenta = cuentaService.crearCuenta(request);
            return new ResponseEntity<>(nuevaCuenta, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerCuentaPorId(@PathVariable Long id) {
        try {
            Optional<CuentaDTO> cuenta = cuentaService.obtenerCuentaPorId(id);
            return cuenta.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al obtener la cuenta"));
        }
    }

    @GetMapping("/numero/{numeroCuenta}")
    public ResponseEntity<?> obtenerCuentaPorNumero(@PathVariable String numeroCuenta) {
        try {
            Optional<CuentaDTO> cuenta = cuentaService.obtenerCuentaPorNumero(numeroCuenta);
            return cuenta.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al obtener la cuenta"));
        }
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<?> obtenerCuentasPorCliente(@PathVariable String clienteId) {
        try {
            List<CuentaDTO> cuentas = cuentaService.obtenerCuentasPorCliente(clienteId);
            return ResponseEntity.ok(cuentas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al obtener las cuentas"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCuenta(@PathVariable Long id,
                                              @Valid @RequestBody UpdateCuentaRequest request) {
        try {
            CuentaDTO cuentaActualizada = cuentaService.actualizarCuenta(id, request);
            return ResponseEntity.ok(cuentaActualizada);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrada")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PatchMapping("/desactivar/{id}")
    public ResponseEntity<?> desactivarCuenta(@PathVariable Long id) {
        try {
            CuentaDTO cuentaDesactivada = cuentaService.desactivarCuenta(id);
            return ResponseEntity.ok(cuentaDesactivada);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrada")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCuenta(@PathVariable Long id) {
        try {
            cuentaService.eliminarCuenta(id);
            return ResponseEntity.ok("Cuenta eliminada satisfactoriamente.");
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Fallo la eliminacion de la cuenta.")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }


}
