package com.example.pedidosddd.domain.model;

import java.util.Objects;
import java.util.UUID;

/**
 * VALUE OBJECT - Identidad única de un Pedido Características de un Value Object: - Inmutable
 * (campos final) - Igualdad por valor (equals/hashCode) - Sin identidad propia
 */
public class PedidoId {
  private final String value;

  public PedidoId() {
    this.value = UUID.randomUUID().toString();
  }

  public PedidoId(String value) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException("PedidoId cannot be null or empty");
    }
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PedidoId pedidoId = (PedidoId) o;
    return Objects.equals(value, pedidoId.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public String toString() {
    return "PedidoId{" + "value='" + value + '\'' + '}';
  }
}
