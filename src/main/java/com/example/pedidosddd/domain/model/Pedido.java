package com.example.pedidosddd.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Aggregate Root para el contexto de Pedidos Encapsula las reglas de negocio relacionadas con
 * pedidos y sus items
 */
public class Pedido {
  private PedidoId id;
  private List<PedidoItem> items;
  private EstadoPedido estado;
  private LocalDateTime fechaCreacion;
  private LocalDateTime fechaActualizacion;

  // Constructor para un nuevo pedido
  public Pedido() {
    this.id = new PedidoId();
    this.items = new ArrayList<>();
    this.estado = EstadoPedido.PENDIENTE;
    this.fechaCreacion = LocalDateTime.now();
    this.fechaActualizacion = LocalDateTime.now();
  }

  // Constructor para recrear un Pedido desde la persistencia (usado por el repositorio)
  public Pedido(
      PedidoId id,
      List<PedidoItem> items,
      EstadoPedido estado,
      LocalDateTime fechaCreacion,
      LocalDateTime fechaActualizacion) {
    if (id == null
        || items == null
        || estado == null
        || fechaCreacion == null
        || fechaActualizacion == null) {
      throw new IllegalArgumentException("All parameters must be non-null for Pedido recreation.");
    }
    this.id = id;
    this.items = new ArrayList<>(items); // Defensive copy
    this.estado = estado;
    this.fechaCreacion = fechaCreacion;
    this.fechaActualizacion = fechaActualizacion;
  }

  // ========== COMPORTAMIENTO DE NEGOCIO ==========
  // La lógica de negocio va en el dominio, no en servicios

  /** Regla de negocio: Solo se pueden agregar productos a pedidos pendientes */
  public void agregarProducto(Producto producto, int cantidad) {
    if (estado != EstadoPedido.PENDIENTE) {
      throw new IllegalStateException("No se pueden agregar productos a un pedido " + estado);
    }
    if (producto == null || cantidad <= 0) {
      throw new IllegalArgumentException("Producto y cantidad deben ser válidos.");
    }

    // Buscar si ya existe un item con el mismo producto
    Optional<PedidoItem> existingItem =
        items.stream()
            .filter(item -> item.getProducto().getId().equals(producto.getId()))
            .findFirst();

    if (existingItem.isPresent()) {
      // Reemplazar el item existente con uno nuevo con cantidad aumentada
      items.remove(existingItem.get());
      items.add(existingItem.get().aumentarCantidad(cantidad));
    } else {
      // Agregar nuevo item
      items.add(new PedidoItem(producto, cantidad));
    }
    this.fechaActualizacion = LocalDateTime.now();
  }

  /** Regla de negocio: Solo se pueden completar pedidos pendientes con items */
  public void completarPedido() {
    if (estado != EstadoPedido.PENDIENTE) {
      throw new IllegalStateException("Solo se pueden completar pedidos en estado PENDIENTE");
    }
    if (items.isEmpty()) {
      throw new IllegalStateException("No se puede completar un pedido vacío");
    }
    this.estado = EstadoPedido.COMPLETADO;
    this.fechaActualizacion = LocalDateTime.now();
    // TODO: En proyectos avanzados, aquí se dispararía un Domain Event
    // DomainEventPublisher.publish(new PedidoCompletadoEvent(this.id));
  }

  public void cancelarPedido() {
    if (estado == EstadoPedido.COMPLETADO) {
      throw new IllegalStateException("Completed order cannot be cancelled.");
    }
    this.estado = EstadoPedido.CANCELADO;
    this.fechaActualizacion = LocalDateTime.now();
    // Podríamos disparar un evento de dominio aquí, e.g., new PedidoCanceladoEvent(this.id);
  }

  public BigDecimal calcularTotal() {
    return items.stream().map(PedidoItem::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  // ========== GETTERS ==========
  // Exponen el estado interno pero mantienen la encapsulación

  public PedidoId getId() {
    return id;
  }

  /** Retorna una vista inmutable de los items para proteger la invariante del agregado */
  public List<PedidoItem> getItems() {
    return Collections.unmodifiableList(items);
  }

  public EstadoPedido getEstado() {
    return estado;
  }

  public LocalDateTime getFechaCreacion() {
    return fechaCreacion;
  }

  public LocalDateTime getFechaActualizacion() {
    return fechaActualizacion;
  }

  // Nota: No hay setters públicos para propiedades mutables, el estado se cambia a través de los
  // métodos de negocio.
}
