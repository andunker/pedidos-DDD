package com.example.pedidosddd.application.ports.in;

import com.example.pedidosddd.domain.model.PedidoId;
import com.example.pedidosddd.domain.model.ProductoId;
import java.math.BigDecimal;

/**
 * PUERTO DE ENTRADA - Agregar productos a pedidos 
 * 🎯 DDD: Usa Value Objects del dominio, NO primitivos
 */
public interface AgregarProductoUseCase {
  /** Agrega un producto a un pedido existente */
  void agregarProducto(AgregarProductoCommand command);

  /**
   * COMMAND OBJECT - Encapsula los datos de entrada 
   * 🎯 DDD: PedidoId y ProductoId son Value Objects del dominio, no String primitivos
   */
  record AgregarProductoCommand(
      PedidoId pedidoId,
      ProductoId productoId,
      String nombreProducto,
      BigDecimal precioProducto,
      int cantidad) {
    public AgregarProductoCommand {
      if (pedidoId == null) {
        throw new IllegalArgumentException("PedidoId es requerido");
      }
      if (productoId == null) {
        throw new IllegalArgumentException("ProductoId es requerido");
      }
      if (nombreProducto == null || nombreProducto.isBlank()) {
        throw new IllegalArgumentException("Nombre del producto es requerido");
      }
      if (precioProducto == null || precioProducto.compareTo(BigDecimal.ZERO) <= 0) {
        throw new IllegalArgumentException("Precio debe ser positivo");
      }
      if (cantidad <= 0) {
        throw new IllegalArgumentException("Cantidad debe ser positiva");
      }
    }
  }
}
