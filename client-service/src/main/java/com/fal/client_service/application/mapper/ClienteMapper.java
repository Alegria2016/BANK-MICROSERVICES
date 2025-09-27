package com.fal.client_service.application.mapper;

import com.fal.client_service.application.dto.ClienteDTO;
import com.fal.client_service.application.dto.CreateClienteRequest;
import com.fal.client_service.application.dto.UpdateClienteRequest;
import com.fal.client_service.domain.model.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {
    public Cliente toEntity(CreateClienteRequest request) {
        Cliente cliente = new Cliente();
        cliente.setNombre(request.getNombre());
        cliente.setGenero(request.getGenero());
        cliente.setEdad(request.getEdad());
        cliente.setIdentificacion(request.getIdentificacion());
        cliente.setDireccion(request.getDireccion());
        cliente.setTelefono(request.getTelefono());
        cliente.setPassword(request.getPassword());
        cliente.setEstado(true); // Por defecto activo
        return cliente;
    }

    public void updateEntityFromRequest(UpdateClienteRequest request, Cliente cliente) {
        if (request.getNombre() != null) {
            cliente.setNombre(request.getNombre());
        }
        if (request.getGenero() != null) {
            cliente.setGenero(request.getGenero());
        }
        if (request.getEdad() != null) {
            cliente.setEdad(request.getEdad());
        }
        if (request.getIdentificacion() != null) {
            cliente.setIdentificacion(request.getIdentificacion());
        }
        if (request.getDireccion() != null) {
            cliente.setDireccion(request.getDireccion());
        }
        if (request.getTelefono() != null) {
            cliente.setTelefono(request.getTelefono());
        }
        if (request.hasPassword()) {
            cliente.setPassword(request.getPassword());
        }
        if (request.hasEstado()) {
            cliente.setEstado(request.getEstado());
        }
    }

    public ClienteDTO toDTO(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setNombre(cliente.getNombre());
        dto.setGenero(cliente.getGenero());
        dto.setEdad(cliente.getEdad());
        dto.setIdentificacion(cliente.getIdentificacion());
        dto.setDireccion(cliente.getDireccion());
        dto.setTelefono(cliente.getTelefono());
        dto.setClienteId(cliente.getClienteId());
        dto.setEstado(cliente.getEstado());
        dto.setFechaCreacion(cliente.getFechaCreacion());
        dto.setFechaActualizacion(cliente.getFechaActualizacion());
        return dto;
    }

    public CreateClienteRequest toCreateRequest(Cliente cliente) {
        CreateClienteRequest request = new CreateClienteRequest();
        request.setNombre(cliente.getNombre());
        request.setGenero(cliente.getGenero());
        request.setEdad(cliente.getEdad());
        request.setIdentificacion(cliente.getIdentificacion());
        request.setDireccion(cliente.getDireccion());
        request.setTelefono(cliente.getTelefono());
        request.setPassword(cliente.getPassword());
        return request;
    }
}
