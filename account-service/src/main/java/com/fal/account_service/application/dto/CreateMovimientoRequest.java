package com.fal.account_service.application.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class CreateMovimientoRequest {
    @NotNull(message = "El ID de la cuenta es obligatorio")
    private Long cuentaId;

    @NotBlank(message = "El tipo de movimiento es obligatorio")
    @Pattern(regexp = "^(DEPOSITO|RETIRO)$", message = "El tipo de movimiento debe ser DEPOSITO o RETIRO")
    private String tipoMovimiento;

    @NotNull(message = "El valor del movimiento es obligatorio")
    @DecimalMin(value = "0.00", message = "El valor debe ser mayor a 0")
    @Digits(integer = 10, fraction = 2, message = "El valor debe tener máximo 10 enteros y 2 decimales")
    private BigDecimal valor;

    @Size(max = 50, message = "La descripción no puede exceder 50 caracteres")
    private String descripcion;

    // Constructores
    public CreateMovimientoRequest() {}

    public CreateMovimientoRequest(Long cuentaId, String tipoMovimiento, BigDecimal valor, String descripcion) {
        this.cuentaId = cuentaId;
        this.tipoMovimiento = tipoMovimiento;
        this.valor = valor;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public Long getCuentaId() { return cuentaId; }
    public void setCuentaId(Long cuentaId) { this.cuentaId = cuentaId; }

    public String getTipoMovimiento() { return tipoMovimiento; }
    public void setTipoMovimiento(String tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    // Métodos utilitarios
    public boolean isDeposito() {
        return "DEPOSITO".equalsIgnoreCase(tipoMovimiento);
    }

    public boolean isRetiro() {
        return "RETIRO".equalsIgnoreCase(tipoMovimiento);
    }

    public BigDecimal getValorAbsoluto() {
        return valor != null ? valor.abs() : BigDecimal.ZERO;
    }

    public BigDecimal getValorConSigno() {
        if (valor == null) return BigDecimal.ZERO;
        return isRetiro() ? valor.negate() : valor;
    }

    public void validarMovimiento() {
        if (valor == null) {
            throw new IllegalArgumentException("El valor del movimiento no puede ser nulo");
        }

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El valor del movimiento debe ser mayor a 0");
        }

        if (isRetiro()) {
            // Validaciones específicas para retiros
            if (valor.compareTo(new BigDecimal("1000000")) > 0) {
                throw new IllegalArgumentException("El monto máximo para un retiro es de 1,000,000");
            }
        } else if (isDeposito()) {
            // Validaciones específicas para depósitos
            if (valor.compareTo(new BigDecimal("5000000")) > 0) {
                throw new IllegalArgumentException("El monto máximo para un depósito es de 5,000,000");
            }
        }
    }

    public String getDescripcionAutomatica() {
        if (descripcion != null && !descripcion.trim().isEmpty()) {
            return descripcion;
        }

        if (isDeposito()) {
            return String.format("Depósito por $%,.2f", valor);
        } else if (isRetiro()) {
            return String.format("Retiro por $%,.2f", valor);
        }

        return "Movimiento de cuenta";
    }

    @Override
    public String toString() {
        return "CreateMovimientoRequest{" +
                "cuentaId=" + cuentaId +
                ", tipoMovimiento='" + tipoMovimiento + '\'' +
                ", valor=" + valor +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
