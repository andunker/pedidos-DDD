package com.example.pedidosddd.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * VALUE OBJECT - Representa un item dentro de un pedido No tiene identidad propia, su identidad
 * depende del Pedido que lo contiene Es inmutable después de su creación
 */
public class PedidoItem {
  private final Producto producto;
  private final int cantidad;
  private final BigDecimal precioUnitario;

  // Constructor para JPA
  protected PedidoItem() {
    this.producto = null;
    this.cantidad = 0;
    this.precioUnitario = BigDecimal.ZERO;
  }

  public PedidoItem(Producto producto, int cantidad) {
    if (producto == null || cantidad <= 0) {
      throw new IllegalArgumentException(
          "Producto y cantidad deben ser válidos para crear un PedidoItem.");
    }
    this.producto = producto;
    this.cantidad = cantidad;
    this.precioUnitario = producto.getPrecio(); // Captura el precio al momento de agregar
  }

  public Producto getProducto() {
    return producto;
  }

  public int getCantidad() {
    return cantidad;
  }

  public BigDecimal getPrecioUnitario() {
    return precioUnitario;
  }

  public BigDecimal getTotal() {
    return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
  }

  /**
   * Crea un nuevo PedidoItem con cantidad aumentada Como es un Value Object, no se modifica sino
   * que se crea uno nuevo
   */
  public PedidoItem aumentarCantidad(int cantidadAdicional) {
    if (cantidadAdicional <= 0) {
      throw new IllegalArgumentException("La cantidad a agregar debe ser positiva.");
    }
    return new PedidoItem(this.producto, this.cantidad + cantidadAdicional);
  }

  // Un pedido item se considera igual si el producto es el mismo
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PedidoItem that = (PedidoItem) o;
    return Objects.equals(producto.getId(), that.producto.getId()); // Comparamos por ProductoId (Value Object)
  }

  @Override
  public int hashCode() {
    return Objects.hash(producto.getId());
  }
}
