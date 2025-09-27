package com.fal.client_service.infrastructure.persistence;

import com.fal.client_service.domain.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByIdentificacion(String identificacion);

    Optional<Cliente> findByClienteId(String clienteId);

    @Query("SELECT c FROM Cliente c WHERE c.estado = true AND c.identificacion = :identificacion")
    Optional<Cliente> findActiveByIdentificacion(@Param("identificacion") String identificacion);

    boolean existsByIdentificacion(String identificacion);

    boolean existsByClienteId(String clienteId);

    List<Cliente> findByEstadoTrue();

    @Query("SELECT c FROM Cliente c WHERE c.estado = true ORDER BY c.nombre ASC")
    List<Cliente> findAllActiveClients();

    @Query("SELECT COUNT(c) > 0 FROM Cliente c WHERE c.identificacion = :identificacion AND c.id != :id")
    boolean existsByIdentificacionAndIdNot(@Param("identificacion") String identificacion, @Param("id") Long id);
}
