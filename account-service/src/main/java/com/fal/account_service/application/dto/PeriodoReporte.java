package com.fal.account_service.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class PeriodoReporte {
    @JsonProperty("fechaInicio")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaInicio;

    @JsonProperty("fechaFin")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaFin;

    @JsonProperty("dias")
    private int dias;

    // Constructores
    public PeriodoReporte() {}

    public PeriodoReporte(LocalDate fechaInicio, LocalDate fechaFin) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        if (fechaInicio != null && fechaFin != null) {
            this.dias = (int) java.time.temporal.ChronoUnit.DAYS.between(fechaInicio, fechaFin) + 1;
        }
    }

    // Getters y Setters
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
        calcularDias();
    }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
        calcularDias();
    }

    public int getDias() { return dias; }
    public void setDias(int dias) { this.dias = dias; }

    private void calcularDias() {
        if (fechaInicio != null && fechaFin != null) {
            this.dias = (int) java.time.temporal.ChronoUnit.DAYS.between(fechaInicio, fechaFin) + 1;
        }
    }

    public String getPeriodoFormateado() {
        if (fechaInicio != null && fechaFin != null) {
            return String.format("Del %s al %s (%d días)", fechaInicio, fechaFin, dias);
        }
        return "Periodo no especificado";
    }

}
