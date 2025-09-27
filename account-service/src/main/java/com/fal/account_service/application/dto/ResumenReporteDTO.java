package com.fal.account_service.application.dto;

import java.math.BigDecimal;

public class ResumenReporteDTO {

    private BigDecimal totalDepositos;
    private BigDecimal totalRetiros;
    private BigDecimal saldoInicialTotal;
    private BigDecimal saldoFinalTotal;
    private int totalMovimientos;
    private int totalCuentas;
    private BigDecimal saldoNetoTotal;


    public ResumenReporteDTO() {
        this.totalDepositos = BigDecimal.ZERO;
        this.totalRetiros = BigDecimal.ZERO;
        this.saldoInicialTotal = BigDecimal.ZERO;
        this.saldoFinalTotal = BigDecimal.ZERO;
    }

    public ResumenReporteDTO(BigDecimal totalDepositos, BigDecimal totalRetiros,
                             BigDecimal saldoInicialTotal, BigDecimal saldoFinalTotal,
                             int totalMovimientos, int totalCuentas) {
        this.totalDepositos = totalDepositos != null ? totalDepositos : BigDecimal.ZERO;
        this.totalRetiros = totalRetiros != null ? totalRetiros : BigDecimal.ZERO;
        this.saldoInicialTotal = saldoInicialTotal != null ? saldoInicialTotal : BigDecimal.ZERO;
        this.saldoFinalTotal = saldoFinalTotal != null ? saldoFinalTotal : BigDecimal.ZERO;
        this.totalMovimientos = totalMovimientos;
        this.totalCuentas = totalCuentas;
        this.saldoNetoTotal = this.totalDepositos.subtract(this.totalRetiros);
    }

    public BigDecimal getTotalDepositos() { return totalDepositos; }
    public void setTotalDepositos(BigDecimal totalDepositos) {
        this.totalDepositos = totalDepositos != null ? totalDepositos : BigDecimal.ZERO;
        calcularSaldoNetoTotal();
    }

    public BigDecimal getTotalRetiros() { return totalRetiros; }
    public void setTotalRetiros(BigDecimal totalRetiros) {
        this.totalRetiros = totalRetiros != null ? totalRetiros : BigDecimal.ZERO;
        calcularSaldoNetoTotal();
    }

    public BigDecimal getSaldoInicialTotal() { return saldoInicialTotal; }
    public void setSaldoInicialTotal(BigDecimal saldoInicialTotal) {
        this.saldoInicialTotal = saldoInicialTotal != null ? saldoInicialTotal : BigDecimal.ZERO;
    }

    public BigDecimal getSaldoFinalTotal() { return saldoFinalTotal; }
    public void setSaldoFinalTotal(BigDecimal saldoFinalTotal) {
        this.saldoFinalTotal = saldoFinalTotal != null ? saldoFinalTotal : BigDecimal.ZERO;
    }

    public int getTotalMovimientos() { return totalMovimientos; }
    public void setTotalMovimientos(int totalMovimientos) { this.totalMovimientos = totalMovimientos; }

    public int getTotalCuentas() { return totalCuentas; }
    public void setTotalCuentas(int totalCuentas) { this.totalCuentas = totalCuentas; }

    public BigDecimal getSaldoNetoTotal() { return saldoNetoTotal; }

    // Métodos utilitarios
    private void calcularSaldoNetoTotal() {
        this.saldoNetoTotal = totalDepositos.subtract(totalRetiros);
    }

    public BigDecimal getVariacionSaldoTotal() {
        return saldoFinalTotal.subtract(saldoInicialTotal);
    }

    public BigDecimal getPromedioMovimientosPorCuenta() {
        if (totalCuentas == 0) return BigDecimal.ZERO;
        return BigDecimal.valueOf(totalMovimientos).divide(BigDecimal.valueOf(totalCuentas), 2);
    }

    // Métodos de formateo
    public String getTotalDepositosFormateado() {
        return String.format("$%,.2f", totalDepositos);
    }

    public String getTotalRetirosFormateado() {
        return String.format("$%,.2f", totalRetiros);
    }

    public String getSaldoInicialTotalFormateado() {
        return String.format("$%,.2f", saldoInicialTotal);
    }

    public String getSaldoFinalTotalFormateado() {
        return String.format("$%,.2f", saldoFinalTotal);
    }

    public String getSaldoNetoTotalFormateado() {
        return String.format("$%,.2f", saldoNetoTotal);
    }

    public String getVariacionSaldoTotalFormateado() {
        BigDecimal variacion = getVariacionSaldoTotal();
        String signo = variacion.compareTo(BigDecimal.ZERO) >= 0 ? "+" : "";
        return String.format("%s$%,.2f", signo, variacion);
    }

    @Override
    public String toString() {
        return "ResumenReporteDTO{" +
                "totalDepositos=" + totalDepositos +
                ", totalRetiros=" + totalRetiros +
                ", saldoInicialTotal=" + saldoInicialTotal +
                ", saldoFinalTotal=" + saldoFinalTotal +
                ", totalMovimientos=" + totalMovimientos +
                ", totalCuentas=" + totalCuentas +
                '}';
    }
}
