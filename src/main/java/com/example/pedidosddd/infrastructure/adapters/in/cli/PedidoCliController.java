package com.example.pedidosddd.infrastructure.adapters.in.cli;

import com.example.pedidosddd.application.ports.in.AgregarProductoUseCase;
import com.example.pedidosddd.application.ports.in.CrearPedidoUseCase;
import com.example.pedidosddd.domain.model.PedidoId;
import com.example.pedidosddd.domain.model.ProductoId;
import java.math.BigDecimal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * SEGUNDO ADAPTADOR DE ENTRADA - CLI
 *
 * <p>¡Esta es la MAGIA de Arquitectura Hexagonal!
 *
 * <p>✅ Mismo dominio, mismo use case ✅ DIFERENTE adaptador (CLI en lugar de Web) ✅ Sin tocar el
 * núcleo de la aplicación
 *
 * <p>Esto demuestra que puedo tener: - API REST - Línea de comandos - Interfaz gráfica - Tests
 * automatizados - Todos usando los MISMOS use cases
 */
@Component
public class PedidoCliController implements CommandLineRunner {

  // Los MISMOS puertos que usa el Web Controller
  private final CrearPedidoUseCase crearPedidoUseCase;
  private final AgregarProductoUseCase agregarProductoUseCase;

  public PedidoCliController(
      CrearPedidoUseCase crearPedidoUseCase, AgregarProductoUseCase agregarProductoUseCase) {
    this.crearPedidoUseCase = crearPedidoUseCase;
    this.agregarProductoUseCase = agregarProductoUseCase;
  }

  @Override
  public void run(String... args) throws Exception {
    System.out.println("\n🔶 DEMO: Arquitectura Hexagonal - Adaptador CLI");
    System.out.println("===============================================");

    // 1. Crear pedido desde CLI
    System.out.println("1️⃣ Creando pedido desde CLI...");
    PedidoId pedidoId = crearPedidoUseCase.crearNuevoPedido();
    System.out.println("   ✅ Pedido creado: " + pedidoId.getValue());

    // 2. Agregar producto desde CLI
    // 🎯 ADAPTADOR CLI: Convertir String → Value Objects del dominio
    System.out.println("\n2️⃣ Agregando producto desde CLI...");
    ProductoId productoId = new ProductoId("LAPTOP-001");
    AgregarProductoUseCase.AgregarProductoCommand command =
        new AgregarProductoUseCase.AgregarProductoCommand(
            pedidoId, productoId, "Laptop Gaming", new BigDecimal("1200.00"), 1);

    agregarProductoUseCase.agregarProducto(command);
    System.out.println("   ✅ Producto agregado correctamente");

    System.out.println("\n🎯 RESULTADO: Misma lógica, diferentes interfaces!");
    System.out.println("   📱 Web API: localhost:8080/api/pedidos");
    System.out.println("   💻 CLI: Esta misma demostración");
    System.out.println("   🔧 Arquitectura Hexagonal funcionando!\n");
  }
}
