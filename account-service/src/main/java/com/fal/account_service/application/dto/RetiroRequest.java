package com.fal.account_service.application.dto;

import java.math.BigDecimal;

public class RetiroRequest {
    @jakarta.validation.constraints.NotNull(message = "El ID de la cuenta es obligatorio")
    private Long cuentaId;

    @jakarta.validation.constraints.NotNull(message = "El monto es obligatorio")
    @jakarta.validation.constraints.DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private BigDecimal monto;

    private String descripcion;

    // Getters y Setters
    public Long getCuentaId() { return cuentaId; }
    public void setCuentaId(Long cuentaId) { this.cuentaId = cuentaId; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
