package com.example.pedidosddd.infrastructure.adapters.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * REPOSITORIO JPA - Spring Data
 *
 * <p>¿Qué es? La interfaz que Spring Data implementa automáticamente ¿Dónde está? En infrastructure
 * porque es detalle técnico de persistencia
 *
 * <p>En Hexagonal: Este es un DETALLE del adaptador, no el puerto
 */
@Repository
public interface JpaPedidoRepository extends JpaRepository<PedidoEntity, String> {

  // Spring Data genera automáticamente:
  // - save(PedidoEntity entity)
  // - findById(String id)
  // - deleteById(String id)
  // - findAll()
  // Y muchos más...

  // Podemos agregar queries personalizadas si necesitamos:
  // @Query("SELECT p FROM PedidoEntity p WHERE p.estado = :estado")
  // List<PedidoEntity> findByEstado(@Param("estado") EstadoPedidoEntity estado);
}
