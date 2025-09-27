package com.fal.account_service.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MovimientoResumenResponse {
    private String clienteId;
    private int totalMovimientos;
    private BigDecimal totalDepositos;
    private BigDecimal totalRetiros;
    private BigDecimal saldoNeto;
    private String periodo;

    // Getters y Setters
    public String getClienteId() { return clienteId; }
    public void setClienteId(String clienteId) { this.clienteId = clienteId; }

    public int getTotalMovimientos() { return totalMovimientos; }
    public void setTotalMovimientos(int totalMovimientos) { this.totalMovimientos = totalMovimientos; }

    public BigDecimal getTotalDepositos() { return totalDepositos; }
    public void setTotalDepositos(BigDecimal totalDepositos) { this.totalDepositos = totalDepositos; }

    public BigDecimal getTotalRetiros() { return totalRetiros; }
    public void setTotalRetiros(BigDecimal totalRetiros) { this.totalRetiros = totalRetiros; }

    public BigDecimal getSaldoNeto() { return saldoNeto; }
    public void setSaldoNeto(BigDecimal saldoNeto) { this.saldoNeto = saldoNeto; }

    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }
}

