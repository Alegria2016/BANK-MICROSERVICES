package com.fal.client_service.application.service;

import com.fal.client_service.application.dto.ClienteDTO;
import com.fal.client_service.application.dto.CreateClienteRequest;
import com.fal.client_service.application.dto.UpdateClienteRequest;
import com.fal.client_service.application.mapper.ClienteMapper;
import com.fal.client_service.domain.model.Cliente;
import com.fal.client_service.infrastructure.persistence.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ClienteMapper clienteMapper;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private Cliente cliente;
    private ClienteDTO clienteDTO;
    private CreateClienteRequest createRequest;
    private UpdateClienteRequest updateRequest;

    @BeforeEach
    void setUp() {
        // Configurar entidad Cliente
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Jose Lema");
        cliente.setGenero("Masculino");
        cliente.setEdad(35);
        cliente.setIdentificacion("1234567890");
        cliente.setDireccion("Otavalo sn y principal");
        cliente.setTelefono("098254785");
        cliente.setPassword("password123");
        cliente.setClienteId("CLI-ABC123");
        cliente.setEstado(true);
        cliente.setFechaCreacion(LocalDateTime.now());

        // Configurar ClienteDTO
        clienteDTO = new ClienteDTO();
        clienteDTO.setId(1L);
        clienteDTO.setNombre("Jose Lema");
        clienteDTO.setGenero("Masculino");
        clienteDTO.setEdad(35);
        clienteDTO.setIdentificacion("1234567890");
        clienteDTO.setDireccion("Otavalo sn y principal");
        clienteDTO.setTelefono("098254785");
        clienteDTO.setClienteId("CLI-ABC123");
        clienteDTO.setEstado(true);
        clienteDTO.setFechaCreacion(LocalDateTime.now());

        // Configurar CreateClienteRequest
        createRequest = new CreateClienteRequest();
        createRequest.setNombre("Jose Lema");
        createRequest.setGenero("Masculino");
        createRequest.setEdad(35);
        createRequest.setIdentificacion("1234567890");
        createRequest.setDireccion("Otavalo sn y principal");
        createRequest.setTelefono("098254785");
        createRequest.setPassword("password123");

        // Configurar UpdateClienteRequest
        updateRequest = new UpdateClienteRequest();
        updateRequest.setNombre("Jose Lema Actualizado");
        updateRequest.setGenero("Masculino");
        updateRequest.setEdad(36);
        updateRequest.setIdentificacion("1234567890");
        updateRequest.setDireccion("Nueva dirección");
        updateRequest.setTelefono("099999999");
        updateRequest.setPassword("newpassword123");
        updateRequest.setEstado(true);
    }

    @Test
    void testCrearClienteExitoso() {
        // Given
        when(clienteRepository.existsByIdentificacion(anyString())).thenReturn(false);
        when(clienteMapper.toEntity(any(CreateClienteRequest.class))).thenReturn(cliente);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        when(clienteMapper.toDTO(any(Cliente.class))).thenReturn(clienteDTO);

        // When
        ClienteDTO resultado = clienteService.crearCliente(createRequest);

        // Then
        assertNotNull(resultado);
        assertEquals("Jose Lema", resultado.getNombre());
        assertEquals("1234567890", resultado.getIdentificacion());
        assertTrue(resultado.getEstado());

        verify(clienteRepository, times(1)).existsByIdentificacion(anyString());
        verify(clienteMapper, times(1)).toEntity(any(CreateClienteRequest.class));
        verify(clienteRepository, times(1)).save(any(Cliente.class));
        verify(clienteMapper, times(1)).toDTO(any(Cliente.class));
    }

    @Test
    void testCrearClienteConIdentificacionExistente() {
        // Given
        when(clienteRepository.existsByIdentificacion(anyString())).thenReturn(true);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            clienteService.crearCliente(createRequest);
        });

        verify(clienteRepository, times(1)).existsByIdentificacion(anyString());
        verify(clienteMapper, never()).toEntity(any(CreateClienteRequest.class));
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void testObtenerClientePorIdExistente() {
        // Given
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteMapper.toDTO(any(Cliente.class))).thenReturn(clienteDTO);

        // When
        Optional<ClienteDTO> resultado = clienteService.obtenerClientePorId(1L);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("Jose Lema", resultado.get().getNombre());
        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteMapper, times(1)).toDTO(any(Cliente.class));
    }

    @Test
    void testObtenerClientePorIdNoExistente() {
        // Given
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<ClienteDTO> resultado = clienteService.obtenerClientePorId(999L);

        // Then
        assertFalse(resultado.isPresent());
        verify(clienteRepository, times(1)).findById(999L);
        verify(clienteMapper, never()).toDTO(any(Cliente.class));
    }

    @Test
    void testEliminarClienteExitoso() {
        // Given
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        // When
        clienteService.eliminarCliente(1L);

        // Then
        assertFalse(cliente.getEstado()); // Verificar eliminación lógica
        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    void testEliminarClienteNoExistente() {
        // Given
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            clienteService.eliminarCliente(999L);
        });

        verify(clienteRepository, times(1)).findById(999L);
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void testActualizarClienteExitoso() {
        // Given
        Cliente clienteActualizado = new Cliente();
        clienteActualizado.setId(1L);
        clienteActualizado.setNombre("Jose Lema Actualizado");
        clienteActualizado.setIdentificacion("1234567890");
        clienteActualizado.setEstado(true);

        ClienteDTO clienteActualizadoDTO = new ClienteDTO();
        clienteActualizadoDTO.setId(1L);
        clienteActualizadoDTO.setNombre("Jose Lema Actualizado");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.existsByIdentificacion(anyString())).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteActualizado);
        when(clienteMapper.toDTO(any(Cliente.class))).thenReturn(clienteActualizadoDTO);

        // When
        ClienteDTO resultado = clienteService.actualizarCliente(1L, updateRequest);

        // Then
        assertNotNull(resultado);
        assertEquals("Jose Lema Actualizado", resultado.getNombre());

        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteRepository, times(1)).existsByIdentificacion(anyString());
        verify(clienteMapper, times(1)).updateEntityFromRequest(any(UpdateClienteRequest.class), any(Cliente.class));
        verify(clienteRepository, times(1)).save(any(Cliente.class));
        verify(clienteMapper, times(1)).toDTO(any(Cliente.class));
    }

    @Test
    void testActualizarClienteConIdentificacionExistente() {
        // Given
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.existsByIdentificacion(anyString())).thenReturn(true);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            clienteService.actualizarCliente(1L, updateRequest);
        });

        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteRepository, times(1)).existsByIdentificacion(anyString());
        verify(clienteMapper, never()).updateEntityFromRequest(any(UpdateClienteRequest.class), any(Cliente.class));
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void testObtenerClientePorIdentificacion() {
        // Given
        when(clienteRepository.findByIdentificacion("1234567890")).thenReturn(Optional.of(cliente));
        when(clienteMapper.toDTO(any(Cliente.class))).thenReturn(clienteDTO);

        // When
        Optional<ClienteDTO> resultado = clienteService.obtenerClientePorIdentificacion("1234567890");

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("1234567890", resultado.get().getIdentificacion());
        verify(clienteRepository, times(1)).findByIdentificacion("1234567890");
        verify(clienteMapper, times(1)).toDTO(any(Cliente.class));
    }

    @Test
    void testObtenerTodosLosClientes() {
        // Given
        List<Cliente> clientes = List.of(cliente);
        when(clienteRepository.findAll()).thenReturn(clientes);
        when(clienteMapper.toDTO(any(Cliente.class))).thenReturn(clienteDTO);

        // When
        List<ClienteDTO> resultados = clienteService.obtenerTodosLosClientes();

        // Then
        assertNotNull(resultados);
        assertEquals(1, resultados.size());
        assertEquals("Jose Lema", resultados.get(0).getNombre());
        verify(clienteRepository, times(1)).findAll();
        verify(clienteMapper, times(1)).toDTO(any(Cliente.class));
    }

    @Test
    void testExisteClientePorIdentificacion() {
        // Given
        when(clienteRepository.existsByIdentificacion("1234567890")).thenReturn(true);

        // When
        boolean existe = clienteService.existeClientePorIdentificacion("1234567890");

        // Then
        assertTrue(existe);
        verify(clienteRepository, times(1)).existsByIdentificacion("1234567890");
    }

    @Test
    void testActivarCliente() {
        // Given
        cliente.setEstado(false); // Cliente inactivo
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        when(clienteMapper.toDTO(any(Cliente.class))).thenReturn(clienteDTO);

        // When
        ClienteDTO resultado = clienteService.activarCliente(1L);

        // Then
        assertNotNull(resultado);
        assertTrue(cliente.getEstado()); // Verificar que se activó
        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteRepository, times(1)).save(cliente);
        verify(clienteMapper, times(1)).toDTO(any(Cliente.class));
    }

    @Test
    void testObtenerClientesActivos() {
        // Given
        List<Cliente> clientesActivos = List.of(cliente);
        when(clienteRepository.findByEstadoTrue()).thenReturn(clientesActivos);
        when(clienteMapper.toDTO(any(Cliente.class))).thenReturn(clienteDTO);

        // When
        List<ClienteDTO> resultados = clienteService.obtenerClientesActivos();

        // Then
        assertNotNull(resultados);
        assertEquals(1, resultados.size());
        assertTrue(resultados.get(0).getEstado());
        verify(clienteRepository, times(1)).findByEstadoTrue();
        verify(clienteMapper, times(1)).toDTO(any(Cliente.class));
    }
}
