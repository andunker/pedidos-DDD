package com.example.pedidosddd.application.ports.out;

import com.example.pedidosddd.domain.model.Pedido;
import com.example.pedidosddd.domain.model.PedidoId;
import java.util.Optional;

/**
 * PUERTO DE SALIDA - Define cómo la aplicación accede a datos externos En Arquitectura Hexagonal,
 * esto está en application/ports/out
 */
public interface PedidoRepositoryPort {
  Optional<Pedido> findById(PedidoId pedidoId);

  Pedido save(Pedido pedido);

  void deleteById(PedidoId pedidoId);
}
