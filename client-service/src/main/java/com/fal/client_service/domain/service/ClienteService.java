package com.fal.client_service.domain.service;

import com.fal.client_service.application.dto.ClienteDTO;
import com.fal.client_service.application.dto.CreateClienteRequest;
import com.fal.client_service.application.dto.UpdateClienteRequest;
import com.fal.client_service.domain.model.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteService {
    ClienteDTO crearCliente(CreateClienteRequest cliente);
    Optional<ClienteDTO> obtenerClientePorId(Long id);
    Optional<ClienteDTO> obtenerClientePorIdentificacion(String identificacion);
    List<ClienteDTO> obtenerTodosLosClientes();
    ClienteDTO actualizarCliente(Long id, UpdateClienteRequest cliente);
    void eliminarCliente(Long id);
    boolean existeClientePorIdentificacion(String identificacion);
    Optional<ClienteDTO> obtenerClientePorClienteId(String clienteId);
    public List<ClienteDTO> obtenerClientesActivos();
    public ClienteDTO activarCliente(Long id);
}
