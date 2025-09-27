package com.fal.account_service.interfaces.controllers;


import com.fal.account_service.application.dto.*;
import com.fal.account_service.domain.service.MovimientoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/movimientos")
public class MovimientoController {

    private final MovimientoService movimientoService;

    @Autowired
    public MovimientoController(MovimientoService movimientoService) {
        this.movimientoService = movimientoService;
    }

    @PostMapping
    public ResponseEntity<?> crearMovimiento(@Valid @RequestBody CreateMovimientoRequest request) {
        try {

            request.validarMovimiento();

            MovimientoDTO movimiento = movimientoService.realizarMovimiento(request);
            return new ResponseEntity<>(movimiento, HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (RuntimeException e) {
            if ("Saldo no disponible".equals(e.getMessage())) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Saldo no disponible"));
            }
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error interno del servidor al crear el movimiento"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerMovimientoPorId(@PathVariable Long id) {
        try {
            MovimientoDTO movimiento = movimientoService.obtenerMovimientoPorId(id);
            return ResponseEntity.ok(movimiento);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al obtener el movimiento"));
        }
    }

    @GetMapping
    public ResponseEntity<?> obtenerTodosLosMovimientos() {
        try {
            List<MovimientoDTO> movimientos = movimientoService.obtenerTodosLosMovimientos();
            return ResponseEntity.ok(movimientos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al obtener los movimientos"));
        }
    }

    @GetMapping("/cuenta/{cuentaId}")
    public ResponseEntity<?> obtenerMovimientosPorCuenta(@PathVariable Long cuentaId) {
        try {
            List<MovimientoDTO> movimientos = movimientoService.obtenerMovimientosPorCuenta(cuentaId);
            return ResponseEntity.ok(movimientos);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrada")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al obtener los movimientos de la cuenta"));
        }
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<?> obtenerMovimientosPorCliente(@PathVariable String clienteId) {
        try {
            List<MovimientoDTO> movimientos = movimientoService.obtenerMovimientosPorCliente(clienteId);
            return ResponseEntity.ok(movimientos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al obtener los movimientos del cliente"));
        }
    }


    @GetMapping("/cliente/resumen/{clienteId}")
    public ResponseEntity<?> obtenerResumenMovimientos(@PathVariable String clienteId,
                                                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
                                                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        try {
            MovimientoResumenResponse resumen = movimientoService.obtenerResumenMovimientos(clienteId, fechaInicio, fechaFin);
            return ResponseEntity.ok(resumen);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al obtener el resumen de movimientos"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarMovimiento(@PathVariable Long id,
                                                  @Valid @RequestBody UpdateMovimientoRequest request) {
        try {
            MovimientoDTO movimientoActualizado = movimientoService.actualizarMovimiento(id, request);
            return ResponseEntity.ok(movimientoActualizado);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al actualizar el movimiento"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarMovimiento(@PathVariable Long id) {
        try {
            movimientoService.eliminarMovimiento(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al eliminar el movimiento"));
        }
    }

    @PostMapping("/deposito")
    public ResponseEntity<?> realizarDeposito(@Valid @RequestBody DepositoRequest request) {
        try {
            MovimientoDTO movimiento = movimientoService.realizarDeposito(
                    request.getCuentaId(), request.getMonto(), request.getDescripcion());
            return new ResponseEntity<>(movimiento, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrada")) {
                return ResponseEntity.notFound().build();
            }
            if ("Saldo no disponible".equals(e.getMessage())) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Saldo no disponible"));
            }
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al realizar el depósito"));
        }
    }

    @PostMapping("/retiro")
    public ResponseEntity<?> realizarRetiro(@Valid @RequestBody RetiroRequest request) {
        try {
            MovimientoDTO movimiento = movimientoService.realizarRetiro(
                    request.getCuentaId(), request.getMonto(), request.getDescripcion());
            return new ResponseEntity<>(movimiento, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrada")) {
                return ResponseEntity.notFound().build();
            }
            if ("Saldo no disponible".equals(e.getMessage())) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Saldo no disponible"));
            }
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al realizar el retiro"));
        }
    }



}
