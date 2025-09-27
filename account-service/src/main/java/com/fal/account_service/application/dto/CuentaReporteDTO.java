package com.fal.account_service.application.dto;

import java.math.BigDecimal;
import java.util.List;

public class CuentaReporteDTO {

    private Long cuentaId;
    private String numeroCuenta;
    private String tipoCuenta;
    private BigDecimal saldoInicial;
    private BigDecimal saldoActual;
    private Boolean estado;
    private List<MovimientoReporteDTO> movimientos;
    private ResumenCuentaDTO resumenCuenta;

    // Constructores
    public CuentaReporteDTO() {}

    public CuentaReporteDTO(Long cuentaId, String numeroCuenta, String tipoCuenta,
                            BigDecimal saldoInicial, BigDecimal saldoActual, Boolean estado) {
        this.cuentaId = cuentaId;
        this.numeroCuenta = numeroCuenta;
        this.tipoCuenta = tipoCuenta;
        this.saldoInicial = saldoInicial;
        this.saldoActual = saldoActual;
        this.estado = estado;
    }

    // Getters y Setters
    public Long getCuentaId() { return cuentaId; }
    public void setCuentaId(Long cuentaId) { this.cuentaId = cuentaId; }

    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }

    public String getTipoCuenta() { return tipoCuenta; }
    public void setTipoCuenta(String tipoCuenta) { this.tipoCuenta = tipoCuenta; }

    public BigDecimal getSaldoInicial() { return saldoInicial; }
    public void setSaldoInicial(BigDecimal saldoInicial) { this.saldoInicial = saldoInicial; }

    public BigDecimal getSaldoActual() { return saldoActual; }
    public void setSaldoActual(BigDecimal saldoActual) { this.saldoActual = saldoActual; }

    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }

    public List<MovimientoReporteDTO> getMovimientos() { return movimientos; }
    public void setMovimientos(List<MovimientoReporteDTO> movimientos) { this.movimientos = movimientos; }

    public ResumenCuentaDTO getResumenCuenta() { return resumenCuenta; }
    public void setResumenCuenta(ResumenCuentaDTO resumenCuenta) { this.resumenCuenta = resumenCuenta; }

    // Métodos utilitarios
    public void calcularResumenCuenta() {
        if (movimientos == null || movimientos.isEmpty()) {
            this.resumenCuenta = new ResumenCuentaDTO(
                    BigDecimal.ZERO, BigDecimal.ZERO, saldoInicial, saldoActual, 0
            );
            return;
        }

        BigDecimal totalDepositos = BigDecimal.ZERO;
        BigDecimal totalRetiros = BigDecimal.ZERO;

        for (MovimientoReporteDTO movimiento : movimientos) {
            if (movimiento.isDeposito()) {
                totalDepositos = totalDepositos.add(movimiento.getValor());
            } else if (movimiento.isRetiro()) {
                totalRetiros = totalRetiros.add(movimiento.getValor());
            }
        }

        this.resumenCuenta = new ResumenCuentaDTO(
                totalDepositos,
                totalRetiros,
                saldoInicial,
                saldoActual,
                movimientos.size()
        );
    }

    public boolean isActiva() {
        return Boolean.TRUE.equals(estado);
    }

    public String getTipoCuentaFormateado() {
        if ("AHORROS".equalsIgnoreCase(tipoCuenta)) {
            return "Cuenta de Ahorros";
        } else if ("CORRIENTE".equalsIgnoreCase(tipoCuenta)) {
            return "Cuenta Corriente";
        }
        return tipoCuenta;
    }

    @Override
    public String toString() {
        return "CuentaReporteDTO{" +
                "numeroCuenta='" + numeroCuenta + '\'' +
                ", tipoCuenta='" + tipoCuenta + '\'' +
                ", saldoActual=" + saldoActual +
                ", totalMovimientos=" + (movimientos != null ? movimientos.size() : 0) +
                '}';
    }
}
