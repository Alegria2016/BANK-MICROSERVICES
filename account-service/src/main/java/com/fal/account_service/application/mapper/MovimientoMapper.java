package com.fal.account_service.application.mapper;

import com.fal.account_service.application.dto.CreateMovimientoRequest;
import com.fal.account_service.application.dto.MovimientoDTO;
import com.fal.account_service.application.dto.UpdateMovimientoRequest;
import com.fal.account_service.domain.model.Cuenta;
import com.fal.account_service.domain.model.Movimiento;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class MovimientoMapper {

    public Movimiento toEntity(CreateMovimientoRequest request, Cuenta cuenta, BigDecimal nuevoSaldo) {
        Movimiento movimiento = new Movimiento();
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setTipoMovimiento(request.getTipoMovimiento());
        movimiento.setValor(request.getValor());
        movimiento.setSaldo(nuevoSaldo);
        movimiento.setDescripcion(request.getDescripcionAutomatica());
        movimiento.setCuenta(cuenta);
        movimiento.setClienteId(cuenta.getClienteId());
        return movimiento;
    }

    public void updateEntityFromRequest(UpdateMovimientoRequest request, Movimiento movimiento) {
        if (request.hasTipoMovimiento()) {
            movimiento.setTipoMovimiento(request.getTipoMovimiento());
        }
        if (request.hasValor()) {
            movimiento.setValor(request.getValor());
        }
        if (request.hasDescripcion()) {
            movimiento.setDescripcion(request.getDescripcion());
        }
        movimiento.setFechaActualizacion(LocalDateTime.now());
    }

    public MovimientoDTO toDTO(Movimiento movimiento) {
        MovimientoDTO dto = new MovimientoDTO();
        dto.setId(movimiento.getId());
        dto.setFecha(movimiento.getFecha());
        dto.setTipoMovimiento(movimiento.getTipoMovimiento());
        dto.setValor(movimiento.getValor());
        dto.setSaldo(movimiento.getSaldo());
        dto.setDescripcion(movimiento.getDescripcion());

        if (movimiento.getCuenta() != null) {
            dto.setCuentaId(movimiento.getCuenta().getId());
            dto.setNumeroCuenta(movimiento.getCuenta().getNumeroCuenta());
        }

        dto.setClienteId(movimiento.getClienteId());
        return dto;
    }

    public MovimientoDTO toDTOWithClienteInfo(Movimiento movimiento, String nombreCliente) {
        MovimientoDTO dto = toDTO(movimiento);
        dto.setNombreCliente(nombreCliente);
        return dto;
    }

    public CreateMovimientoRequest toCreateRequest(Movimiento movimiento) {
        CreateMovimientoRequest request = new CreateMovimientoRequest();
        if (movimiento.getCuenta() != null) {
            request.setCuentaId(movimiento.getCuenta().getId());
        }
        request.setTipoMovimiento(movimiento.getTipoMovimiento());
        request.setValor(movimiento.getValor());
        request.setDescripcion(movimiento.getDescripcion());
        return request;
    }
}
