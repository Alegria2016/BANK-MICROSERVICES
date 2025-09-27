package com.fal.account_service.domain.service;

import com.fal.account_service.application.dto.EstadoCuentaResponse;
import com.fal.account_service.application.dto.ReporteDTO;
import com.fal.account_service.domain.model.Movimiento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ReporteService {

    ReporteDTO generarReporteEstadoCuenta(String clienteId, LocalDate fechaInicio, LocalDate fechaFin);

}
