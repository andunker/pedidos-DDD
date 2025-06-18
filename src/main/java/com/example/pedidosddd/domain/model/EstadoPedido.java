package com.example.pedidosddd.domain.model;

/**
 * ENUM como VALUE OBJECT - Estados posibles de un Pedido Define las transiciones válidas en el
 * ciclo de vida del pedido
 */
public enum EstadoPedido {
  PENDIENTE,
  COMPLETADO,
  CANCELADO
}
