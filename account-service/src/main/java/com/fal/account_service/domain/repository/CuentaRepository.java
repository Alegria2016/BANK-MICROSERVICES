package com.fal.account_service.domain.repository;

import com.fal.account_service.domain.model.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {

    Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);
    List<Cuenta> findByClienteId(String clienteId);
    List<Cuenta> findByEstadoTrue();
    List<Cuenta> findByClienteIdAndEstadoTrue(String clienteId);
    boolean existsByNumeroCuenta(String numeroCuenta);

    @Query("SELECT c FROM Cuenta c WHERE c.tipoCuenta ILIKE :tipoCuenta")
    List<Cuenta> findByTipoCuentaIgnoreCase(@Param("tipoCuenta") String tipoCuenta);

    List<Cuenta> findBySaldoActualBetween(BigDecimal saldoMin, BigDecimal saldoMax);

    long countByEstadoTrue();
    long countByClienteId(String clienteId);

}
