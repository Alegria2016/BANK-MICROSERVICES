package com.fal.account_service.domain.repository;

import com.fal.account_service.domain.model.Cuenta;
import com.fal.account_service.domain.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    List<Movimiento> findByCuentaId(Long cuentaId);
    List<Movimiento> findByClienteId(String clienteId);
    List<Movimiento> findByClienteIdAndFechaBetween(String clienteId, LocalDateTime fechaInicio, LocalDateTime fechaFin);

    @Query("SELECT m FROM Movimiento m WHERE m.cuenta.id = :cuentaId ORDER BY m.fecha DESC LIMIT :limite")
    List<Movimiento> findTopByCuentaIdOrderByFechaDesc(@Param("cuentaId") Long cuentaId, @Param("limite") int limite);

    @Query("SELECT m FROM Movimiento m WHERE " +
            "(:cuentaId IS NULL OR m.cuenta.id = :cuentaId) AND " +
            "(:tipoMovimiento IS NULL OR UPPER(m.tipoMovimiento) = UPPER(:tipoMovimiento)) AND " +
            "(:fechaInicio IS NULL OR m.fecha >= :fechaInicio) AND " +
            "(:fechaFin IS NULL OR m.fecha <= :fechaFin) " +
            "ORDER BY m.fecha DESC")
    List<Movimiento> findByFiltros(@Param("cuentaId") Long cuentaId,
                                   @Param("tipoMovimiento") String tipoMovimiento,
                                   @Param("fechaInicio") LocalDateTime fechaInicio,
                                   @Param("fechaFin") LocalDateTime fechaFin);

    @Query("SELECT m FROM Movimiento m WHERE m.cuenta.id = :cuentaId AND " +
            "(:tipoMovimiento IS NULL OR UPPER(m.tipoMovimiento) = UPPER(:tipoMovimiento)) AND " +
            "(:fechaInicio IS NULL OR m.fecha >= :fechaInicio) AND " +
            "(:fechaFin IS NULL OR m.fecha <= :fechaFin) " +
            "ORDER BY m.fecha DESC")
    List<Movimiento> findByCuentaIdAndFiltros(@Param("cuentaId") Long cuentaId,
                                              @Param("tipoMovimiento") String tipoMovimiento,
                                              @Param("fechaInicio") LocalDateTime fechaInicio,
                                              @Param("fechaFin") LocalDateTime fechaFin);

    @Query("SELECT m FROM Movimiento m WHERE m.clienteId = :clienteId AND " +
            "(:tipoMovimiento IS NULL OR UPPER(m.tipoMovimiento) = UPPER(:tipoMovimiento)) AND " +
            "(:fechaInicio IS NULL OR m.fecha >= :fechaInicio) AND " +
            "(:fechaFin IS NULL OR m.fecha <= :fechaFin) " +
            "ORDER BY m.fecha DESC")
    List<Movimiento> findByClienteIdAndFiltros(@Param("clienteId") String clienteId,
                                               @Param("tipoMovimiento") String tipoMovimiento,
                                               @Param("fechaInicio") LocalDateTime fechaInicio,
                                               @Param("fechaFin") LocalDateTime fechaFin);

    @Query("SELECT SUM(m.valor) FROM Movimiento m WHERE m.clienteId = :clienteId AND " +
            "UPPER(m.tipoMovimiento) = 'DEPOSITO' AND " +
            "m.fecha BETWEEN :fechaInicio AND :fechaFin")
    BigDecimal sumDepositosByClienteAndPeriod(@Param("clienteId") String clienteId,
                                              @Param("fechaInicio") LocalDateTime fechaInicio,
                                              @Param("fechaFin") LocalDateTime fechaFin);

    @Query("SELECT SUM(m.valor) FROM Movimiento m WHERE m.clienteId = :clienteId AND " +
            "UPPER(m.tipoMovimiento) = 'RETIRO' AND " +
            "m.fecha BETWEEN :fechaInicio AND :fechaFin")
    BigDecimal sumRetirosByClienteAndPeriod(@Param("clienteId") String clienteId,
                                            @Param("fechaInicio") LocalDateTime fechaInicio,
                                            @Param("fechaFin") LocalDateTime fechaFin);

    boolean existsByCuentaIdAndFechaBetween(Long cuentaId, LocalDateTime fechaInicio, LocalDateTime fechaFin);

    List<Movimiento> findByCuentaIdAndFechaBetween(Long cuentaId, LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
