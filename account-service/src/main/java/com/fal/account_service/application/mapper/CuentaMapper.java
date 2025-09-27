package com.fal.account_service.application.mapper;

import com.fal.account_service.application.dto.CreateCuentaRequest;
import com.fal.account_service.application.dto.CuentaDTO;
import com.fal.account_service.application.dto.UpdateCuentaRequest;
import com.fal.account_service.domain.model.Cuenta;
import org.springframework.stereotype.Component;

@Component
public class CuentaMapper {

    public Cuenta toEntity(CreateCuentaRequest request) {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(request.getNumeroCuenta());
        cuenta.setTipoCuenta(request.getTipoCuenta());
        cuenta.setSaldoInicial(request.getSaldoInicial());
        cuenta.setSaldoActual(request.getSaldoInicial()); // Al crear, saldo actual = saldo inicial
        cuenta.setClienteId(request.getClienteId());
        cuenta.setEstado(true); // Por defecto activa
        return cuenta;
    }

    public void updateEntityFromRequest(UpdateCuentaRequest request, Cuenta cuenta) {
        if (request.hasNumeroCuenta()) {
            cuenta.setNumeroCuenta(request.getNumeroCuenta());
        }
        if (request.hasTipoCuenta()) {
            cuenta.setTipoCuenta(request.getTipoCuenta());
        }
        if (request.hasSaldoInicial()) {
            cuenta.setSaldoInicial(request.getSaldoInicial());
            // Si se actualiza el saldo inicial, también actualizar el saldo actual
            if (cuenta.getSaldoActual().compareTo(request.getSaldoInicial()) != 0) {
                cuenta.setSaldoActual(request.getSaldoInicial());
            }
        }
        if (request.hasEstado()) {
            cuenta.setEstado(request.getEstado());
        }
    }

    public CuentaDTO toDTO(Cuenta cuenta) {
        CuentaDTO dto = new CuentaDTO();
        dto.setId(cuenta.getId());
        dto.setNumeroCuenta(cuenta.getNumeroCuenta());
        dto.setTipoCuenta(cuenta.getTipoCuenta());
        dto.setSaldoInicial(cuenta.getSaldoInicial());
        dto.setSaldoActual(cuenta.getSaldoActual());
        dto.setEstado(cuenta.getEstado());
        dto.setClienteId(cuenta.getClienteId());
        dto.setFechaCreacion(cuenta.getFechaCreacion());
        dto.setFechaActualizacion(cuenta.getFechaActualizacion());
        return dto;
    }

    public CreateCuentaRequest toCreateRequest(Cuenta cuenta) {
        CreateCuentaRequest request = new CreateCuentaRequest();
        request.setNumeroCuenta(cuenta.getNumeroCuenta());
        request.setTipoCuenta(cuenta.getTipoCuenta());
        request.setSaldoInicial(cuenta.getSaldoInicial());
        request.setClienteId(cuenta.getClienteId());
        return request;
    }
}
