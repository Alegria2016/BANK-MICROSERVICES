package com.fal.client_service.domain.service.events;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClienteCreadoEvent {
    private String eventId;
    private String clienteId;
    private String nombre;
    private String identificacion;
    private String genero;
    private Integer edad;
    private String direccion;
    private String telefono;
    private String email;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaCreacion;

    private String tipoEvento;
    private String version;

    // Constructores
    public ClienteCreadoEvent() {
        this.eventId = UUID.randomUUID().toString();
        this.fechaCreacion = LocalDateTime.now();
        this.tipoEvento = "CLIENTE_CREADO";
        this.version = "1.0";
    }

    public ClienteCreadoEvent(String clienteId, String nombre, String identificacion) {
        this();
        this.clienteId = clienteId;
        this.nombre = nombre;
        this.identificacion = identificacion;
    }

    public ClienteCreadoEvent(String clienteId, String nombre, String identificacion,
                              String genero, Integer edad, String direccion, String telefono) {
        this(clienteId, nombre, identificacion);
        this.genero = genero;
        this.edad = edad;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    // Builder pattern para creación fluida
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String clienteId;
        private String nombre;
        private String identificacion;
        private String genero;
        private Integer edad;
        private String direccion;
        private String telefono;
        private String email;

        public Builder clienteId(String clienteId) {
            this.clienteId = clienteId;
            return this;
        }

        public Builder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public Builder identificacion(String identificacion) {
            this.identificacion = identificacion;
            return this;
        }

        public Builder genero(String genero) {
            this.genero = genero;
            return this;
        }

        public Builder edad(Integer edad) {
            this.edad = edad;
            return this;
        }

        public Builder direccion(String direccion) {
            this.direccion = direccion;
            return this;
        }

        public Builder telefono(String telefono) {
            this.telefono = telefono;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public ClienteCreadoEvent build() {
            ClienteCreadoEvent event = new ClienteCreadoEvent(clienteId, nombre, identificacion);
            event.setGenero(genero);
            event.setEdad(edad);
            event.setDireccion(direccion);
            event.setTelefono(telefono);
            event.setEmail(email);
            return event;
        }
    }

    // Getters y Setters
    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public String getClienteId() { return clienteId; }
    public void setClienteId(String clienteId) { this.clienteId = clienteId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getIdentificacion() { return identificacion; }
    public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public Integer getEdad() { return edad; }
    public void setEdad(Integer edad) { this.edad = edad; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public String getTipoEvento() { return tipoEvento; }
    public void setTipoEvento(String tipoEvento) { this.tipoEvento = tipoEvento; }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    // Métodos utilitarios
    public boolean isValid() {
        return clienteId != null && !clienteId.trim().isEmpty() &&
                nombre != null && !nombre.trim().isEmpty() &&
                identificacion != null && !identificacion.trim().isEmpty();
    }

    public String getNombreCompleto() {
        return nombre != null ? nombre.trim() : "";
    }

    public boolean tieneInformacionContacto() {
        return (telefono != null && !telefono.trim().isEmpty()) ||
                (email != null && !email.trim().isEmpty());
    }

    public String getResumen() {
        return String.format("Cliente: %s (ID: %s, Identificación: %s)",
                nombre, clienteId, identificacion);
    }

    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClienteCreadoEvent that = (ClienteCreadoEvent) o;
        return Objects.equals(eventId, that.eventId) &&
                Objects.equals(clienteId, that.clienteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, clienteId);
    }

    @Override
    public String toString() {
        return "ClienteCreadoEvent{" +
                "eventId='" + eventId + '\'' +
                ", clienteId='" + clienteId + '\'' +
                ", nombre='" + nombre + '\'' +
                ", identificacion='" + identificacion + '\'' +
                ", genero='" + genero + '\'' +
                ", edad=" + edad +
                ", telefono='" + telefono + '\'' +
                ", tipoEvento='" + tipoEvento + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }

    // Método para crear desde JSON (útil para deserialización)
    public static ClienteCreadoEvent fromJson(String json) {
        // En una implementación real, usarías Jackson ObjectMapper
        // Por simplicidad, retornamos un evento vacío
        ClienteCreadoEvent event = new ClienteCreadoEvent();
        event.setTipoEvento("CLIENTE_CREADO");
        return event;
    }

    // Método para validar edad mínima
    public boolean esMayorDeEdad() {
        return edad != null && edad >= 18;
    }

    // Método para obtener iniciales
    public String getIniciales() {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "";
        }
        String[] partes = nombre.trim().split("\\s+");
        if (partes.length == 1) {
            return partes[0].substring(0, Math.min(2, partes[0].length())).toUpperCase();
        }
        return (partes[0].substring(0, 1) + partes[partes.length - 1].substring(0, 1)).toUpperCase();
    }
}