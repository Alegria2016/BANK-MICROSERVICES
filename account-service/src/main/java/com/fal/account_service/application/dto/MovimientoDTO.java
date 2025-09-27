package com.fal.account_service.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MovimientoDTO {

    private Long id;
    private LocalDateTime fecha;
    private String tipoMovimiento;
    private BigDecimal valor;
    private BigDecimal saldo;
    private String movimiento;
    private Long cuentaId;
    private String numeroCuenta;
    private String clienteId;
    private String nombreCliente;

    // Constructores
    public MovimientoDTO() {}

    public MovimientoDTO(Long id, LocalDateTime fecha, String tipoMovimiento, BigDecimal valor,
                         BigDecimal saldo, String movimiento, Long cuentaId, String numeroCuenta,
                         String clienteId, String nombreCliente) {
        this.id = id;
        this.fecha = fecha;
        this.tipoMovimiento = tipoMovimiento;
        this.valor = valor;
        this.saldo = saldo;
        this.movimiento = movimiento;
        this.cuentaId = cuentaId;
        this.numeroCuenta = numeroCuenta;
        this.clienteId = clienteId;
        this.nombreCliente = nombreCliente;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public String getTipoMovimiento() { return tipoMovimiento; }
    public void setTipoMovimiento(String tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public BigDecimal getSaldo() { return saldo; }
    public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }

    public String getDescripcion() { return movimiento; }
    public void setDescripcion(String descripcion) { this.movimiento = descripcion; }

    public Long getCuentaId() { return cuentaId; }
    public void setCuentaId(Long cuentaId) { this.cuentaId = cuentaId; }

    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }

    public String getClienteId() { return clienteId; }
    public void setClienteId(String clienteId) { this.clienteId = clienteId; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    // Métodos utilitarios
    public boolean isDeposito() {
        return "DEPOSITO".equalsIgnoreCase(tipoMovimiento);
    }

    public boolean isRetiro() {
        return "RETIRO".equalsIgnoreCase(tipoMovimiento);
    }

    public BigDecimal getValorConSigno() {
        if (valor == null) return BigDecimal.ZERO;
        return isRetiro() ? valor.negate() : valor;
    }

    public String getTipoMovimientoFormateado() {
        if (isDeposito()) {
            return "Depósito";
        } else if (isRetiro()) {
            return "Retiro";
        }
        return tipoMovimiento;
    }

    public String getValorFormateado() {
        if (valor == null) return "$0.00";
        return String.format("$%,.2f", isRetiro() ? valor.negate() : valor);
    }

    public String getSaldoFormateado() {
        if (saldo == null) return "$0.00";
        return String.format("$%,.2f", saldo);
    }

    @Override
    public String toString() {
        return "MovimientoDTO{" +
                "id=" + id +
                ", fecha=" + fecha +
                ", tipoMovimiento='" + tipoMovimiento + '\'' +
                ", valor=" + valor +
                ", saldo=" + saldo +
                ", movimiento='" + movimiento + '\'' +
                ", cuentaId=" + cuentaId +
                ", numeroCuenta='" + numeroCuenta + '\'' +
                ", clienteId='" + clienteId + '\'' +
                ", nombreCliente='" + nombreCliente + '\'' +
                '}';
    }
}
