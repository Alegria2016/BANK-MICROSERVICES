package com.fal.account_service.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class MetadataReporte {
    @JsonProperty("fechaGeneracion")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaGeneracion;

    @JsonProperty("version")
    private String version;

    @JsonProperty("formato")
    private String formato;

    // Constructores
    public MetadataReporte() {
        this.fechaGeneracion = LocalDateTime.now();
        this.version = "1.0";
        this.formato = "JSON";
    }

    // Getters y Setters
    public LocalDateTime getFechaGeneracion() { return fechaGeneracion; }
    public void setFechaGeneracion(LocalDateTime fechaGeneracion) { this.fechaGeneracion = fechaGeneracion; }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    public String getFormato() { return formato; }
    public void setFormato(String formato) { this.formato = formato; }

}
