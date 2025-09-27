package com.fal.account_service.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class ResumenGeneral {
    @JsonProperty("totalSaldoInicial")
    private BigDecimal totalSaldoInicial;

    @JsonProperty("totalSaldoFinal")
    private BigDecimal totalSaldoFinal;

    @JsonProperty("totalDepositos")
    private BigDecimal totalDepositos;

    @JsonProperty("totalRetiros")
    private BigDecimal totalRetiros;

    @JsonProperty("totalMovimientos")
    private int totalMovimientos;

    @JsonProperty("totalCuentas")
    private int totalCuentas;

    @JsonProperty("cuentasActivas")
    private int cuentasActivas;

    @JsonProperty("saldoNeto")
    private BigDecimal saldoNeto;

    @JsonProperty("variacionSaldo")
    private BigDecimal variacionSaldo;

    // Constructores
    public ResumenGeneral() {
        this.totalSaldoInicial = BigDecimal.ZERO;
        this.totalSaldoFinal = BigDecimal.ZERO;
        this.totalDepositos = BigDecimal.ZERO;
        this.totalRetiros = BigDecimal.ZERO;
        this.saldoNeto = BigDecimal.ZERO;
        this.variacionSaldo = BigDecimal.ZERO;
    }

    public ResumenGeneral(BigDecimal totalSaldoInicial, BigDecimal totalSaldoFinal,
                          BigDecimal totalDepositos, BigDecimal totalRetiros,
                          int totalMovimientos, int totalCuentas, int cuentasActivas) {
        this.totalSaldoInicial = totalSaldoInicial != null ? totalSaldoInicial : BigDecimal.ZERO;
        this.totalSaldoFinal = totalSaldoFinal != null ? totalSaldoFinal : BigDecimal.ZERO;
        this.totalDepositos = totalDepositos != null ? totalDepositos : BigDecimal.ZERO;
        this.totalRetiros = totalRetiros != null ? totalRetiros : BigDecimal.ZERO;
        this.totalMovimientos = totalMovimientos;
        this.totalCuentas = totalCuentas;
        this.cuentasActivas = cuentasActivas;
        this.saldoNeto = this.totalDepositos.subtract(this.totalRetiros);
        this.variacionSaldo = this.totalSaldoFinal.subtract(this.totalSaldoInicial);
    }

    // Getters y Setters
    public BigDecimal getTotalSaldoInicial() { return totalSaldoInicial; }
    public void setTotalSaldoInicial(BigDecimal totalSaldoInicial) {
        this.totalSaldoInicial = totalSaldoInicial != null ? totalSaldoInicial : BigDecimal.ZERO;
        calcularVariacion();
    }

    public BigDecimal getTotalSaldoFinal() { return totalSaldoFinal; }
    public void setTotalSaldoFinal(BigDecimal totalSaldoFinal) {
        this.totalSaldoFinal = totalSaldoFinal != null ? totalSaldoFinal : BigDecimal.ZERO;
        calcularVariacion();
    }

    public BigDecimal getTotalDepositos() { return totalDepositos; }
    public void setTotalDepositos(BigDecimal totalDepositos) {
        this.totalDepositos = totalDepositos != null ? totalDepositos : BigDecimal.ZERO;
        calcularSaldoNeto();
    }

    public BigDecimal getTotalRetiros() { return totalRetiros; }
    public void setTotalRetiros(BigDecimal totalRetiros) {
        this.totalRetiros = totalRetiros != null ? totalRetiros : BigDecimal.ZERO;
        calcularSaldoNeto();
    }

    public int getTotalMovimientos() { return totalMovimientos; }
    public void setTotalMovimientos(int totalMovimientos) { this.totalMovimientos = totalMovimientos; }

    public int getTotalCuentas() { return totalCuentas; }
    public void setTotalCuentas(int totalCuentas) { this.totalCuentas = totalCuentas; }

    public int getCuentasActivas() { return cuentasActivas; }
    public void setCuentasActivas(int cuentasActivas) { this.cuentasActivas = cuentasActivas; }

    public BigDecimal getSaldoNeto() { return saldoNeto; }
    public BigDecimal getVariacionSaldo() { return variacionSaldo; }

    private void calcularSaldoNeto() {
        this.saldoNeto = this.totalDepositos.subtract(this.totalRetiros);
    }

    private void calcularVariacion() {
        this.variacionSaldo = this.totalSaldoFinal.subtract(this.totalSaldoInicial);
    }

    // Métodos utilitarios adicionales
    public BigDecimal getPromedioMovimientosPorCuenta() {
        if (totalCuentas == 0) return BigDecimal.ZERO;
        return BigDecimal.valueOf(totalMovimientos).divide(BigDecimal.valueOf(totalCuentas), 2, BigDecimal.ROUND_HALF_UP);
    }

    public String getTasaActividad() {
        if (totalCuentas == 0) return "0%";
        double tasa = (double) cuentasActivas / totalCuentas * 100;
        return String.format("%.1f%%", tasa);
    }

}
