package com.example.pedidosddd.domain.model;

import java.time.LocalDateTime;

/** Factory para la creación de Pedidos Encapsula la lógica de construcción y validación inicial */
public class PedidoFactory {

  public static Pedido crearNuevoPedido() {
    return new Pedido();
  }

  public static Pedido recrearPedidoDesdeRepositorio(
      PedidoId id,
      java.util.List<PedidoItem> items,
      EstadoPedido estado,
      LocalDateTime fechaCreacion,
      LocalDateTime fechaActualizacion) {
    return new Pedido(id, items, estado, fechaCreacion, fechaActualizacion);
  }
}
