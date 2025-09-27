package com.fal.account_service.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EstadoCuentaResponse {

    @JsonProperty("cliente")
    private ClienteInfo clienteInfo;

    @JsonProperty("periodo")
    private PeriodoReporte periodo;

    @JsonProperty("cuentas")
    private List<CuentaEstado> cuentas;

    @JsonProperty("resumenGeneral")
    private ResumenGeneral resumenGeneral;

    @JsonProperty("metadata")
    private MetadataReporte metadata;

    // Constructores
    public EstadoCuentaResponse() {
        this.metadata = new MetadataReporte();
    }

    public EstadoCuentaResponse(ClienteInfo clienteInfo, PeriodoReporte periodo) {
        this();
        this.clienteInfo = clienteInfo;
        this.periodo = periodo;
    }

    // Getters y Setters
    public ClienteInfo getClienteInfo() { return clienteInfo; }
    public void setClienteInfo(ClienteInfo clienteInfo) { this.clienteInfo = clienteInfo; }

    public PeriodoReporte getPeriodo() { return periodo; }
    public void setPeriodo(PeriodoReporte periodo) { this.periodo = periodo; }

    public List<CuentaEstado> getCuentas() { return cuentas; }
    public void setCuentas(List<CuentaEstado> cuentas) { this.cuentas = cuentas; }

    public ResumenGeneral getResumenGeneral() { return resumenGeneral; }
    public void setResumenGeneral(ResumenGeneral resumenGeneral) { this.resumenGeneral = resumenGeneral; }

    public MetadataReporte getMetadata() { return metadata; }
    public void setMetadata(MetadataReporte metadata) { this.metadata = metadata; }


    public void calcularResumenGeneral() {
        if (cuentas == null || cuentas.isEmpty()) {
            this.resumenGeneral = new ResumenGeneral();
            return;
        }

        BigDecimal totalSaldoInicial = BigDecimal.ZERO;
        BigDecimal totalSaldoFinal = BigDecimal.ZERO;
        BigDecimal totalDepositos = BigDecimal.ZERO;
        BigDecimal totalRetiros = BigDecimal.ZERO;
        int totalMovimientos = 0;
        int totalCuentasActivas = 0;

        for (CuentaEstado cuenta : cuentas) {
            // Sumar saldos directamente de la cuenta
            if (cuenta.getSaldoInicial() != null) {
                totalSaldoInicial = totalSaldoInicial.add(cuenta.getSaldoInicial());
            }
            if (cuenta.getSaldoFinal() != null) {
                totalSaldoFinal = totalSaldoFinal.add(cuenta.getSaldoFinal());
            }

            // Calcular movimientos y montos
            if (cuenta.getMovimientos() != null) {
                for (MovimientoEstado movimiento : cuenta.getMovimientos()) {
                    if (movimiento != null && movimiento.getMonto() != null) {
                        if (movimiento.isCredito()) {
                            totalDepositos = totalDepositos.add(movimiento.getMonto());
                        } else if (movimiento.isDebito()) {
                            totalRetiros = totalRetiros.add(movimiento.getMonto());
                        }
                    }
                }
                totalMovimientos += cuenta.getMovimientos().size();
            }

            if (cuenta.isActiva()) {
                totalCuentasActivas++;
            }
        }

        this.resumenGeneral = new ResumenGeneral(
                totalSaldoInicial,
                totalSaldoFinal,
                totalDepositos,  // Usar totalDepositos en lugar de totalCreditos
                totalRetiros,    // Usar totalRetiros en lugar de totalDebitos
                totalMovimientos,
                cuentas.size(),
                totalCuentasActivas
        );
    }

    public boolean tieneMovimientos() {
        return cuentas != null && cuentas.stream()
                .anyMatch(cuenta -> cuenta.getMovimientos() != null && !cuenta.getMovimientos().isEmpty());
    }

}
