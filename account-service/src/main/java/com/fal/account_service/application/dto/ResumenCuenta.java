package com.fal.account_service.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class ResumenCuenta {
    @JsonProperty("totalCreditos")
    private BigDecimal totalCreditos;

    @JsonProperty("totalDebitos")
    private BigDecimal totalDebitos;

    @JsonProperty("saldoInicial")
    private BigDecimal saldoInicial;

    @JsonProperty("saldoFinal")
    private BigDecimal saldoFinal;

    @JsonProperty("totalMovimientos")
    private int totalMovimientos;

    @JsonProperty("saldoNeto")
    private BigDecimal saldoNeto;

    // Constructores
    public ResumenCuenta() {
        this.totalCreditos = BigDecimal.ZERO;
        this.totalDebitos = BigDecimal.ZERO;
        this.saldoInicial = BigDecimal.ZERO;
        this.saldoFinal = BigDecimal.ZERO;
    }

    public ResumenCuenta(BigDecimal totalCreditos, BigDecimal totalDebitos,
                         BigDecimal saldoInicial, BigDecimal saldoFinal, int totalMovimientos) {
        this.totalCreditos = totalCreditos != null ? totalCreditos : BigDecimal.ZERO;
        this.totalDebitos = totalDebitos != null ? totalDebitos : BigDecimal.ZERO;
        this.saldoInicial = saldoInicial != null ? saldoInicial : BigDecimal.ZERO;
        this.saldoFinal = saldoFinal != null ? saldoFinal : BigDecimal.ZERO;
        this.totalMovimientos = totalMovimientos;
        this.saldoNeto = this.totalCreditos.subtract(this.totalDebitos);
    }

    // Getters y Setters
    public BigDecimal getTotalCreditos() { return totalCreditos; }
    public void setTotalCreditos(BigDecimal totalCreditos) {
        this.totalCreditos = totalCreditos != null ? totalCreditos : BigDecimal.ZERO;
        calcularSaldoNeto();
    }

    public BigDecimal getTotalDebitos() { return totalDebitos; }
    public void setTotalDebitos(BigDecimal totalDebitos) {
        this.totalDebitos = totalDebitos != null ? totalDebitos : BigDecimal.ZERO;
        calcularSaldoNeto();
    }

    public BigDecimal getSaldoInicial() { return saldoInicial; }
    public void setSaldoInicial(BigDecimal saldoInicial) { this.saldoInicial = saldoInicial; }

    public BigDecimal getSaldoFinal() { return saldoFinal; }
    public void setSaldoFinal(BigDecimal saldoFinal) { this.saldoFinal = saldoFinal; }

    public int getTotalMovimientos() { return totalMovimientos; }
    public void setTotalMovimientos(int totalMovimientos) { this.totalMovimientos = totalMovimientos; }

    public BigDecimal getSaldoNeto() { return saldoNeto; }

    private void calcularSaldoNeto() {
        this.saldoNeto = totalCreditos.subtract(totalDebitos);
    }

    public BigDecimal getVariacionSaldo() {
        return saldoFinal.subtract(saldoInicial);
    }

    public void setSaldoNeto(BigDecimal saldoNeto) {
        this.saldoNeto = saldoNeto;
    }
}
