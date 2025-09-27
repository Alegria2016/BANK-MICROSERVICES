package com.fal.account_service.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class UpdateCuentaRequest {

    @Size(min = 6, max = 20, message = "El número de cuenta debe tener entre 6 y 20 caracteres")
    private String numeroCuenta;

    @Pattern(regexp = "^(AHORROS|CORRIENTE)$", message = "El tipo de cuenta debe ser AHORROS o CORRIENTE")
    private String tipoCuenta;

    @DecimalMin(value = "0.0", inclusive = false, message = "El saldo inicial debe ser mayor a 0")
    @Digits(integer = 10, fraction = 2, message = "El saldo inicial debe tener máximo 10 enteros y 2 decimales")
    private BigDecimal saldoInicial;

    private Boolean estado;

    // Constructores
    public UpdateCuentaRequest() {}

    public UpdateCuentaRequest(String numeroCuenta, String tipoCuenta, BigDecimal saldoInicial, Boolean estado) {
        this.numeroCuenta = numeroCuenta;
        this.tipoCuenta = tipoCuenta;
        this.saldoInicial = saldoInicial;
        this.estado = estado;
    }

    // Getters y Setters
    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }

    public String getTipoCuenta() { return tipoCuenta; }
    public void setTipoCuenta(String tipoCuenta) { this.tipoCuenta = tipoCuenta; }

    public BigDecimal getSaldoInicial() { return saldoInicial; }
    public void setSaldoInicial(BigDecimal saldoInicial) { this.saldoInicial = saldoInicial; }

    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }

    // Métodos utilitarios
    public boolean hasNumeroCuenta() {
        return numeroCuenta != null && !numeroCuenta.trim().isEmpty();
    }

    public boolean hasTipoCuenta() {
        return tipoCuenta != null && !tipoCuenta.trim().isEmpty();
    }

    public boolean hasSaldoInicial() {
        return saldoInicial != null;
    }

    public boolean hasEstado() {
        return estado != null;
    }

    public boolean isTipoAhorros() {
        return "AHORROS".equalsIgnoreCase(tipoCuenta);
    }

    public boolean isTipoCorriente() {
        return "CORRIENTE".equalsIgnoreCase(tipoCuenta);
    }

    @Override
    public String toString() {
        return "UpdateCuentaRequest{" +
                "numeroCuenta='" + numeroCuenta + '\'' +
                ", tipoCuenta='" + tipoCuenta + '\'' +
                ", saldoInicial=" + saldoInicial +
                ", estado=" + estado +
                '}';
    }
}
