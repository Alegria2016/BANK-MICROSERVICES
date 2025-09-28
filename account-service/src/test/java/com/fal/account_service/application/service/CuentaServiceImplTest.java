package com.fal.account_service.application.service;


import com.fal.account_service.application.dto.CreateCuentaRequest;
import com.fal.account_service.application.dto.CuentaDTO;
import com.fal.account_service.application.dto.UpdateCuentaRequest;
import com.fal.account_service.application.mapper.CuentaMapper;
import com.fal.account_service.domain.model.Cuenta;
import com.fal.account_service.domain.repository.CuentaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CuentaServiceImplTest {

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private CuentaMapper cuentaMapper;

    @InjectMocks
    private CuentaServiceImpl cuentaService;

    private Cuenta cuenta;
    private CuentaDTO cuentaDTO;
    private CreateCuentaRequest createRequest;
    private UpdateCuentaRequest updateRequest;

    @BeforeEach
    void setUp() {

        cuenta = new Cuenta();
        cuenta.setId(1L);
        cuenta.setNumeroCuenta("1234567890");
        cuenta.setTipoCuenta("AHORROS");
        cuenta.setSaldoInicial(new BigDecimal("1000.00"));
        cuenta.setSaldoActual(new BigDecimal("1000.00"));
        cuenta.setClienteId("CLI-123");
        cuenta.setEstado(true);


        cuentaDTO = new CuentaDTO();
        cuentaDTO.setId(1L);
        cuentaDTO.setNumeroCuenta("1234567890");
        cuentaDTO.setTipoCuenta("AHORROS");
        cuentaDTO.setSaldoInicial(new BigDecimal("1000.00"));
        cuentaDTO.setSaldoActual(new BigDecimal("1000.00"));
        cuentaDTO.setClienteId("CLI-123");
        cuentaDTO.setEstado(true);


        createRequest = new CreateCuentaRequest();
        createRequest.setNumeroCuenta("1234567890");
        createRequest.setTipoCuenta("AHORROS");
        createRequest.setSaldoInicial(new BigDecimal("1000.00"));
        createRequest.setClienteId("CLI-123");


        updateRequest = new UpdateCuentaRequest();
        updateRequest.setNumeroCuenta("1234567890");
        updateRequest.setTipoCuenta("CORRIENTE");
        updateRequest.setSaldoInicial(new BigDecimal("2000.00"));
        updateRequest.setEstado(true);
    }

    @Test
    void testCrearCuentaExitoso() {
        // Given
        when(cuentaRepository.existsByNumeroCuenta(anyString())).thenReturn(false);
        when(cuentaMapper.toEntity(any(CreateCuentaRequest.class))).thenReturn(cuenta);
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(cuenta);
        when(cuentaMapper.toDTO(any(Cuenta.class))).thenReturn(cuentaDTO);

        // When
        CuentaDTO resultado = cuentaService.crearCuenta(createRequest);

        // Then
        assertNotNull(resultado);
        assertEquals("1234567890", resultado.getNumeroCuenta());
        assertEquals("AHORROS", resultado.getTipoCuenta());
        assertEquals(new BigDecimal("1000.00"), resultado.getSaldoInicial());

        verify(cuentaRepository, times(1)).existsByNumeroCuenta(anyString());
        verify(cuentaMapper, times(1)).toEntity(any(CreateCuentaRequest.class));
        verify(cuentaRepository, times(1)).save(any(Cuenta.class));
        verify(cuentaMapper, times(1)).toDTO(any(Cuenta.class));
    }

    @Test
    void testCrearCuentaConNumeroExistente() {
        // Given
        when(cuentaRepository.existsByNumeroCuenta(anyString())).thenReturn(true);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cuentaService.crearCuenta(createRequest);
        });

        assertEquals("Ya existe una cuenta con el número: " + createRequest.getNumeroCuenta(),
                exception.getMessage());

        verify(cuentaRepository, times(1)).existsByNumeroCuenta(anyString());
        verify(cuentaMapper, never()).toEntity(any(CreateCuentaRequest.class));
        verify(cuentaRepository, never()).save(any(Cuenta.class));
    }

    @Test
    void testCrearCuentaConSaldoInicialNegativo() {
        // Given
        createRequest.setSaldoInicial(new BigDecimal("-100.00"));
        when(cuentaRepository.existsByNumeroCuenta(anyString())).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cuentaService.crearCuenta(createRequest);
        });

        assertTrue(exception.getMessage().contains("El saldo inicial para cuenta AHORROS debe ser al menos 0.00"));
        verify(cuentaRepository, times(1)).existsByNumeroCuenta(anyString());
        verify(cuentaMapper, never()).toEntity(any(CreateCuentaRequest.class));
    }



    @Test
    void testObtenerCuentaPorIdExistente() {
        // Given
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        when(cuentaMapper.toDTO(any(Cuenta.class))).thenReturn(cuentaDTO);

        // When
        Optional<CuentaDTO> resultado = cuentaService.obtenerCuentaPorId(1L);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("1234567890", resultado.get().getNumeroCuenta());
        verify(cuentaRepository, times(1)).findById(1L);
        verify(cuentaMapper, times(1)).toDTO(any(Cuenta.class));
    }

    @Test
    void testObtenerCuentaPorIdNoExistente() {
        // Given
        when(cuentaRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<CuentaDTO> resultado = cuentaService.obtenerCuentaPorId(999L);

        // Then
        assertFalse(resultado.isPresent());
        verify(cuentaRepository, times(1)).findById(999L);
        verify(cuentaMapper, never()).toDTO(any(Cuenta.class));
    }

    @Test
    void testObtenerCuentaPorNumeroExistente() {
        // Given
        when(cuentaRepository.findByNumeroCuenta("1234567890")).thenReturn(Optional.of(cuenta));
        when(cuentaMapper.toDTO(any(Cuenta.class))).thenReturn(cuentaDTO);

        // When
        Optional<CuentaDTO> resultado = cuentaService.obtenerCuentaPorNumero("1234567890");

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("1234567890", resultado.get().getNumeroCuenta());
        verify(cuentaRepository, times(1)).findByNumeroCuenta("1234567890");
        verify(cuentaMapper, times(1)).toDTO(any(Cuenta.class));
    }

    @Test
    void testObtenerTodasLasCuentas() {
        // Given
        List<Cuenta> cuentas = List.of(cuenta);
        when(cuentaRepository.findAll()).thenReturn(cuentas);
        when(cuentaMapper.toDTO(any(Cuenta.class))).thenReturn(cuentaDTO);

        // When
        List<CuentaDTO> resultados = cuentaService.obtenerTodasLasCuentas();

        // Then
        assertNotNull(resultados);
        assertEquals(1, resultados.size());
        assertEquals("1234567890", resultados.get(0).getNumeroCuenta());
        verify(cuentaRepository, times(1)).findAll();
        verify(cuentaMapper, times(1)).toDTO(any(Cuenta.class));
    }

    @Test
    void testObtenerCuentasPorCliente() {
        // Given
        List<Cuenta> cuentas = List.of(cuenta);
        when(cuentaRepository.findByClienteId("CLI-123")).thenReturn(cuentas);
        when(cuentaMapper.toDTO(any(Cuenta.class))).thenReturn(cuentaDTO);

        // When
        List<CuentaDTO> resultados = cuentaService.obtenerCuentasPorCliente("CLI-123");

        // Then
        assertNotNull(resultados);
        assertEquals(1, resultados.size());
        assertEquals("CLI-123", resultados.get(0).getClienteId());
        verify(cuentaRepository, times(1)).findByClienteId("CLI-123");
        verify(cuentaMapper, times(1)).toDTO(any(Cuenta.class));
    }

    @Test
    void testObtenerCuentasActivas() {
        // Given
        List<Cuenta> cuentasActivas = List.of(cuenta);
        when(cuentaRepository.findByEstadoTrue()).thenReturn(cuentasActivas);
        when(cuentaMapper.toDTO(any(Cuenta.class))).thenReturn(cuentaDTO);

        // When
        List<CuentaDTO> resultados = cuentaService.obtenerCuentasActivas();

        // Then
        assertNotNull(resultados);
        assertEquals(1, resultados.size());
        assertTrue(resultados.get(0).getEstado());
        verify(cuentaRepository, times(1)).findByEstadoTrue();
        verify(cuentaMapper, times(1)).toDTO(any(Cuenta.class));
    }




    @Test
    void testEliminarCuentaExitoso() {
        // Given
        cuenta.setSaldoActual(BigDecimal.ZERO);
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(cuenta);

        // When
        cuentaService.eliminarCuenta(1L);

        // Then
        assertFalse(cuenta.getEstado());
        verify(cuentaRepository, times(1)).findById(1L);
        verify(cuentaRepository, times(1)).save(cuenta);
    }

    @Test
    void testEliminarCuentaConSaldoNoCero() {
        // Given
        cuenta.setSaldoActual(new BigDecimal("500.00"));
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cuentaService.eliminarCuenta(1L);
        });

        assertTrue(exception.getMessage().contains("No se puede eliminar una cuenta con saldo diferente de cero"));
        verify(cuentaRepository, times(1)).findById(1L);
        verify(cuentaRepository, never()).save(any(Cuenta.class));
    }

    @Test
    void testDesactivarCuenta() {
        // Given
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(cuenta);
        when(cuentaMapper.toDTO(any(Cuenta.class))).thenReturn(cuentaDTO);

        // When
        CuentaDTO resultado = cuentaService.desactivarCuenta(1L);

        // Then
        assertNotNull(resultado);
        assertFalse(cuenta.getEstado());
        verify(cuentaRepository, times(1)).findById(1L);
        verify(cuentaRepository, times(1)).save(cuenta);
    }

    @Test
    void testActivarCuenta() {
        // Given
        cuenta.setEstado(false);
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(cuenta);
        when(cuentaMapper.toDTO(any(Cuenta.class))).thenReturn(cuentaDTO);

        // When
        CuentaDTO resultado = cuentaService.activarCuenta(1L);

        // Then
        assertNotNull(resultado);
        assertTrue(cuenta.getEstado());
        verify(cuentaRepository, times(1)).findById(1L);
        verify(cuentaRepository, times(1)).save(cuenta);
    }

    @Test
    void testObtenerSaldoActual() {
        // Given
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));

        // When
        BigDecimal saldo = cuentaService.obtenerSaldoActual(1L);

        // Then
        assertEquals(new BigDecimal("1000.00"), saldo);
        verify(cuentaRepository, times(1)).findById(1L);
    }

    @Test
    void testActualizarSaldoExitoso() {
        // Given
        Cuenta cuentaActualizada = new Cuenta();
        cuentaActualizada.setId(1L);
        cuentaActualizada.setSaldoActual(new BigDecimal("1500.00"));

        CuentaDTO cuentaActualizadaDTO = new CuentaDTO();
        cuentaActualizadaDTO.setSaldoActual(new BigDecimal("1500.00"));

        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(cuentaActualizada);
        when(cuentaMapper.toDTO(any(Cuenta.class))).thenReturn(cuentaActualizadaDTO);

        // When
        CuentaDTO resultado = cuentaService.actualizarSaldo(1L, new BigDecimal("1500.00"));

        // Then
        assertNotNull(resultado);
        assertEquals(new BigDecimal("1500.00"), resultado.getSaldoActual());
        verify(cuentaRepository, times(1)).findById(1L);
        verify(cuentaRepository, times(1)).save(any(Cuenta.class));
    }

    @Test
    void testActualizarSaldoCuentaInactiva() {
        // Given
        cuenta.setEstado(false);
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cuentaService.actualizarSaldo(1L, new BigDecimal("1500.00"));
        });

        assertTrue(exception.getMessage().contains("No se puede actualizar el saldo de una cuenta inactiva"));
        verify(cuentaRepository, times(1)).findById(1L);
        verify(cuentaRepository, never()).save(any(Cuenta.class));
    }

    @Test
    void testRealizarDepositoExitoso() {
        // Given
        Cuenta cuentaActualizada = new Cuenta();
        cuentaActualizada.setId(1L);
        cuentaActualizada.setSaldoActual(new BigDecimal("1500.00"));

        CuentaDTO cuentaActualizadaDTO = new CuentaDTO();
        cuentaActualizadaDTO.setSaldoActual(new BigDecimal("1500.00"));

        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(cuentaActualizada);
        when(cuentaMapper.toDTO(any(Cuenta.class))).thenReturn(cuentaActualizadaDTO);

        // When
        CuentaDTO resultado = cuentaService.realizarDeposito(1L, new BigDecimal("500.00"));

        // Then
        assertNotNull(resultado);
        assertEquals(new BigDecimal("1500.00"), resultado.getSaldoActual());
        verify(cuentaRepository, times(1)).findById(1L);
        verify(cuentaRepository, times(1)).save(any(Cuenta.class));
    }

    @Test
    void testRealizarRetiroExitoso() {
        // Given
        Cuenta cuentaActualizada = new Cuenta();
        cuentaActualizada.setId(1L);
        cuentaActualizada.setSaldoActual(new BigDecimal("500.00"));

        CuentaDTO cuentaActualizadaDTO = new CuentaDTO();
        cuentaActualizadaDTO.setSaldoActual(new BigDecimal("500.00"));

        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(cuentaActualizada);
        when(cuentaMapper.toDTO(any(Cuenta.class))).thenReturn(cuentaActualizadaDTO);

        // When
        CuentaDTO resultado = cuentaService.realizarRetiro(1L, new BigDecimal("500.00"));

        // Then
        assertNotNull(resultado);
        assertEquals(new BigDecimal("500.00"), resultado.getSaldoActual());
        verify(cuentaRepository, times(1)).findById(1L);
        verify(cuentaRepository, times(1)).save(any(Cuenta.class));
    }

    @Test
    void testRealizarRetiroSaldoInsuficiente() {
        // Given
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cuentaService.realizarRetiro(1L, new BigDecimal("1500.00"));
        });

        assertTrue(exception.getMessage().contains("Saldo no disponible para realizar la operación"));
        verify(cuentaRepository, times(1)).findById(1L);
        verify(cuentaRepository, never()).save(any(Cuenta.class));
    }

    @Test
    void testTieneSaldoSuficiente() {
        // Given
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));

        // When
        boolean tieneSaldo = cuentaService.tieneSaldoSuficiente(1L, new BigDecimal("500.00"));

        // Then
        assertTrue(tieneSaldo);
        verify(cuentaRepository, times(1)).findById(1L);
    }

    @Test
    void testCuentaEstaActiva() {
        // Given
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));

        // When
        boolean estaActiva = cuentaService.cuentaEstaActiva(1L);

        // Then
        assertTrue(estaActiva);
        verify(cuentaRepository, times(1)).findById(1L);
    }

    @Test
    void testBuscarCuentasPorTipo() {
        // Given
        List<Cuenta> cuentas = List.of(cuenta);
        when(cuentaRepository.findByTipoCuentaIgnoreCase("AHORROS")).thenReturn(cuentas);
        when(cuentaMapper.toDTO(any(Cuenta.class))).thenReturn(cuentaDTO);

        // When
        List<CuentaDTO> resultados = cuentaService.buscarCuentasPorTipo("AHORROS");

        // Then
        assertNotNull(resultados);
        assertEquals(1, resultados.size());
        assertEquals("AHORROS", resultados.get(0).getTipoCuenta());
        verify(cuentaRepository, times(1)).findByTipoCuentaIgnoreCase("AHORROS");
    }

    @Test
    void testContarCuentasActivas() {
        // Given
        when(cuentaRepository.countByEstadoTrue()).thenReturn(5L);

        // When
        long count = cuentaService.contarCuentasActivas();

        // Then
        assertEquals(5L, count);
        verify(cuentaRepository, times(1)).countByEstadoTrue();
    }
}