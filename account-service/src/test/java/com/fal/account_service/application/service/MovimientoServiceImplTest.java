package com.fal.account_service.application.service;


import com.fal.account_service.application.dto.*;
import com.fal.account_service.application.mapper.MovimientoMapper;
import com.fal.account_service.domain.model.Cuenta;
import com.fal.account_service.domain.model.Movimiento;
import com.fal.account_service.domain.repository.CuentaRepository;
import com.fal.account_service.domain.repository.MovimientoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovimientoServiceImplTest {

    @Mock
    private MovimientoRepository movimientoRepository;

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private MovimientoMapper movimientoMapper;

    @InjectMocks
    private MovimientoServiceImpl movimientoService;

    private Cuenta cuenta;
    private Movimiento movimiento;
    private MovimientoDTO movimientoDTO;
    private CreateMovimientoRequest createRequest;

    @BeforeEach
    void setUp() {
        // Configurar Cuenta
        cuenta = new Cuenta();
        cuenta.setId(1L);
        cuenta.setNumeroCuenta("1234567890");
        cuenta.setSaldoActual(new BigDecimal("1000.00"));
        cuenta.setEstado(true);
        cuenta.setClienteId("CLI-123");

        // Configurar Movimiento
        movimiento = new Movimiento();
        movimiento.setId(1L);
        movimiento.setTipoMovimiento("DEPOSITO");
        movimiento.setValor(new BigDecimal("500.00"));
        movimiento.setSaldo(new BigDecimal("1500.00"));
        movimiento.setCuenta(cuenta);
        movimiento.setFecha(LocalDateTime.now());

        // Configurar MovimientoDTO
        movimientoDTO = new MovimientoDTO();
        movimientoDTO.setId(1L);
        movimientoDTO.setTipoMovimiento("DEPOSITO");
        movimientoDTO.setValor(new BigDecimal("500.00"));
        movimientoDTO.setSaldo(new BigDecimal("1500.00"));
        movimientoDTO.setCuentaId(1L);

        // Configurar CreateMovimientoRequest
        createRequest = new CreateMovimientoRequest();
        createRequest.setCuentaId(1L);
        createRequest.setTipoMovimiento("DEPOSITO");
        createRequest.setValor(new BigDecimal("500.00"));
        createRequest.setDescripcion("Depósito de prueba");
    }

    @Test
    void testRealizarMovimientoDepositoExitoso() {
        // Given
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        when(movimientoMapper.toEntity(any(CreateMovimientoRequest.class), any(Cuenta.class), any(BigDecimal.class)))
                .thenReturn(movimiento);
        when(movimientoRepository.save(any(Movimiento.class))).thenReturn(movimiento);
        when(movimientoMapper.toDTO(any(Movimiento.class))).thenReturn(movimientoDTO);

        // When
        MovimientoDTO resultado = movimientoService.realizarMovimiento(createRequest);

        // Then
        assertNotNull(resultado);
        assertEquals("DEPOSITO", resultado.getTipoMovimiento());
        assertEquals(new BigDecimal("500.00"), resultado.getValor());

        verify(cuentaRepository, times(1)).findById(1L);
        verify(cuentaRepository, times(1)).save(cuenta);
        verify(movimientoRepository, times(1)).save(movimiento);
        verify(movimientoMapper, times(1)).toDTO(movimiento);
    }

    @Test
    void testRealizarMovimientoRetiroExitoso() {
        // Given
        createRequest.setTipoMovimiento("RETIRO");
        createRequest.setValor(new BigDecimal("300.00"));

        movimiento.setTipoMovimiento("RETIRO");
        movimiento.setValor(new BigDecimal("300.00"));
        movimiento.setSaldo(new BigDecimal("700.00"));

        movimientoDTO.setTipoMovimiento("RETIRO");
        movimientoDTO.setValor(new BigDecimal("300.00"));
        movimientoDTO.setSaldo(new BigDecimal("700.00"));

        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        when(movimientoMapper.toEntity(any(CreateMovimientoRequest.class), any(Cuenta.class), any(BigDecimal.class)))
                .thenReturn(movimiento);
        when(movimientoRepository.save(any(Movimiento.class))).thenReturn(movimiento);
        when(movimientoMapper.toDTO(any(Movimiento.class))).thenReturn(movimientoDTO);

        // When
        MovimientoDTO resultado = movimientoService.realizarMovimiento(createRequest);

        // Then
        assertNotNull(resultado);
        assertEquals("RETIRO", resultado.getTipoMovimiento());
        assertEquals(new BigDecimal("300.00"), resultado.getValor());

        verify(cuentaRepository, times(1)).findById(1L);
        verify(cuentaRepository, times(1)).save(cuenta);
        verify(movimientoRepository, times(1)).save(movimiento);
    }

    @Test
    void testRealizarMovimientoCuentaNoEncontrada() {
        // Given
        when(cuentaRepository.findById(999L)).thenReturn(Optional.empty());
        createRequest.setCuentaId(999L);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            movimientoService.realizarMovimiento(createRequest);
        });

        assertEquals("Cuenta no encontrada con ID: 999", exception.getMessage());
        verify(cuentaRepository, times(1)).findById(999L);
        verify(cuentaRepository, never()).save(any(Cuenta.class));
        verify(movimientoRepository, never()).save(any(Movimiento.class));
    }

    @Test
    void testRealizarMovimientoCuentaInactiva() {
        // Given
        cuenta.setEstado(false);
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            movimientoService.realizarMovimiento(createRequest);
        });

        assertEquals("La cuenta está inactiva", exception.getMessage());
        verify(cuentaRepository, times(1)).findById(1L);
        verify(cuentaRepository, never()).save(any(Cuenta.class));
        verify(movimientoRepository, never()).save(any(Movimiento.class));
    }

    @Test
    void testRealizarMovimientoSaldoInsuficiente() {
        // Given
        createRequest.setTipoMovimiento("RETIRO");
        createRequest.setValor(new BigDecimal("1500.00")); // Más que el saldo actual

        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            movimientoService.realizarMovimiento(createRequest);
        });

        assertEquals("Saldo no disponible", exception.getMessage());
        verify(cuentaRepository, times(1)).findById(1L);
        verify(cuentaRepository, never()).save(any(Cuenta.class));
        verify(movimientoRepository, never()).save(any(Movimiento.class));
    }

    @Test
    void testRealizarDepositoConveniencia() {
        // Given
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        when(movimientoMapper.toEntity(any(CreateMovimientoRequest.class), any(Cuenta.class), any(BigDecimal.class)))
                .thenReturn(movimiento);
        when(movimientoRepository.save(any(Movimiento.class))).thenReturn(movimiento);
        when(movimientoMapper.toDTO(any(Movimiento.class))).thenReturn(movimientoDTO);

        // When
        MovimientoDTO resultado = movimientoService.realizarDeposito(1L, new BigDecimal("500.00"), "Depósito automático");

        // Then
        assertNotNull(resultado);
        assertEquals("DEPOSITO", resultado.getTipoMovimiento());
        verify(cuentaRepository, times(1)).findById(1L);
        verify(movimientoRepository, times(1)).save(any(Movimiento.class));
    }

    @Test
    void testRealizarRetiroConveniencia() {
        // Given
        movimiento.setTipoMovimiento("RETIRO");
        movimiento.setValor(new BigDecimal("300.00"));
        movimiento.setSaldo(new BigDecimal("700.00"));

        movimientoDTO.setTipoMovimiento("RETIRO");
        movimientoDTO.setValor(new BigDecimal("300.00"));
        movimientoDTO.setSaldo(new BigDecimal("700.00"));

        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        when(movimientoMapper.toEntity(any(CreateMovimientoRequest.class), any(Cuenta.class), any(BigDecimal.class)))
                .thenReturn(movimiento);
        when(movimientoRepository.save(any(Movimiento.class))).thenReturn(movimiento);
        when(movimientoMapper.toDTO(any(Movimiento.class))).thenReturn(movimientoDTO);

        // When
        MovimientoDTO resultado = movimientoService.realizarRetiro(1L, new BigDecimal("300.00"), "Retiro automático");

        // Then
        assertNotNull(resultado);
        assertEquals("RETIRO", resultado.getTipoMovimiento());
        verify(cuentaRepository, times(1)).findById(1L);
        verify(movimientoRepository, times(1)).save(any(Movimiento.class));
    }

    @Test
    void testObtenerMovimientoPorIdExistente() {
        // Given
        when(movimientoRepository.findById(1L)).thenReturn(Optional.of(movimiento));
        when(movimientoMapper.toDTO(any(Movimiento.class))).thenReturn(movimientoDTO);

        // When
        MovimientoDTO resultado = movimientoService.obtenerMovimientoPorId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(movimientoRepository, times(1)).findById(1L);
        verify(movimientoMapper, times(1)).toDTO(movimiento);
    }

    @Test
    void testObtenerMovimientoPorIdNoExistente() {
        // Given
        when(movimientoRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            movimientoService.obtenerMovimientoPorId(999L);
        });

        assertEquals("Movimiento no encontrado con ID: 999", exception.getMessage());
        verify(movimientoRepository, times(1)).findById(999L);
        verify(movimientoMapper, never()).toDTO(any(Movimiento.class));
    }

    @Test
    void testObtenerMovimientosPorCuenta() {
        // Given
        List<Movimiento> movimientos = List.of(movimiento);
        when(cuentaRepository.existsById(1L)).thenReturn(true);
        when(movimientoRepository.findByCuentaId(1L)).thenReturn(movimientos);
        when(movimientoMapper.toDTO(any(Movimiento.class))).thenReturn(movimientoDTO);

        // When
        List<MovimientoDTO> resultados = movimientoService.obtenerMovimientosPorCuenta(1L);

        // Then
        assertNotNull(resultados);
        assertEquals(1, resultados.size());
        verify(cuentaRepository, times(1)).existsById(1L);
        verify(movimientoRepository, times(1)).findByCuentaId(1L);
        verify(movimientoMapper, times(1)).toDTO(movimiento);
    }

    @Test
    void testObtenerMovimientosPorCuentaNoExistente() {
        // Given
        when(cuentaRepository.existsById(999L)).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            movimientoService.obtenerMovimientosPorCuenta(999L);
        });

        assertEquals("Cuenta no encontrada con ID: 999", exception.getMessage());
        verify(cuentaRepository, times(1)).existsById(999L);
        verify(movimientoRepository, never()).findByCuentaId(anyLong());
    }

    @Test
    void testObtenerResumenMovimientos() {
        // Given
        LocalDate fechaInicio = LocalDate.now().minusDays(30);
        LocalDate fechaFin = LocalDate.now();

        Movimiento deposito = new Movimiento();
        deposito.setTipoMovimiento("DEPOSITO");
        deposito.setValor(new BigDecimal("1000.00"));

        Movimiento retiro = new Movimiento();
        retiro.setTipoMovimiento("RETIRO");
        retiro.setValor(new BigDecimal("300.00"));

        List<Movimiento> movimientos = List.of(deposito, retiro);

        when(movimientoRepository.findByClienteIdAndFechaBetween(anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(movimientos);

        // When
        MovimientoResumenResponse resumen = movimientoService.obtenerResumenMovimientos("CLI-123", fechaInicio, fechaFin);

        // Then
        assertNotNull(resumen);
        assertEquals("CLI-123", resumen.getClienteId());
        assertEquals(2, resumen.getTotalMovimientos());
        assertEquals(new BigDecimal("1000.00"), resumen.getTotalDepositos());
        assertEquals(new BigDecimal("300.00"), resumen.getTotalRetiros());
        assertEquals(new BigDecimal("700.00"), resumen.getSaldoNeto());
        assertTrue(resumen.getPeriodo().contains(fechaInicio.toString()));
    }

    @Test
    void testActualizarMovimientoSoloCamposPermitidos() {
        // Given
        UpdateMovimientoRequest updateRequest = new UpdateMovimientoRequest();
        updateRequest.setDescripcion("Nueva descripción");

        when(movimientoRepository.findById(1L)).thenReturn(Optional.of(movimiento));
        doNothing().when(movimientoMapper).updateEntityFromRequest(any(UpdateMovimientoRequest.class), any(Movimiento.class));
        when(movimientoRepository.save(any(Movimiento.class))).thenReturn(movimiento);
        when(movimientoMapper.toDTO(any(Movimiento.class))).thenReturn(movimientoDTO);

        // When
        MovimientoDTO resultado = movimientoService.actualizarMovimiento(1L, updateRequest);

        // Then
        assertNotNull(resultado);
        verify(movimientoRepository, times(1)).findById(1L);
        verify(movimientoMapper, times(1)).updateEntityFromRequest(updateRequest, movimiento);
        verify(movimientoRepository, times(1)).save(movimiento);
    }

    @Test
    void testActualizarMovimientoValorNoPermitido() {
        // Given
        UpdateMovimientoRequest updateRequest = new UpdateMovimientoRequest();
        updateRequest.setValor(new BigDecimal("1000.00")); // No permitido

        when(movimientoRepository.findById(1L)).thenReturn(Optional.of(movimiento));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            movimientoService.actualizarMovimiento(1L, updateRequest);
        });

        assertEquals("No se puede modificar el valor o tipo de movimiento por integridad contable",
                exception.getMessage());
        verify(movimientoRepository, times(1)).findById(1L);
        verify(movimientoMapper, never()).updateEntityFromRequest(any(UpdateMovimientoRequest.class), any(Movimiento.class));
        verify(movimientoRepository, never()).save(any(Movimiento.class));
    }

    @Test
    void testEliminarMovimientoNoPermitido() {
        // Given
        when(movimientoRepository.findById(1L)).thenReturn(Optional.of(movimiento));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            movimientoService.eliminarMovimiento(1L);
        });

        assertEquals("No se permite eliminar movimientos por integridad contable. Use reverso de movimiento.",
                exception.getMessage());
        verify(movimientoRepository, times(1)).findById(1L);
        verify(movimientoRepository, never()).delete(any(Movimiento.class));
    }
}
