package com.example.pedidosddd.infrastructure.adapters.out.persistence;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * ENTIDAD JPA - Capa de Persistencia
 *
 * <p>¿Qué es? La representación de Pedido para la base de datos ¿Por qué separada del dominio? Para
 * mantener el dominio puro
 *
 * <p>En Hexagonal: Esta clase pertenece al ADAPTADOR, no al dominio
 */
@Entity
@Table(name = "pedidos")
public class PedidoEntity {

  @Id private String id;

  @Enumerated(EnumType.STRING)
  private EstadoPedidoEntity estado;

  @Column(name = "fecha_creacion")
  private LocalDateTime fechaCreacion;

  @Column(name = "fecha_actualizacion")
  private LocalDateTime fechaActualizacion;

  @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<PedidoItemEntity> items = new ArrayList<>();

  // Constructor para JPA
  protected PedidoEntity() {}

  public PedidoEntity(
      String id,
      EstadoPedidoEntity estado,
      LocalDateTime fechaCreacion,
      LocalDateTime fechaActualizacion) {
    this.id = id;
    this.estado = estado;
    this.fechaCreacion = fechaCreacion;
    this.fechaActualizacion = fechaActualizacion;
  }

  // Getters y Setters
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public EstadoPedidoEntity getEstado() {
    return estado;
  }

  public void setEstado(EstadoPedidoEntity estado) {
    this.estado = estado;
  }

  public LocalDateTime getFechaCreacion() {
    return fechaCreacion;
  }

  public void setFechaCreacion(LocalDateTime fechaCreacion) {
    this.fechaCreacion = fechaCreacion;
  }

  public LocalDateTime getFechaActualizacion() {
    return fechaActualizacion;
  }

  public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
    this.fechaActualizacion = fechaActualizacion;
  }

  public List<PedidoItemEntity> getItems() {
    return items;
  }

  public void setItems(List<PedidoItemEntity> items) {
    this.items = items;
  }

  public void addItem(PedidoItemEntity item) {
    items.add(item);
    item.setPedido(this);
  }
}

/** Enum para el estado del pedido en base de datos */
enum EstadoPedidoEntity {
  NUEVO,
  PROCESANDO,
  COMPLETADO,
  CANCELADO
}
