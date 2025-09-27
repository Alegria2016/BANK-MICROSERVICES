package com.fal.account_service.application.dto;

import java.math.BigDecimal;

public class VerificacionSaldoResponse {
    private Long cuentaId;
    private BigDecimal montoSolicitado;
    private BigDecimal saldoActual;
    private boolean saldoSuficiente;
    private BigDecimal diferencia;

    public VerificacionSaldoResponse(Long cuentaId, BigDecimal montoSolicitado, BigDecimal saldoActual) {
        this.cuentaId = cuentaId;
        this.montoSolicitado = montoSolicitado;
        this.saldoActual = saldoActual;
        this.saldoSuficiente = saldoActual.compareTo(montoSolicitado) >= 0;
        this.diferencia = saldoActual.subtract(montoSolicitado);
    }

    // Getters y Setters
    public Long getCuentaId() { return cuentaId; }
    public void setCuentaId(Long cuentaId) { this.cuentaId = cuentaId; }

    public BigDecimal getMontoSolicitado() { return montoSolicitado; }
    public void setMontoSolicitado(BigDecimal montoSolicitado) { this.montoSolicitado = montoSolicitado; }

    public BigDecimal getSaldoActual() { return saldoActual; }
    public void setSaldoActual(BigDecimal saldoActual) { this.saldoActual = saldoActual; }

    public boolean isSaldoSuficiente() { return saldoSuficiente; }
    public void setSaldoSuficiente(boolean saldoSuficiente) { this.saldoSuficiente = saldoSuficiente; }

    public BigDecimal getDiferencia() { return diferencia; }
    public void setDiferencia(BigDecimal diferencia) { this.diferencia = diferencia; }
}
