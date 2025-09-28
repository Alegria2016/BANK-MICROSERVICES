package com.fal.client_service.application.service;

import com.fal.client_service.application.dto.ClienteDTO;
import com.fal.client_service.application.dto.CreateClienteRequest;
import com.fal.client_service.application.dto.UpdateClienteRequest;
import com.fal.client_service.application.mapper.ClienteMapper;
import com.fal.client_service.domain.model.Cliente;
import com.fal.client_service.domain.service.ClienteService;
import com.fal.client_service.domain.service.events.ClienteCreadoEvent;
import com.fal.client_service.infrastructure.messaging.ClienteEventPublisher;
import com.fal.client_service.infrastructure.persistence.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

   private final ClienteEventPublisher eventPublisher;

    @Autowired
    public ClienteServiceImpl(ClienteRepository clienteRepository, ClienteMapper clienteMapper, ClienteEventPublisher eventPublisher) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public ClienteDTO crearCliente(CreateClienteRequest request) {
        if (clienteRepository.existsByIdentificacion(request.getIdentificacion())) {
            throw new RuntimeException("Ya existe un cliente con la identificación: " + request.getIdentificacion());
        }

        Cliente cliente = clienteMapper.toEntity(request);
        cliente.setClienteId(generarClienteId());

        Cliente clienteGuardado = clienteRepository.save(cliente);

        // Publicar evento
        ClienteCreadoEvent event = new ClienteCreadoEvent(
                clienteGuardado.getClienteId(),
                clienteGuardado.getNombre(),
                clienteGuardado.getIdentificacion()

        );
        eventPublisher.publicarClienteCreado(event);

        return clienteMapper.toDTO(clienteGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClienteDTO> obtenerClientePorId(Long id) {
        return clienteRepository.findById(id)
                .map(clienteMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClienteDTO> obtenerClientePorIdentificacion(String identificacion) {
        return clienteRepository.findByIdentificacion(identificacion)
                .map(clienteMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteDTO> obtenerTodosLosClientes() {
        System.out.println("CONSULTANDO CLIENTES...");

        return clienteRepository.findAll()
                .stream()
                .map(clienteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ClienteDTO actualizarCliente(Long id, UpdateClienteRequest request) {
        Optional<Cliente> clienteExistenteOpt = clienteRepository.findById(id);

        if (clienteExistenteOpt.isPresent()) {
            Cliente clienteExistente = clienteExistenteOpt.get();

            // Verificar si la identificación ya existe en otro cliente
            if (!clienteExistente.getIdentificacion().equals(request.getIdentificacion()) &&
                    clienteRepository.existsByIdentificacion(request.getIdentificacion())) {
                throw new RuntimeException("Ya existe otro cliente con la identificación: " + request.getIdentificacion());
            }

            // Actualizar campos
            clienteMapper.updateEntityFromRequest(request, clienteExistente);

            Cliente clienteActualizado = clienteRepository.save(clienteExistente);
            return clienteMapper.toDTO(clienteActualizado);
        } else {
            throw new RuntimeException("Cliente no encontrado con ID: " + id);
        }
    }

    @Override
    public void eliminarCliente(Long id) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(id);

        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            cliente.setEstado(false); // Eliminación lógica
            clienteRepository.save(cliente);
        } else {
            throw new RuntimeException("Cliente no encontrado con ID: " + id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeClientePorIdentificacion(String identificacion) {
        return clienteRepository.existsByIdentificacion(identificacion);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClienteDTO> obtenerClientePorClienteId(String clienteId) {
        return clienteRepository.findByClienteId(clienteId)
                .map(clienteMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteDTO> obtenerClientesActivos() {
        return clienteRepository.findByEstadoTrue()
                .stream()
                .map(clienteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ClienteDTO activarCliente(Long id) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(id);

        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            cliente.setEstado(true);
            Cliente clienteActivado = clienteRepository.save(cliente);
            return clienteMapper.toDTO(clienteActivado);
        } else {
            throw new RuntimeException("Cliente no encontrado con ID: " + id);
        }
    }



    private String generarClienteId() {
        return "CLI-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}