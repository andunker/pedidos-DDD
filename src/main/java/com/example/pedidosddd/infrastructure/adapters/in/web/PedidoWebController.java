package com.example.pedidosddd.infrastructure.adapters.in.web;

import com.example.pedidosddd.application.ports.in.AgregarProductoUseCase;
import com.example.pedidosddd.application.ports.in.ConsultarPedidoUseCase;
import com.example.pedidosddd.application.ports.in.CrearPedidoUseCase;
import com.example.pedidosddd.domain.model.Pedido;
import com.example.pedidosddd.domain.model.PedidoId;
import com.example.pedidosddd.domain.model.ProductoId;
import java.math.BigDecimal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * ADAPTADOR DE ENTRADA WEB - Arquitectura Hexagonal
 *
 * <p>¿Qué hace? Traduce las peticiones HTTP a llamadas de Use Cases ¿Qué NO hace? No tiene lógica
 * de negocio, solo traduce y delega
 *
 * <p>En Hexagonal: Controller -> Puerto de Entrada -> Use Case -> Puerto de Salida -> Adaptador de
 * Salida
 */
@RestController
@RequestMapping("/api/pedidos")
public class PedidoWebController {

  // Solo conoce los PUERTOS DE ENTRADA, no las implementaciones
  private final CrearPedidoUseCase crearPedidoUseCase;
  private final AgregarProductoUseCase agregarProductoUseCase;
  private final ConsultarPedidoUseCase consultarPedidoUseCase;

  public PedidoWebController(
      CrearPedidoUseCase crearPedidoUseCase,
      AgregarProductoUseCase agregarProductoUseCase,
      ConsultarPedidoUseCase consultarPedidoUseCase) {
    this.crearPedidoUseCase = crearPedidoUseCase;
    this.agregarProductoUseCase = agregarProductoUseCase;
    this.consultarPedidoUseCase = consultarPedidoUseCase;
  }

  /** Endpoint: Crear un nuevo pedido HTTP POST /api/pedidos */
  @PostMapping
  public ResponseEntity<CrearPedidoResponse> crearPedido() {
    // Delega al Use Case
    PedidoId pedidoId = crearPedidoUseCase.crearNuevoPedido();

    // Traduce la respuesta del dominio a HTTP
    return ResponseEntity.ok(new CrearPedidoResponse(pedidoId.getValue()));
  }

  /**
   * Endpoint: Agregar producto a un pedido HTTP POST /api/pedidos/{pedidoId}/productos 🎯
   * ADAPTADOR: Convierte String (HTTP) → PedidoId (Dominio)
   */
  @PostMapping("/{pedidoId}/productos")
  public ResponseEntity<String> agregarProducto(
      @PathVariable String pedidoId, @RequestBody AgregarProductoRequest request) {

    try {
      // 1. ADAPTADOR: Convertir String HTTP → Value Object del dominio
      PedidoId id = new PedidoId(pedidoId);

      // 2. ADAPTADOR: Crear Command con objetos del dominio
      ProductoId productoId = new ProductoId(request.productoId());
      AgregarProductoUseCase.AgregarProductoCommand command =
          new AgregarProductoUseCase.AgregarProductoCommand(
              id, // ← 🎯 PedidoId (Value Object), no String
              productoId, // ← 🎯 ProductoId (Value Object), no String
              request.nombreProducto(),
              request.precio(),
              request.cantidad());

      // 3. DELEGAR al Use Case
      agregarProductoUseCase.agregarProducto(command);

      return ResponseEntity.ok("Producto agregado correctamente");
    } catch (IllegalArgumentException e) {
      // Puede ser por ID inválido (adaptador) o validación de command (dominio)
      return ResponseEntity.badRequest().body("Error: " + e.getMessage());
    }
  }

  /**
   * Endpoint: Consultar un pedido por ID HTTP GET /api/pedidos/{pedidoId} 🎯 ADAPTADOR: Convierte
   * String (HTTP) → PedidoId (Dominio)
   */
  @GetMapping("/{pedidoId}")
  public ResponseEntity<ConsultarPedidoResponse> consultarPedido(@PathVariable String pedidoId) {
    try {
      // 1. ADAPTADOR: Convertir String HTTP → Value Object del dominio
      PedidoId id = new PedidoId(pedidoId);

      // 2. DELEGAR al Use Case con objeto del dominio
      Pedido pedido = consultarPedidoUseCase.consultarPedidoPorId(id);

      // Traduce la respuesta del dominio a HTTP
      ConsultarPedidoResponse response =
          new ConsultarPedidoResponse(
              pedido.getId().getValue(),
              pedido.getEstado().name(),
              pedido.calcularTotal(),
              pedido.getItems().stream()
                  .map(
                      item ->
                          new ProductoResponse(
                              item.getProducto().getId().getValue(), // ← ADAPTADOR: ProductoId → String
                              item.getProducto().getNombre(),
                              item.getProducto().getPrecio(),
                              item.getCantidad()))
                  .toList());

      return ResponseEntity.ok(response);
    } catch (IllegalArgumentException e) {
      // Puede ser por ID inválido (adaptador) o pedido no encontrado (dominio)
      return ResponseEntity.notFound().build();
    }
  }

  // DTOs para la capa web (adaptador)
  public record CrearPedidoResponse(String pedidoId) {}

  public record AgregarProductoRequest(
      String productoId, String nombreProducto, BigDecimal precio, int cantidad) {}

  public record ConsultarPedidoResponse(
      String pedidoId,
      String estado,
      BigDecimal total,
      java.util.List<ProductoResponse> productos) {}

  public record ProductoResponse(
      String productoId, String nombre, BigDecimal precio, int cantidad) {}
}
