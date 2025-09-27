package com.fal.account_service.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ClienteInfo {
    @JsonProperty("id")
    private String clienteId;

    @JsonProperty("nombre")
    private String nombre;

    @JsonProperty("identificacion")
    private String identificacion;

    @JsonProperty("telefono")
    private String telefono;

    @JsonProperty("direccion")
    private String direccion;

    // Constructores
    public ClienteInfo() {}

    public ClienteInfo(String clienteId, String nombre, String identificacion) {
        this.clienteId = clienteId;
        this.nombre = nombre;
        this.identificacion = identificacion;
    }

    // Getters y Setters
    public String getClienteId() { return clienteId; }
    public void setClienteId(String clienteId) { this.clienteId = clienteId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getIdentificacion() { return identificacion; }
    public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

}
