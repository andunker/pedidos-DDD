package com.example.pedidosddd.application.ports.in;

import com.example.pedidosddd.domain.model.Pedido;
import com.example.pedidosddd.domain.model.PedidoId;

/**
 * PUERTO DE ENTRADA - Consultar Pedido Use Case
 *
 * <p>¿Qué define? El contrato para consultar un pedido ¿Quién lo implementa? PedidoUseCasesImpl
 * (capa aplicación) ¿Quién lo usa? Adaptadores de entrada (Web, CLI)
 *
 * <p>En Hexagonal: Define qué se puede hacer, no cómo se hace
 *
 * <p>🎯 DDD: Trabaja con Value Objects del dominio, NO primitivos
 */
public interface ConsultarPedidoUseCase {

  /**
   * Consulta un pedido por su ID
   *
   * @param pedidoId Value Object PedidoId del dominio
   * @return Pedido encontrado
   * @throws RuntimeException si el pedido no existe
   */
  Pedido consultarPedidoPorId(PedidoId pedidoId);
}
