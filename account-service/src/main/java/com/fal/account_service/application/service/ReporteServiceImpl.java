package com.fal.account_service.application.service;

import com.fal.account_service.application.dto.CuentaReporteDTO;
import com.fal.account_service.application.dto.EstadoCuentaResponse;
import com.fal.account_service.application.dto.MovimientoReporteDTO;
import com.fal.account_service.application.dto.ReporteDTO;
import com.fal.account_service.domain.model.Cuenta;
import com.fal.account_service.domain.model.Movimiento;
import com.fal.account_service.domain.repository.CuentaRepository;
import com.fal.account_service.domain.repository.MovimientoRepository;
import com.fal.account_service.domain.service.ReporteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ReporteServiceImpl implements ReporteService {

    private final CuentaRepository cuentaRepository;
    private final MovimientoRepository movimientoRepository;

    public ReporteServiceImpl(CuentaRepository cuentaRepository, MovimientoRepository movimientoRepository) {
        this.cuentaRepository = cuentaRepository;
        this.movimientoRepository = movimientoRepository;
    }

    @Override
    public ReporteDTO generarReporteEstadoCuenta(String clienteId, LocalDate fechaInicio, LocalDate fechaFin) {
        validarParametrosReporte(clienteId, fechaInicio, fechaFin);

        List<Cuenta> cuentas = cuentaRepository.findByClienteIdAndEstadoTrue(clienteId);

        if (cuentas.isEmpty()) {
            throw new RuntimeException("El cliente no tiene cuentas activas");
        }

        ReporteDTO reporte = new ReporteDTO();
        reporte.setFechaInicio(fechaInicio);
        reporte.setFechaFin(fechaFin);
        reporte.setClienteId(clienteId);

        reporte.setNombreCliente("Cliente " + clienteId); // Esto se debería obtener del servicio de clientes
        reporte.setIdentificacionCliente("ID-" + clienteId);

        List<CuentaReporteDTO> cuentasReporte = cuentas.stream()
                .map(cuenta -> mapearCuentaAReporte(cuenta, fechaInicio, fechaFin))
                .collect(Collectors.toList());

        reporte.setCuentas(cuentasReporte);
        reporte.calcularResumen();

        return reporte;
    }


    private void validarParametrosReporte(String clienteId, LocalDate fechaInicio, LocalDate fechaFin) {
        if (clienteId == null || clienteId.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del cliente es requerido");
        }
        validarParametrosFechas(fechaInicio, fechaFin);
    }

    private void validarParametrosFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null) {
            throw new IllegalArgumentException("La fecha de inicio es requerida");
        }
        if (fechaFin == null) {
            throw new IllegalArgumentException("La fecha de fin es requerida");
        }
        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }

        if (fechaInicio.plusYears(1).isBefore(fechaFin)) {
            throw new IllegalArgumentException("El rango de fechas no puede exceder 1 año");
        }
    }



    private CuentaReporteDTO mapearCuentaAReporte(Cuenta cuenta, LocalDate fechaInicio, LocalDate fechaFin) {
        CuentaReporteDTO cuentaReporte = new CuentaReporteDTO();
        cuentaReporte.setCuentaId(cuenta.getId());
        cuentaReporte.setNumeroCuenta(cuenta.getNumeroCuenta());
        cuentaReporte.setTipoCuenta(cuenta.getTipoCuenta());
        cuentaReporte.setSaldoInicial(cuenta.getSaldoInicial());
        cuentaReporte.setSaldoActual(cuenta.getSaldoActual());
        cuentaReporte.setEstado(cuenta.getEstado());


        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.atTime(LocalTime.MAX);

        List<Movimiento> movimientos = movimientoRepository.findByCuentaIdAndFechaBetween(
                cuenta.getId(), inicio, fin);

        List<MovimientoReporteDTO> movimientosReporte = movimientos.stream()
                .map(this::mapearMovimientoAReporte)
                .collect(Collectors.toList());

        cuentaReporte.setMovimientos(movimientosReporte);
        cuentaReporte.calcularResumenCuenta();

        return cuentaReporte;
    }


    private MovimientoReporteDTO mapearMovimientoAReporte(Movimiento movimiento) {
        MovimientoReporteDTO movimientoReporte = new MovimientoReporteDTO();
        movimientoReporte.setMovimientoId(movimiento.getId());
        movimientoReporte.setFecha(movimiento.getFecha());
        movimientoReporte.setTipoMovimiento(movimiento.getTipoMovimiento());
        movimientoReporte.setValor(movimiento.getValor());
        movimientoReporte.setSaldo(movimiento.getSaldo());
        movimientoReporte.setDescripcion(movimiento.getDescripcion());
        movimientoReporte.setReferencia("MOV-" + String.format("%06d", movimiento.getId()));

        return movimientoReporte;
    }
}
