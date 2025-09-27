package com.fal.account_service.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MovimientoEstado {
    @JsonProperty("id")
    private Long movimientoId;

    @JsonProperty("fecha")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fecha;

    @JsonProperty("tipo")
    private String tipo;

    @JsonProperty("movimiento")
    private String movimiento;

    @JsonProperty("monto")
    private BigDecimal monto;

    @JsonProperty("saldo")
    private BigDecimal saldo;

    @JsonProperty("referencia")
    private String referencia;

    @JsonProperty("tipoOperacion")
    private String tipoOperacion; // CREDITO/DEBITO

    // Constructores
    public MovimientoEstado() {}

    public MovimientoEstado(Long movimientoId, LocalDateTime fecha, String tipo,
                            String descripcion, BigDecimal monto, BigDecimal saldo) {
        this.movimientoId = movimientoId;
        this.fecha = fecha;
        this.tipo = tipo;
        this.movimiento = movimiento;
        this.monto = monto;
        this.saldo = saldo;
        this.tipoOperacion = "DEPOSITO".equalsIgnoreCase(tipo) ? "CREDITO" : "DEBITO";
        this.referencia = String.format("MOV-%06d", movimientoId);
    }

    // Getters y Setters
    public Long getMovimientoId() { return movimientoId; }
    public void setMovimientoId(Long movimientoId) { this.movimientoId = movimientoId; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) {
        this.tipo = tipo;
        this.tipoOperacion = "DEPOSITO".equalsIgnoreCase(tipo) ? "CREDITO" : "DEBITO";
    }

    public String getDescripcion() { return movimiento; }
    public void setDescripcion(String descripcion) { this.movimiento = descripcion; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public BigDecimal getSaldo() { return saldo; }
    public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }

    public String getTipoOperacion() { return tipoOperacion; }
    public void setTipoOperacion(String tipoOperacion) { this.tipoOperacion = tipoOperacion; }

    // Métodos utilitarios
    public boolean isCredito() {
        return "CREDITO".equalsIgnoreCase(tipoOperacion);
    }

    public boolean isDebito() {
        return "DEBITO".equalsIgnoreCase(tipoOperacion);
    }

    public String getMontoFormateado() {
        if (monto == null) return "$0.00";
        String signo = isCredito() ? "+" : "-";
        return String.format("%s$%,.2f", signo, monto);
    }

    public String getSaldoFormateado() {
        if (saldo == null) return "$0.00";
        return String.format("$%,.2f", saldo);
    }
}
