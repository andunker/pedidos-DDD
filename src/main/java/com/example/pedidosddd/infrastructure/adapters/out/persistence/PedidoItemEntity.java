package com.example.pedidosddd.infrastructure.adapters.out.persistence;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * ENTIDAD JPA - Item de Pedido para persistencia
 *
 * <p>Representa un item dentro de un pedido en la base de datos Separada del Value Object del
 * dominio para mantener pureza
 */
@Entity
@Table(name = "pedido_items")
public class PedidoItemEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "pedido_id")
  private PedidoEntity pedido;

  @Column(name = "producto_id")
  private String productoId;

  @Column(name = "nombre_producto")
  private String nombreProducto;

  private int cantidad;

  @Column(name = "precio_unitario", precision = 10, scale = 2)
  private BigDecimal precioUnitario;

  // Constructor para JPA
  protected PedidoItemEntity() {}

  public PedidoItemEntity(
      String productoId, String nombreProducto, int cantidad, BigDecimal precioUnitario) {
    this.productoId = productoId;
    this.nombreProducto = nombreProducto;
    this.cantidad = cantidad;
    this.precioUnitario = precioUnitario;
  }

  // Getters y Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public PedidoEntity getPedido() {
    return pedido;
  }

  public void setPedido(PedidoEntity pedido) {
    this.pedido = pedido;
  }

  public String getProductoId() {
    return productoId;
  }

  public void setProductoId(String productoId) {
    this.productoId = productoId;
  }

  public String getNombreProducto() {
    return nombreProducto;
  }

  public void setNombreProducto(String nombreProducto) {
    this.nombreProducto = nombreProducto;
  }

  public int getCantidad() {
    return cantidad;
  }

  public void setCantidad(int cantidad) {
    this.cantidad = cantidad;
  }

  public BigDecimal getPrecioUnitario() {
    return precioUnitario;
  }

  public void setPrecioUnitario(BigDecimal precioUnitario) {
    this.precioUnitario = precioUnitario;
  }

  public BigDecimal getTotal() {
    return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
  }
}
