package com.fal.client_service.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "clientes")
public class Cliente extends Persona {

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false)
    private Boolean estado = true;

    @Column(name = "cliente_id", nullable = false, unique = true, length = 20)
    private String clienteId;

    // Constructores
    public Cliente() {
    }

    public Cliente(String nombre, String genero, Integer edad, String identificacion,
                   String direccion, String telefono, String password) {
        setNombre(nombre);
        setGenero(genero);
        setEdad(edad);
        setIdentificacion(identificacion);
        setDireccion(direccion);
        setTelefono(telefono);
        setPassword(password);
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }
}
