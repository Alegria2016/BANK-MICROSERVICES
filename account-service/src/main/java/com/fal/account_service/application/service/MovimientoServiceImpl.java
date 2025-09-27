package com.fal.account_service.application.service;

import com.fal.account_service.application.dto.*;
import com.fal.account_service.application.mapper.MovimientoMapper;
import com.fal.account_service.domain.model.Cuenta;
import com.fal.account_service.domain.model.Movimiento;
import com.fal.account_service.domain.repository.CuentaRepository;
import com.fal.account_service.domain.repository.MovimientoRepository;
import com.fal.account_service.domain.service.MovimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MovimientoServiceImpl implements MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final CuentaRepository cuentaRepository;
    private final MovimientoMapper movimientoMapper;

    @Autowired
    public MovimientoServiceImpl(MovimientoRepository movimientoRepository,
                                 CuentaRepository cuentaRepository,
                                 MovimientoMapper movimientoMapper) {
        this.movimientoRepository = movimientoRepository;
        this.cuentaRepository = cuentaRepository;
        this.movimientoMapper = movimientoMapper;
    }

    @Override
    @Transactional
    public MovimientoDTO realizarMovimiento(CreateMovimientoRequest request) {

        request.validarMovimiento();

        Cuenta cuenta = cuentaRepository.findById(request.getCuentaId())
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con ID: " + request.getCuentaId()));

        if (!cuenta.getEstado()) {
            throw new RuntimeException("La cuenta está inactiva");
        }

        BigDecimal nuevoSaldo = calcularNuevoSaldo(cuenta.getSaldoActual(), request.getTipoMovimiento(), request.getValor());

        if (request.isRetiro() && nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Saldo no disponible");
        }

        Movimiento movimiento = movimientoMapper.toEntity(request, cuenta, nuevoSaldo);

        cuenta.setSaldoActual(nuevoSaldo);
        cuentaRepository.save(cuenta);

        Movimiento movimientoGuardado = movimientoRepository.save(movimiento);

        return movimientoMapper.toDTO(movimientoGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public MovimientoDTO obtenerMovimientoPorId(Long id) {
        Movimiento movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado con ID: " + id));
        return movimientoMapper.toDTO(movimiento);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoDTO> obtenerTodosLosMovimientos() {
        return movimientoRepository.findAll()
                .stream()
                .map(movimientoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoDTO> obtenerMovimientosPorCuenta(Long cuentaId) {

        if (!cuentaRepository.existsById(cuentaId)) {
            throw new RuntimeException("Cuenta no encontrada con ID: " + cuentaId);
        }

        return movimientoRepository.findByCuentaId(cuentaId)
                .stream()
                .map(movimientoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoDTO> obtenerMovimientosPorCliente(String clienteId) {

        List<Cuenta> cuentas = cuentaRepository.findByClienteId(clienteId);
        if (cuentas.isEmpty()) {
            throw new RuntimeException("El cliente no tiene cuentas asociadas");
        }

        return movimientoRepository.findByClienteId(clienteId)
                .stream()
                .map(movimientoMapper::toDTO)
                .collect(Collectors.toList());
    }



    @Override
    @Transactional(readOnly = true)
    public MovimientoResumenResponse obtenerResumenMovimientos(String clienteId, LocalDate fechaInicio, LocalDate fechaFin) {

        if (fechaInicio == null || fechaFin == null) {
            fechaFin = LocalDate.now();
            fechaInicio = fechaFin.minusMonths(1);
        }

        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.atTime(LocalTime.MAX);

        List<Movimiento> movimientos = movimientoRepository.findByClienteIdAndFechaBetween(clienteId, inicio, fin);

        BigDecimal totalDepositos = BigDecimal.ZERO;
        BigDecimal totalRetiros = BigDecimal.ZERO;

        for (Movimiento movimiento : movimientos) {
            if ("DEPOSITO".equalsIgnoreCase(movimiento.getTipoMovimiento())) {
                totalDepositos = totalDepositos.add(movimiento.getValor());
            } else if ("RETIRO".equalsIgnoreCase(movimiento.getTipoMovimiento())) {
                totalRetiros = totalRetiros.add(movimiento.getValor());
            }
        }

        MovimientoResumenResponse resumen = new MovimientoResumenResponse();
        resumen.setClienteId(clienteId);
        resumen.setTotalMovimientos(movimientos.size());
        resumen.setTotalDepositos(totalDepositos);
        resumen.setTotalRetiros(totalRetiros);
        resumen.setSaldoNeto(totalDepositos.subtract(totalRetiros));
        resumen.setPeriodo(String.format("%s a %s", fechaInicio, fechaFin));

        return resumen;
    }

    @Override
    @Transactional
    public MovimientoDTO actualizarMovimiento(Long id, UpdateMovimientoRequest request) {
        Movimiento movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado con ID: " + id));

        if (request.hasValor() || request.hasTipoMovimiento()) {
            throw new RuntimeException("No se puede modificar el valor o tipo de movimiento por integridad contable");
        }

        movimientoMapper.updateEntityFromRequest(request, movimiento);

        Movimiento movimientoActualizado = movimientoRepository.save(movimiento);
        return movimientoMapper.toDTO(movimientoActualizado);
    }

    @Override
    @Transactional
    public void eliminarMovimiento(Long id) {
        Movimiento movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado con ID: " + id));

        throw new RuntimeException("No se permite eliminar movimientos por integridad contable. Use reverso de movimiento.");
    }

    @Override
    @Transactional
    public MovimientoDTO realizarDeposito(Long cuentaId, BigDecimal monto, String descripcion) {
        CreateMovimientoRequest request = new CreateMovimientoRequest();
        request.setCuentaId(cuentaId);
        request.setTipoMovimiento("DEPOSITO");
        request.setValor(monto);
        request.setDescripcion(descripcion != null ? descripcion : "Depósito realizado");

        return realizarMovimiento(request);
    }

    @Override
    @Transactional
    public MovimientoDTO realizarRetiro(Long cuentaId, BigDecimal monto, String descripcion) {
        CreateMovimientoRequest request = new CreateMovimientoRequest();
        request.setCuentaId(cuentaId);
        request.setTipoMovimiento("RETIRO");
        request.setValor(monto);
        request.setDescripcion(descripcion != null ? descripcion : "Retiro realizado");

        return realizarMovimiento(request);
    }



    private BigDecimal calcularNuevoSaldo(BigDecimal saldoActual, String tipoMovimiento, BigDecimal valor) {
        return "DEPOSITO".equals(tipoMovimiento) ?
                saldoActual.add(valor) : saldoActual.subtract(valor);
    }

}
