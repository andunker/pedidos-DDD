package com.example.pedidosddd.infrastructure.adapters.out.persistence;

import com.example.pedidosddd.domain.model.*;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * MAPPER - Convierte entre Dominio y Persistencia
 *
 * <p>¿Qué hace? Traduce objetos del dominio a entidades JPA y viceversa ¿Por qué existe? Para
 * mantener el dominio puro sin anotaciones JPA
 *
 * <p>En Hexagonal: Esto es parte del ADAPTADOR, no del dominio
 */
@Component
public class PedidoMapper {

  /** Convierte del dominio a entidad JPA Dominio -> Base de datos */
  public PedidoEntity toEntity(Pedido pedido) {
    PedidoEntity entity =
        new PedidoEntity(
            pedido.getId().getValue(),
            mapEstado(pedido.getEstado()),
            pedido.getFechaCreacion(),
            pedido.getFechaActualizacion());

    // Mapear items
    java.util.List<PedidoItemEntity> itemEntities =
        pedido.getItems().stream().map(this::toItemEntity).collect(Collectors.toList());

    for (PedidoItemEntity itemEntity : itemEntities) {
      entity.addItem(itemEntity);
    }

    return entity;
  }

  /** Convierte de entidad JPA a dominio Base de datos -> Dominio */
  public Pedido toDomain(PedidoEntity entity) {
    java.util.List<PedidoItem> items =
        entity.getItems().stream().map(this::toItemDomain).collect(Collectors.toList());

    return PedidoFactory.recrearPedidoDesdeRepositorio(
        new PedidoId(entity.getId()),
        items,
        mapEstado(entity.getEstado()),
        entity.getFechaCreacion(),
        entity.getFechaActualizacion());
  }

  /** Convierte item del dominio a entidad */
  private PedidoItemEntity toItemEntity(PedidoItem item) {
    return new PedidoItemEntity(
        item.getProducto().getId().getValue(), // ← MAPPER: ProductoId → String
        item.getProducto().getNombre(),
        item.getCantidad(),
        item.getPrecioUnitario());
  }

  /** Convierte item de entidad a dominio */
  private PedidoItem toItemDomain(PedidoItemEntity entity) {
    ProductoId productoId = new ProductoId(entity.getProductoId()); // ← MAPPER: String → ProductoId
    Producto producto =
        new Producto(
            productoId, entity.getNombreProducto(), entity.getPrecioUnitario());

    return new PedidoItem(producto, entity.getCantidad());
  }

  /** Mapea estado del dominio a entidad */
  private EstadoPedidoEntity mapEstado(EstadoPedido estado) {
    return switch (estado) {
      case PENDIENTE -> EstadoPedidoEntity.NUEVO;
      case COMPLETADO -> EstadoPedidoEntity.COMPLETADO;
      case CANCELADO -> EstadoPedidoEntity.CANCELADO;
    };
  }

  /** Mapea estado de entidad a dominio */
  private EstadoPedido mapEstado(EstadoPedidoEntity estado) {
    return switch (estado) {
      case NUEVO -> EstadoPedido.PENDIENTE;
      case PROCESANDO -> EstadoPedido.PENDIENTE; // Mapeamos a PENDIENTE si no existe en dominio
      case COMPLETADO -> EstadoPedido.COMPLETADO;
      case CANCELADO -> EstadoPedido.CANCELADO;
    };
  }
}
