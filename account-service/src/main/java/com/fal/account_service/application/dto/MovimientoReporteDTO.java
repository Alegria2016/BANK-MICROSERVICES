package com.fal.account_service.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MovimientoReporteDTO {

    private Long movimientoId;
    private LocalDateTime fecha;
    private String tipoMovimiento;
    private BigDecimal valor;
    private BigDecimal saldo;
    private String movimiento;
    private String referencia;

    // Constructores
    public MovimientoReporteDTO() {}

    public MovimientoReporteDTO(Long movimientoId, LocalDateTime fecha, String tipoMovimiento,
                                BigDecimal valor, BigDecimal saldo, String movimiento) {
        this.movimientoId = movimientoId;
        this.fecha = fecha;
        this.tipoMovimiento = tipoMovimiento;
        this.valor = valor;
        this.saldo = saldo;
        this.movimiento = movimiento;
    }

    // Getters y Setters
    public Long getMovimientoId() { return movimientoId; }
    public void setMovimientoId(Long movimientoId) { this.movimientoId = movimientoId; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public String getTipoMovimiento() { return tipoMovimiento; }
    public void setTipoMovimiento(String tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public BigDecimal getSaldo() { return saldo; }
    public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }

    public String getDescripcion() { return movimiento; }
    public void setDescripcion(String descripcion) { this.movimiento = descripcion; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }

    // Métodos utilitarios
    public boolean isDeposito() {
        return "DEPOSITO".equalsIgnoreCase(tipoMovimiento);
    }

    public boolean isRetiro() {
        return "RETIRO".equalsIgnoreCase(tipoMovimiento);
    }

    public String getTipoMovimientoFormateado() {
        if (isDeposito()) {
            return "DEPÓSITO";
        } else if (isRetiro()) {
            return "RETIRO";
        }
        return tipoMovimiento;
    }

    public String getValorFormateado() {
        if (valor == null) return "$0.00";
        String signo = isRetiro() ? "-" : "+";
        return String.format("%s$%,.2f", signo, valor);
    }

    public String getSaldoFormateado() {
        if (saldo == null) return "$0.00";
        return String.format("$%,.2f", saldo);
    }

    public String getFechaFormateada() {
        if (fecha == null) return "";
        return fecha.toString();
    }

    public String getReferenciaFormateada() {
        if (referencia != null && !referencia.trim().isEmpty()) {
            return referencia;
        }
        return String.format("MOV-%06d", movimientoId);
    }

    @Override
    public String toString() {
        return "MovimientoReporteDTO{" +
                "fecha=" + fecha +
                ", tipoMovimiento='" + tipoMovimiento + '\'' +
                ", valor=" + valor +
                ", saldo=" + saldo +
                ", movimiento='" + movimiento + '\'' +
                '}';
    }
}
