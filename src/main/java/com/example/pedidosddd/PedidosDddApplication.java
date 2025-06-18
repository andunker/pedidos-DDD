package com.example.pedidosddd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(
    basePackages = "com.example.pedidosddd.infrastructure.adapters.out.persistence")
public class PedidosDddApplication {

  public static void main(String[] args) {
    SpringApplication.run(PedidosDddApplication.class, args);
  }
}
