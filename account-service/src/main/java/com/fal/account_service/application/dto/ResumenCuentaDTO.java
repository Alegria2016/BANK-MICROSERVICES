package com.fal.account_service.application.dto;

import java.math.BigDecimal;

public class ResumenCuentaDTO {

    private BigDecimal totalDepositos;
    private BigDecimal totalRetiros;
    private BigDecimal saldoInicial;
    private BigDecimal saldoFinal;
    private int totalMovimientos;
    private BigDecimal saldoNeto;

    // Constructores
    public ResumenCuentaDTO() {
        this.totalDepositos = BigDecimal.ZERO;
        this.totalRetiros = BigDecimal.ZERO;
        this.saldoInicial = BigDecimal.ZERO;
        this.saldoFinal = BigDecimal.ZERO;
    }

    public ResumenCuentaDTO(BigDecimal totalDepositos, BigDecimal totalRetiros,
                            BigDecimal saldoInicial, BigDecimal saldoFinal, int totalMovimientos) {
        this.totalDepositos = totalDepositos != null ? totalDepositos : BigDecimal.ZERO;
        this.totalRetiros = totalRetiros != null ? totalRetiros : BigDecimal.ZERO;
        this.saldoInicial = saldoInicial != null ? saldoInicial : BigDecimal.ZERO;
        this.saldoFinal = saldoFinal != null ? saldoFinal : BigDecimal.ZERO;
        this.totalMovimientos = totalMovimientos;
        this.saldoNeto = this.totalDepositos.subtract(this.totalRetiros);
    }

    // Getters y Setters
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

    public BigDecimal getSaldoInicial() { return saldoInicial; }
    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial != null ? saldoInicial : BigDecimal.ZERO;
    }

    public BigDecimal getSaldoFinal() { return saldoFinal; }
    public void setSaldoFinal(BigDecimal saldoFinal) {
        this.saldoFinal = saldoFinal != null ? saldoFinal : BigDecimal.ZERO;
    }

    public int getTotalMovimientos() { return totalMovimientos; }
    public void setTotalMovimientos(int totalMovimientos) { this.totalMovimientos = totalMovimientos; }

    public BigDecimal getSaldoNeto() { return saldoNeto; }

    // Métodos utilitarios
    private void calcularSaldoNeto() {
        this.saldoNeto = totalDepositos.subtract(totalRetiros);
    }

    public BigDecimal getVariacionSaldo() {
        return saldoFinal.subtract(saldoInicial);
    }

    public String getTotalDepositosFormateado() {
        return String.format("$%,.2f", totalDepositos);
    }

    public String getTotalRetirosFormateado() {
        return String.format("$%,.2f", totalRetiros);
    }

    public String getSaldoInicialFormateado() {
        return String.format("$%,.2f", saldoInicial);
    }

    public String getSaldoFinalFormateado() {
        return String.format("$%,.2f", saldoFinal);
    }

    public String getSaldoNetoFormateado() {
        return String.format("$%,.2f", saldoNeto);
    }

    public String getVariacionSaldoFormateado() {
        BigDecimal variacion = getVariacionSaldo();
        String signo = variacion.compareTo(BigDecimal.ZERO) >= 0 ? "+" : "";
        return String.format("%s$%,.2f", signo, variacion);
    }

    @Override
    public String toString() {
        return "ResumenCuentaDTO{" +
                "totalDepositos=" + totalDepositos +
                ", totalRetiros=" + totalRetiros +
                ", saldoInicial=" + saldoInicial +
                ", saldoFinal=" + saldoFinal +
                ", totalMovimientos=" + totalMovimientos +
                '}';
    }
}
