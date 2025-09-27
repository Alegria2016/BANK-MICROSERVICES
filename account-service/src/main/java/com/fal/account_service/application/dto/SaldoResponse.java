package com.fal.account_service.application.dto;

import java.math.BigDecimal;

public class SaldoResponse {
    private BigDecimal cuentaId; // Cambiado a BigDecimal
    private BigDecimal saldo;

    public SaldoResponse(BigDecimal cuentaId, BigDecimal saldo) {
        this.cuentaId = cuentaId;
        this.saldo = saldo;
    }

    // Getters
    public BigDecimal getCuentaId() { return cuentaId; }
    public BigDecimal getSaldo() { return saldo; }

}
