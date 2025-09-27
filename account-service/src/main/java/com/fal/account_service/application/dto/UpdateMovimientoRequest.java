package com.fal.account_service.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class UpdateMovimientoRequest {

    @Pattern(regexp = "^(DEPOSITO|RETIRO)$", message = "El tipo de movimiento debe ser DEPOSITO o RETIRO")
    private String tipoMovimiento;

    @DecimalMin(value = "0.01", message = "El valor debe ser mayor a 0")
    @Digits(integer = 10, fraction = 2, message = "El valor debe tener máximo 10 enteros y 2 decimales")
    private BigDecimal valor;

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;

    // Constructores
    public UpdateMovimientoRequest() {}

    public UpdateMovimientoRequest(String tipoMovimiento, BigDecimal valor, String descripcion) {
        this.tipoMovimiento = tipoMovimiento;
        this.valor = valor;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public String getTipoMovimiento() { return tipoMovimiento; }
    public void setTipoMovimiento(String tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    // Métodos utilitarios
    public boolean hasTipoMovimiento() {
        return tipoMovimiento != null && !tipoMovimiento.trim().isEmpty();
    }

    public boolean hasValor() {
        return valor != null;
    }

    public boolean hasDescripcion() {
        return descripcion != null && !descripcion.trim().isEmpty();
    }

    public boolean isDeposito() {
        return "DEPOSITO".equalsIgnoreCase(tipoMovimiento);
    }

    public boolean isRetiro() {
        return "RETIRO".equalsIgnoreCase(tipoMovimiento);
    }

    @Override
    public String toString() {
        return "UpdateMovimientoRequest{" +
                "tipoMovimiento='" + tipoMovimiento + '\'' +
                ", valor=" + valor +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
