package com.fal.account_service.application.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class CreateCuentaRequest {
    @NotBlank(message = "El número de cuenta es obligatorio")
    @Size(min = 6, max = 20, message = "El número de cuenta debe tener entre 6 y 20 caracteres")
    private String numeroCuenta;

    @NotBlank(message = "El tipo de cuenta es obligatorio")
    @Pattern(regexp = "^(AHORROS|CORRIENTE)$", message = "El tipo de cuenta debe ser AHORROS o CORRIENTE")
    private String tipoCuenta;

    @NotNull(message = "El saldo inicial es obligatorio")
    @PositiveOrZero(message = "El saldo inicial debe ser un valor positivo o cero")
    @Digits(integer = 10, fraction = 2, message = "El saldo inicial debe tener máximo 10 enteros y 2 decimales")
    private BigDecimal saldoInicial;

    @NotBlank(message = "El ID del cliente es obligatorio")
    @Size(min = 1, max = 20, message = "El ID del cliente debe tener entre 1 y 20 caracteres")
    private String clienteId;

    // Constructores
    public CreateCuentaRequest() {}

    public CreateCuentaRequest(String numeroCuenta, String tipoCuenta, BigDecimal saldoInicial, String clienteId) {
        this.numeroCuenta = numeroCuenta;
        this.tipoCuenta = tipoCuenta;
        this.saldoInicial = saldoInicial;
        this.clienteId = clienteId;
    }

    // Getters y Setters
    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }

    public String getTipoCuenta() { return tipoCuenta; }
    public void setTipoCuenta(String tipoCuenta) { this.tipoCuenta = tipoCuenta; }

    public BigDecimal getSaldoInicial() { return saldoInicial; }
    public void setSaldoInicial(BigDecimal saldoInicial) { this.saldoInicial = saldoInicial; }

    public String getClienteId() { return clienteId; }
    public void setClienteId(String clienteId) { this.clienteId = clienteId; }

    // Métodos utilitarios
    public boolean isTipoAhorros() {
        return "AHORROS".equalsIgnoreCase(tipoCuenta);
    }

    public boolean isTipoCorriente() {
        return "CORRIENTE".equalsIgnoreCase(tipoCuenta);
    }

    public void validarSaldoMinimo() {
        BigDecimal saldoMinimo = BigDecimal.ZERO;

        if (isTipoAhorros()) {
            saldoMinimo = new BigDecimal("0.00"); // Saldo mínimo para ahorros
        } else if (isTipoCorriente()) {
            saldoMinimo = new BigDecimal("0.00"); // Saldo mínimo para corriente
        }

        if (saldoInicial.compareTo(saldoMinimo) >= 0.00) {
            throw new IllegalArgumentException(
                    String.format("El saldo inicial para cuenta %s debe ser al menos %s",
                            tipoCuenta, saldoMinimo)
            );
        }
    }

    @Override
    public String toString() {
        return "CreateCuentaRequest{" +
                "numeroCuenta='" + numeroCuenta + '\'' +
                ", tipoCuenta='" + tipoCuenta + '\'' +
                ", saldoInicial=" + saldoInicial +
                ", clienteId='" + clienteId + '\'' +
                '}';
    }
}
