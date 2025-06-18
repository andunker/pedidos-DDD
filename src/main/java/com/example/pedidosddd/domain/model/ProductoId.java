package com.example.pedidosddd.domain.model;

import java.util.Objects;
import java.util.UUID;

/**
 * VALUE OBJECT - Identidad única de un Producto
 * Características de un Value Object:
 * - Inmutable (campos final)
 * - Igualdad por valor (equals/hashCode)
 * - Sin identidad propia
 * 
 * 🎯 DDD: Los IDs deberían ser Value Objects, no primitivos
 */
public class ProductoId {
    private final String value;

    public ProductoId() {
        this.value = UUID.randomUUID().toString();
    }

    public ProductoId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ProductoId cannot be null or empty");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductoId that = (ProductoId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "ProductoId{" + "value='" + value + '\'' + '}';
    }
} 