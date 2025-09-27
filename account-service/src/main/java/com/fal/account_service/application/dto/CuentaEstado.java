package com.fal.account_service.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

public class CuentaEstado {
    @JsonProperty("id")
    private Long cuentaId;

    @JsonProperty("numeroCuenta")
    private String numeroCuenta;

    @JsonProperty("tipoCuenta")
    private String tipoCuenta;

    @JsonProperty("tipoCuentaDescripcion")
    private String tipoCuentaDescripcion;

    @JsonProperty("estado")
    private String estado;

    @JsonProperty("saldoInicial")
    private BigDecimal saldoInicial;

    @JsonProperty("saldoFinal")
    private BigDecimal saldoFinal;

    @JsonProperty("movimientos")
    private List<MovimientoEstado> movimientos;

    @JsonProperty("resumenCuenta")
    private ResumenCuentaDTO resumenCuenta;


    public CuentaEstado() {}

    public CuentaEstado(Long cuentaId, String numeroCuenta, String tipoCuenta,
                        BigDecimal saldoInicial, BigDecimal saldoFinal, boolean activa) {
        this.cuentaId = cuentaId;
        this.numeroCuenta = numeroCuenta;
        this.tipoCuenta = tipoCuenta;
        this.tipoCuentaDescripcion = obtenerDescripcionTipoCuenta(tipoCuenta);
        this.estado = activa ? "ACTIVA" : "INACTIVA";
        this.saldoInicial = saldoInicial;
        this.saldoFinal = saldoFinal;
    }

    // Getters y Setters
    public Long getCuentaId() { return cuentaId; }
    public void setCuentaId(Long cuentaId) { this.cuentaId = cuentaId; }

    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }

    public String getTipoCuenta() { return tipoCuenta; }
    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
        this.tipoCuentaDescripcion = obtenerDescripcionTipoCuenta(tipoCuenta);
    }

    public String getTipoCuentaDescripcion() { return tipoCuentaDescripcion; }
    public void setTipoCuentaDescripcion(String tipoCuentaDescripcion) { this.tipoCuentaDescripcion = tipoCuentaDescripcion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public BigDecimal getSaldoInicial() { return saldoInicial; }
    public void setSaldoInicial(BigDecimal saldoInicial) { this.saldoInicial = saldoInicial; }

    public BigDecimal getSaldoFinal() { return saldoFinal; }
    public void setSaldoFinal(BigDecimal saldoFinal) { this.saldoFinal = saldoFinal; }

    public List<MovimientoEstado> getMovimientos() { return movimientos; }
    public void setMovimientos(List<MovimientoEstado> movimientos) { this.movimientos = movimientos; }

    public ResumenCuentaDTO getResumenCuenta() { return resumenCuenta; }
    public void setResumenCuenta(ResumenCuentaDTO resumenCuenta) { this.resumenCuenta = resumenCuenta; }

    // Métodos utilitarios
    public boolean isActiva() {
        return "ACTIVA".equals(estado);
    }

    private String obtenerDescripcionTipoCuenta(String tipoCuenta) {
        if ("AHORROS".equalsIgnoreCase(tipoCuenta)) {
            return "Cuenta de Ahorros";
        } else if ("CORRIENTE".equalsIgnoreCase(tipoCuenta)) {
            return "Cuenta Corriente";
        }
        return tipoCuenta;
    }

    public void calcularResumenCuenta() {
        if (movimientos == null || movimientos.isEmpty()) {
            this.resumenCuenta = new ResumenCuentaDTO(
                    BigDecimal.ZERO, BigDecimal.ZERO, saldoInicial, saldoFinal, 0
            );
            return;
        }

        BigDecimal totalCreditos = BigDecimal.ZERO;
        BigDecimal totalDebitos = BigDecimal.ZERO;

        for (MovimientoEstado movimiento : movimientos) {
            if (movimiento.isCredito()) {
                totalCreditos = totalCreditos.add(movimiento.getMonto());
            } else if (movimiento.isDebito()) {
                totalDebitos = totalDebitos.add(movimiento.getMonto());
            }
        }

        this.resumenCuenta = new ResumenCuentaDTO(
                totalCreditos,
                totalDebitos,
                saldoInicial,
                saldoFinal,
                movimientos.size()
        );
    }
}
