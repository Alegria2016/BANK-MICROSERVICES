package com.fal.account_service.application.service;


import com.fal.account_service.application.dto.CreateCuentaRequest;
import com.fal.account_service.application.dto.CuentaDTO;
import com.fal.account_service.application.dto.UpdateCuentaRequest;
import com.fal.account_service.application.mapper.CuentaMapper;
import com.fal.account_service.domain.model.Cuenta;
import com.fal.account_service.domain.repository.CuentaRepository;
import com.fal.account_service.domain.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CuentaServiceImpl implements CuentaService {

    private final CuentaRepository cuentaRepository;
    private final CuentaMapper cuentaMapper;

    @Autowired
    public CuentaServiceImpl(CuentaRepository cuentaRepository, CuentaMapper cuentaMapper) {
        this.cuentaRepository = cuentaRepository;
        this.cuentaMapper = cuentaMapper;
    }

    @Override
    public CuentaDTO crearCuenta(CreateCuentaRequest request) {
        // Validar que el número de cuenta no exista
        if (cuentaRepository.existsByNumeroCuenta(request.getNumeroCuenta())) {
            throw new RuntimeException("Ya existe una cuenta con el número: " + request.getNumeroCuenta());
        }

        // Validar saldo mínimo según el tipo de cuenta
        validarSaldoMinimo(request);

        // Validar límites de saldo inicial
        validarLimitesSaldoInicial(request);

        // Crear la entidad Cuenta
        Cuenta cuenta = cuentaMapper.toEntity(request);

        // Guardar la cuenta
        Cuenta cuentaGuardada = cuentaRepository.save(cuenta);

        return cuentaMapper.toDTO(cuentaGuardada);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CuentaDTO> obtenerCuentaPorId(Long id) {
        return cuentaRepository.findById(id)
                .map(cuentaMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CuentaDTO> obtenerCuentaPorNumero(String numeroCuenta) {
        return cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .map(cuentaMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CuentaDTO> obtenerTodasLasCuentas() {
        return cuentaRepository.findAll()
                .stream()
                .map(cuentaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CuentaDTO> obtenerCuentasPorCliente(String clienteId) {
        return cuentaRepository.findByClienteId(clienteId)
                .stream()
                .map(cuentaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CuentaDTO> obtenerCuentasActivas() {
        return cuentaRepository.findByEstadoTrue()
                .stream()
                .map(cuentaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CuentaDTO> obtenerCuentasActivasPorCliente(String clienteId) {
        return cuentaRepository.findByClienteIdAndEstadoTrue(clienteId)
                .stream()
                .map(cuentaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CuentaDTO actualizarCuenta(Long id, UpdateCuentaRequest request) {
        Optional<Cuenta> cuentaExistenteOpt = cuentaRepository.findById(id);

        if (cuentaExistenteOpt.isPresent()) {
            Cuenta cuentaExistente = cuentaExistenteOpt.get();


            if (request.hasNumeroCuenta() &&
                    !cuentaExistente.getNumeroCuenta().equals(request.getNumeroCuenta()) &&
                    cuentaRepository.existsByNumeroCuenta(request.getNumeroCuenta())) {
                throw new RuntimeException("Ya existe otra cuenta con el número: " + request.getNumeroCuenta());
            }

            // Validar saldo mínimo si se actualiza el tipo de cuenta y saldo inicial
            if (request.hasTipoCuenta() && request.hasSaldoInicial()) {
               validarSaldoMinimo(request.getTipoCuenta(), request.getSaldoInicial());
            }

            // Actualizar la cuenta
            cuentaMapper.updateEntityFromRequest(request, cuentaExistente);

            Cuenta cuentaActualizada = cuentaRepository.save(cuentaExistente);
            return cuentaMapper.toDTO(cuentaActualizada);
        } else {
            throw new RuntimeException("Cuenta no encontrada con ID: " + id);
        }
    }

    @Override
    public void eliminarCuenta(Long id) {
        Optional<Cuenta> cuentaOpt = cuentaRepository.findById(id);

        if (cuentaOpt.isPresent()) {
            Cuenta cuenta = cuentaOpt.get();

            // Verificar que la cuenta no tenga saldo antes de desactivar
            if (cuenta.getSaldoActual().compareTo(BigDecimal.ZERO) != 0) {
                throw new RuntimeException("No se puede eliminar una cuenta con saldo diferente de cero. Saldo actual: " + cuenta.getSaldoActual());
            }

            // Eliminación lógica
            cuenta.setEstado(false);
            cuentaRepository.save(cuenta);
        } else {
            throw new RuntimeException("Cuenta no encontrada con ID: " + id);
        }
    }

    @Override
    public CuentaDTO desactivarCuenta(Long id) {
        Optional<Cuenta> cuentaOpt = cuentaRepository.findById(id);

        if (cuentaOpt.isPresent()) {
            Cuenta cuenta = cuentaOpt.get();
            cuenta.setEstado(false);
            Cuenta cuentaDesactivada = cuentaRepository.save(cuenta);
            return cuentaMapper.toDTO(cuentaDesactivada);
        } else {
            throw new RuntimeException("Cuenta no encontrada con ID: " + id);
        }
    }

    @Override
    public CuentaDTO activarCuenta(Long id) {
        Optional<Cuenta> cuentaOpt = cuentaRepository.findById(id);

        if (cuentaOpt.isPresent()) {
            Cuenta cuenta = cuentaOpt.get();
            cuenta.setEstado(true);
            Cuenta cuentaActivada = cuentaRepository.save(cuenta);
            return cuentaMapper.toDTO(cuentaActivada);
        } else {
            throw new RuntimeException("Cuenta no encontrada con ID: " + id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeCuentaPorNumero(String numeroCuenta) {
        return cuentaRepository.existsByNumeroCuenta(numeroCuenta);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal obtenerSaldoActual(Long cuentaId) {
        Optional<Cuenta> cuentaOpt = cuentaRepository.findById(cuentaId);

        if (cuentaOpt.isPresent()) {
            return cuentaOpt.get().getSaldoActual();
        } else {
            throw new RuntimeException("Cuenta no encontrada con ID: " + cuentaId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal obtenerSaldoActualPorNumero(String numeroCuenta) {
        Optional<Cuenta> cuentaOpt = cuentaRepository.findByNumeroCuenta(numeroCuenta);

        if (cuentaOpt.isPresent()) {
            return cuentaOpt.get().getSaldoActual();
        } else {
            throw new RuntimeException("Cuenta no encontrada con número: " + numeroCuenta);
        }
    }

    @Override
    public CuentaDTO actualizarSaldo(Long cuentaId, BigDecimal nuevoSaldo) {
        Optional<Cuenta> cuentaOpt = cuentaRepository.findById(cuentaId);

        if (cuentaOpt.isPresent()) {
            Cuenta cuenta = cuentaOpt.get();

            if (!cuenta.getEstado()) {
                throw new RuntimeException("No se puede actualizar el saldo de una cuenta inactiva");
            }

            if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
                throw new RuntimeException("El saldo no puede ser negativo");
            }

            cuenta.setSaldoActual(nuevoSaldo);
            Cuenta cuentaActualizada = cuentaRepository.save(cuenta);
            return cuentaMapper.toDTO(cuentaActualizada);
        } else {
            throw new RuntimeException("Cuenta no encontrada con ID: " + cuentaId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean cuentaPerteneceAlCliente(Long cuentaId, String clienteId) {
        Optional<Cuenta> cuentaOpt = cuentaRepository.findById(cuentaId);
        return cuentaOpt.isPresent() && clienteId.equals(cuentaOpt.get().getClienteId());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean tieneSaldoSuficiente(Long cuentaId, BigDecimal monto) {
        Optional<Cuenta> cuentaOpt = cuentaRepository.findById(cuentaId);

        if (cuentaOpt.isPresent()) {
            Cuenta cuenta = cuentaOpt.get();
            return cuenta.getSaldoActual().compareTo(monto) >= 0;
        } else {
            throw new RuntimeException("Cuenta no encontrada con ID: " + cuentaId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean cuentaEstaActiva(Long cuentaId) {
        Optional<Cuenta> cuentaOpt = cuentaRepository.findById(cuentaId);
        return cuentaOpt.isPresent() && cuentaOpt.get().getEstado();
    }

    // Métodos privados de validación
    private void validarSaldoMinimo(CreateCuentaRequest request) {
        validarSaldoMinimo(request.getTipoCuenta(), request.getSaldoInicial());
    }

    private void validarSaldoMinimo(String tipoCuenta, BigDecimal saldoInicial) {
        BigDecimal saldoMinimo = BigDecimal.ZERO;

        if ("AHORROS".equalsIgnoreCase(tipoCuenta)) {
            saldoMinimo = new BigDecimal("0.00");
        } else if ("CORRIENTE".equalsIgnoreCase(tipoCuenta)) {
            saldoMinimo = new BigDecimal("0.00");
        }

        if (saldoInicial.compareTo(saldoMinimo) < 0.00) {
            throw new RuntimeException(
                    String.format("El saldo inicial para cuenta %s debe ser al menos %s",
                            tipoCuenta, saldoMinimo)
            );
        }
    }

    private void validarLimitesSaldoInicial(CreateCuentaRequest request) {
        BigDecimal saldoMaximo = new BigDecimal("0.00"); // Límite máximo de saldo inicial

        if (request.getSaldoInicial().compareTo(saldoMaximo) >= 100000000.00) {
            throw new RuntimeException(
                    String.format("El saldo inicial no puede exceder %s", saldoMaximo)
            );
        }

        // Validación adicional para cuentas de ahorro
        if ("AHORROS".equalsIgnoreCase(request.getTipoCuenta())) {
            BigDecimal limiteAhorros = new BigDecimal("0.00");
            if (request.getSaldoInicial().compareTo(limiteAhorros) >= 100000000.00) {
                throw new RuntimeException(
                        String.format("El saldo inicial para cuenta de ahorros no puede exceder %s", limiteAhorros)
                );
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CuentaDTO> buscarCuentasPorTipo(String tipoCuenta) {
        return cuentaRepository.findByTipoCuentaIgnoreCase(tipoCuenta)
                .stream()
                .map(cuentaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CuentaDTO> buscarCuentasPorRangoSaldo(BigDecimal saldoMin, BigDecimal saldoMax) {
        return cuentaRepository.findBySaldoActualBetween(saldoMin, saldoMax)
                .stream()
                .map(cuentaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long contarCuentasActivas() {
        return cuentaRepository.countByEstadoTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public long contarCuentasPorCliente(String clienteId) {
        return cuentaRepository.countByClienteId(clienteId);
    }

    @Override
    public CuentaDTO realizarDeposito(Long cuentaId, BigDecimal monto) {
        return actualizarSaldoCuenta(cuentaId, monto, "DEPOSITO");
    }

    @Override
    public CuentaDTO realizarRetiro(Long cuentaId, BigDecimal monto) {
        return actualizarSaldoCuenta(cuentaId, monto.negate(), "RETIRO");
    }

    private CuentaDTO actualizarSaldoCuenta(Long cuentaId, BigDecimal monto, String tipoOperacion) {
        Optional<Cuenta> cuentaOpt = cuentaRepository.findById(cuentaId);

        if (cuentaOpt.isPresent()) {
            Cuenta cuenta = cuentaOpt.get();

            if (!cuenta.getEstado()) {
                throw new RuntimeException("No se puede realizar operaciones en una cuenta inactiva");
            }

            BigDecimal nuevoSaldo = cuenta.getSaldoActual().add(monto);

            if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
                throw new RuntimeException("Saldo no disponible para realizar la operación");
            }

            cuenta.setSaldoActual(nuevoSaldo);
            Cuenta cuentaActualizada = cuentaRepository.save(cuenta);
            return cuentaMapper.toDTO(cuentaActualizada);
        } else {
            throw new RuntimeException("Cuenta no encontrada con ID: " + cuentaId);
        }
    }
}