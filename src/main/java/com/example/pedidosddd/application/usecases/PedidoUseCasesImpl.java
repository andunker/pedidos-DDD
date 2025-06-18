package com.example.pedidosddd.application.usecases;

import com.example.pedidosddd.application.ports.in.AgregarProductoUseCase;
import com.example.pedidosddd.application.ports.in.ConsultarPedidoUseCase;
import com.example.pedidosddd.application.ports.in.CrearPedidoUseCase;
import com.example.pedidosddd.application.ports.out.PedidoRepositoryPort;
import com.example.pedidosddd.domain.model.*;
import org.springframework.stereotype.Service;

/**
 * IMPLEMENTACIÓN DE USE CASES - El núcleo de la capa de aplicación En Arquitectura Hexagonal, aquí
 * van las implementaciones de todos los puertos de entrada
 *
 * <p>¿Qué hace? Orquesta las operaciones del dominio sin lógica de negocio propia
 */
@Service
public class PedidoUseCasesImpl
    implements CrearPedidoUseCase, AgregarProductoUseCase, ConsultarPedidoUseCase {

  private final PedidoRepositoryPort pedidoRepository;

  public PedidoUseCasesImpl(PedidoRepositoryPort pedidoRepository) {
    this.pedidoRepository = pedidoRepository;
  }

  /**
   * USE CASE: Crear un nuevo pedido Paso 1: Usar la factory del dominio para crear el pedido Paso
   * 2: Guardarlo usando el puerto de salida Paso 3: Retornar el ID
   */
  @Override
  public PedidoId crearNuevoPedido() {
    // 1. El dominio se encarga de crear el pedido
    Pedido nuevoPedido = PedidoFactory.crearNuevoPedido();

    // 2. Usamos el puerto de salida para persistir
    Pedido pedidoGuardado = pedidoRepository.save(nuevoPedido);

    // 3. Retornamos el ID
    return pedidoGuardado.getId();
  }

  /**
   * USE CASE: Agregar producto a un pedido 🎯 DDD: Recibe Command con Value Objects del dominio, no
   * primitivos Paso 1: Buscar el pedido (ya tenemos PedidoId del dominio) Paso 2: Crear el producto
   * y agregarlo (lógica de dominio) Paso 3: Guardar los cambios
   */
  @Override
  public void agregarProducto(AgregarProductoCommand command) {
    // 1. Buscar directamente - ya tenemos el Value Object del dominio
    Pedido pedido =
        pedidoRepository
            .findById(command.pedidoId())
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "Pedido no encontrado: " + command.pedidoId().getValue()));

    // 2. Crear el producto y agregarlo (lógica de dominio)
    Producto producto =
        new Producto(command.productoId(), command.nombreProducto(), command.precioProducto());

    // El dominio se encarga de la lógica de agregar el producto
    pedido.agregarProducto(producto, command.cantidad());

    // 3. Guardar usando el puerto de salida
    pedidoRepository.save(pedido);
  }

  /**
   * USE CASE: Consultar un pedido por ID 🎯 DDD: Recibe Value Object del dominio, no primitivos
   * Paso 1: Buscar usando el puerto de salida (ya recibimos PedidoId del dominio) Paso 2: Retornar
   * el pedido encontrado
   */
  @Override
  public Pedido consultarPedidoPorId(PedidoId pedidoId) {
    // 1. Buscar directamente - ya tenemos el Value Object del dominio
    return pedidoRepository
        .findById(pedidoId)
        .orElseThrow(
            () -> new IllegalArgumentException("Pedido no encontrado: " + pedidoId.getValue()));
  }
}
