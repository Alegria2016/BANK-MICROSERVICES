package com.fal.account_service.domain.service;

import com.fal.account_service.application.dto.*;
import com.fal.account_service.domain.model.Movimiento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface MovimientoService {

    MovimientoDTO realizarMovimiento(CreateMovimientoRequest request);
    MovimientoDTO obtenerMovimientoPorId(Long id);
    List<MovimientoDTO> obtenerTodosLosMovimientos();
    List<MovimientoDTO> obtenerMovimientosPorCuenta(Long cuentaId);
    List<MovimientoDTO> obtenerMovimientosPorCliente(String clienteId);
    MovimientoResumenResponse obtenerResumenMovimientos(String clienteId, LocalDate fechaInicio, LocalDate fechaFin);
    MovimientoDTO actualizarMovimiento(Long id, UpdateMovimientoRequest request);
    void eliminarMovimiento(Long id);
    MovimientoDTO realizarDeposito(Long cuentaId, BigDecimal monto, String descripcion);
    MovimientoDTO realizarRetiro(Long cuentaId, BigDecimal monto, String descripcion);


}
