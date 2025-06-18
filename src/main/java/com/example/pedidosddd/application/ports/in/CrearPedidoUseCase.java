package com.example.pedidosddd.application.ports.in;

import com.example.pedidosddd.domain.model.PedidoId;

/**
 * PUERTO DE ENTRADA - Define qué puede hacer la aplicación En Arquitectura Hexagonal, los casos de
 * uso son contratos
 */
public interface CrearPedidoUseCase {
  /**
   * Crea un nuevo pedido vacío
   *
   * @return El ID del pedido creado
   */
  PedidoId crearNuevoPedido();
}
