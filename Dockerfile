# 🐳 Dockerfile para Pedidos DDD - Arquitectura Hexagonal
# Imagen optimizada para Spring Boot con JPA

# Usar OpenJDK 17 (base Alpine para menor tamaño)
FROM openjdk:17-jdk-alpine

# Información del mantenedor
LABEL maintainer="Arquitectura Hexagonal Demo"
LABEL description="Pedidos DDD con Spring Boot, JPA y Arquitectura Hexagonal"

# Crear directorio de trabajo
WORKDIR /app

# Copiar archivos de Gradle
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Dar permisos de ejecución a gradlew
RUN chmod +x ./gradlew

# Copiar código fuente
COPY src src

# Construir la aplicación
RUN ./gradlew build -x test

# Exponer puerto 8080
EXPOSE 8080

# Variables de entorno por defecto
ENV SPRING_PROFILES_ACTIVE=docker
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "build/libs/pedidosddd-0.0.1-SNAPSHOT.jar"]

# Health check para verificar que la aplicación esté funcionando
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1 