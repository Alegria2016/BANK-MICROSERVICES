package com.fal.account_service.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ReporteDTO {

    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String clienteId;
    private String nombreCliente;
    private String identificacionCliente;
    private LocalDateTime fechaGeneracion;
    private List<CuentaReporteDTO> cuentas;
    private ResumenReporteDTO resumen;

    // Constructores
    public ReporteDTO() {
        this.fechaGeneracion = LocalDateTime.now();
    }

    public ReporteDTO(LocalDate fechaInicio, LocalDate fechaFin, String clienteId,
                      String nombreCliente, String identificacionCliente) {
        this();
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.clienteId = clienteId;
        this.nombreCliente = nombreCliente;
        this.identificacionCliente = identificacionCliente;
    }

    // Getters y Setters
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public String getClienteId() { return clienteId; }
    public void setClienteId(String clienteId) { this.clienteId = clienteId; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getIdentificacionCliente() { return identificacionCliente; }
    public void setIdentificacionCliente(String identificacionCliente) { this.identificacionCliente = identificacionCliente; }

    public LocalDateTime getFechaGeneracion() { return fechaGeneracion; }
    public void setFechaGeneracion(LocalDateTime fechaGeneracion) { this.fechaGeneracion = fechaGeneracion; }

    public List<CuentaReporteDTO> getCuentas() { return cuentas; }
    public void setCuentas(List<CuentaReporteDTO> cuentas) { this.cuentas = cuentas; }

    public ResumenReporteDTO getResumen() { return resumen; }
    public void setResumen(ResumenReporteDTO resumen) { this.resumen = resumen; }

    // Métodos utilitarios
    public void calcularResumen() {
        if (cuentas == null || cuentas.isEmpty()) {
            this.resumen = new ResumenReporteDTO();
            return;
        }

        BigDecimal totalDepositos = BigDecimal.ZERO;
        BigDecimal totalRetiros = BigDecimal.ZERO;
        BigDecimal saldoInicialTotal = BigDecimal.ZERO;
        BigDecimal saldoFinalTotal = BigDecimal.ZERO;
        int totalMovimientos = 0;

        for (CuentaReporteDTO cuenta : cuentas) {
            if (cuenta.getResumenCuenta() != null) {
                totalDepositos = totalDepositos.add(cuenta.getResumenCuenta().getTotalDepositos());
                totalRetiros = totalRetiros.add(cuenta.getResumenCuenta().getTotalRetiros());
                saldoInicialTotal = saldoInicialTotal.add(cuenta.getResumenCuenta().getSaldoInicial());
                saldoFinalTotal = saldoFinalTotal.add(cuenta.getResumenCuenta().getSaldoFinal());
                totalMovimientos += cuenta.getResumenCuenta().getTotalMovimientos();
            }
        }

        this.resumen = new ResumenReporteDTO(
                totalDepositos,
                totalRetiros,
                saldoInicialTotal,
                saldoFinalTotal,
                totalMovimientos,
                cuentas.size()
        );
    }

    public boolean tieneMovimientos() {
        return cuentas != null && cuentas.stream()
                .anyMatch(cuenta -> cuenta.getMovimientos() != null && !cuenta.getMovimientos().isEmpty());
    }

    public String getRangoFechasFormateado() {
        if (fechaInicio != null && fechaFin != null) {
            return String.format("Del %s al %s", fechaInicio, fechaFin);
        }
        return "Rango de fechas no especificado";
    }

    @Override
    public String toString() {
        return "ReporteDTO{" +
                "fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", clienteId='" + clienteId + '\'' +
                ", nombreCliente='" + nombreCliente + '\'' +
                ", fechaGeneracion=" + fechaGeneracion +
                ", totalCuentas=" + (cuentas != null ? cuentas.size() : 0) +
                '}';
    }
}
