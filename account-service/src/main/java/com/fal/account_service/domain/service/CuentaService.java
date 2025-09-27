package com.fal.account_service.domain.service;

import com.fal.account_service.application.dto.CreateCuentaRequest;
import com.fal.account_service.application.dto.CuentaDTO;
import com.fal.account_service.application.dto.UpdateCuentaRequest;
import com.fal.account_service.domain.model.Cuenta;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CuentaService {

    CuentaDTO crearCuenta(CreateCuentaRequest request);
    Optional<CuentaDTO> obtenerCuentaPorId(Long id);
    Optional<CuentaDTO> obtenerCuentaPorNumero(String numeroCuenta);
    List<CuentaDTO> obtenerTodasLasCuentas();
    List<CuentaDTO> obtenerCuentasPorCliente(String clienteId);
    List<CuentaDTO> obtenerCuentasActivas();
    List<CuentaDTO> obtenerCuentasActivasPorCliente(String clienteId);
    CuentaDTO actualizarCuenta(Long id, UpdateCuentaRequest request);
    void eliminarCuenta(Long id);
    CuentaDTO desactivarCuenta(Long id);
    CuentaDTO activarCuenta(Long id);
    boolean existeCuentaPorNumero(String numeroCuenta);
    BigDecimal obtenerSaldoActual(Long cuentaId);
    BigDecimal obtenerSaldoActualPorNumero(String numeroCuenta);
    CuentaDTO actualizarSaldo(Long cuentaId, BigDecimal nuevoSaldo);
    boolean cuentaPerteneceAlCliente(Long cuentaId, String clienteId);
    boolean tieneSaldoSuficiente(Long cuentaId, BigDecimal monto);
    boolean cuentaEstaActiva(Long cuentaId);
    List<CuentaDTO> buscarCuentasPorTipo(String tipoCuenta);
    List<CuentaDTO> buscarCuentasPorRangoSaldo(BigDecimal saldoMin, BigDecimal saldoMax);
    long contarCuentasActivas();
    long contarCuentasPorCliente(String clienteId);
    CuentaDTO realizarDeposito(Long cuentaId, BigDecimal monto);
    CuentaDTO realizarRetiro(Long cuentaId, BigDecimal monto);
}
