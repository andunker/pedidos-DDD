package com.example.pedidosddd.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * VALUE OBJECT - Representa un producto en el contexto de Pedidos 
 * En este contexto simplificado, Producto es un Value Object 
 * En una aplicación real, podría ser una Entidad en otro Bounded Context
 * 
 * 🎯 DDD: Usa ProductoId Value Object, no String primitivo
 */
public class Producto {
  private final ProductoId id;
  private final String nombre;
  private final BigDecimal precio;

  public Producto(ProductoId id, String nombre, BigDecimal precio) {
    if (id == null
        || nombre == null
        || nombre.isBlank()
        || precio == null
        || precio.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Invalid product data");
    }
    this.id = id;
    this.nombre = nombre;
    this.precio = precio;
  }

  public ProductoId getId() {
    return id;
  }

  public String getNombre() {
    return nombre;
  }

  public BigDecimal getPrecio() {
    return precio;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Producto producto = (Producto) o;
    return Objects.equals(id, producto.id)
        && Objects.equals(nombre, producto.nombre)
        && Objects.equals(precio, producto.precio);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, nombre, precio);
  }
}
