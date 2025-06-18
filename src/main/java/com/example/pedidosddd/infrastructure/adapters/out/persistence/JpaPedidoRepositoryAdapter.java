package com.example.pedidosddd.infrastructure.adapters.out.persistence;

import com.example.pedidosddd.application.ports.out.PedidoRepositoryPort;
import com.example.pedidosddd.domain.model.Pedido;
import com.example.pedidosddd.domain.model.PedidoId;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * ADAPTADOR DE SALIDA - Persistencia JPA Real
 *
 * <p>¿Qué hace? Implementa el puerto de salida usando JPA + H2 ¿Por qué H2? Base de datos real pero
 * en memoria (perfecto para aprendizaje)
 *
 * <p>En Hexagonal: Este adaptador traduce entre el dominio y la base de datos Ventaja: Puedo
 * cambiar esta implementación (H2 -> PostgreSQL) sin tocar el dominio
 */
@Repository
public class JpaPedidoRepositoryAdapter implements PedidoRepositoryPort {

  private final JpaPedidoRepository jpaRepository;
  private final PedidoMapper mapper;

  public JpaPedidoRepositoryAdapter(JpaPedidoRepository jpaRepository, PedidoMapper mapper) {
    this.jpaRepository = jpaRepository;
    this.mapper = mapper;
  }

  /** Busca un pedido por ID usando JPA real Puerto de salida implementado con H2 */
  @Override
  public Optional<Pedido> findById(PedidoId pedidoId) {
    return jpaRepository
        .findById(pedidoId.getValue())
        .map(mapper::toDomain); // Convierte Entity -> Domain
  }

  /** Guarda un pedido usando JPA real Puerto de salida implementado con H2 */
  @Override
  public Pedido save(Pedido pedido) {
    // 1. Convertir Domain -> Entity
    PedidoEntity entity = mapper.toEntity(pedido);

    // 2. Guardar en H2 usando JPA
    PedidoEntity savedEntity = jpaRepository.save(entity);

    // 3. Convertir Entity -> Domain y retornar
    return mapper.toDomain(savedEntity);
  }

  /** Elimina un pedido por ID usando JPA real Puerto de salida implementado con H2 */
  @Override
  public void deleteById(PedidoId pedidoId) {
    jpaRepository.deleteById(pedidoId.getValue());
  }

  // Método de utilidad para verificar datos en H2
  public long cantidadPedidos() {
    return jpaRepository.count();
  }
}
