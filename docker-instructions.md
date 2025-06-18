# 🐳 **Instrucciones Docker - Pedidos DDD Arquitectura Hexagonal**

## 🚀 **Opciones de Ejecución**

### **1. Con H2 (Desarrollo Rápido)**
```bash
# Construir y ejecutar con H2 en memoria
docker-compose up --build

# Solo ejecutar (si ya está construido)
docker-compose up

# En background
docker-compose up -d
```

**Accesos:**
- **Aplicación**: http://localhost:8080
- **API REST**: http://localhost:8080/api/pedidos
- **H2 Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:pedidosdb`
  - User: `sa`
  - Password: (vacío)
- **Health Check**: http://localhost:8080/actuator/health

### **2. Con PostgreSQL (Más Realista)**
```bash
# Ejecutar con PostgreSQL
docker-compose --profile postgresql up --build

# Solo PostgreSQL + aplicación
docker-compose --profile postgresql up -d
```

**Accesos:**
- **Aplicación**: http://localhost:8080
- **PostgreSQL**: localhost:5432
  - Database: `pedidosdb`
  - User: `pedidos_user`
  - Password: `pedidos_pass`

### **3. Con Herramientas de Administración**
```bash
# Ejecutar con Adminer (gestión de BD)
docker-compose --profile tools --profile postgresql up -d
```

**Accesos adicionales:**
- **Adminer**: http://localhost:8081
  - Server: `postgres`
  - Username: `pedidos_user`
  - Password: `pedidos_pass`
  - Database: `pedidosdb`

---

## 🔧 **Comandos Útiles**

### **Gestión de Contenedores:**
```bash
# Ver logs de la aplicación
docker-compose logs -f pedidos-app

# Ver logs de PostgreSQL
docker-compose logs -f postgres

# Parar todos los servicios
docker-compose down

# Parar y eliminar volúmenes
docker-compose down -v

# Reconstruir solo la aplicación
docker-compose build pedidos-app
```

### **Debugging:**
```bash
# Entrar al contenedor de la aplicación
docker exec -it pedidos-hexagonal sh

# Ver variables de entorno
docker exec pedidos-hexagonal env

# Verificar conectividad a PostgreSQL
docker exec -it pedidos-postgres psql -U pedidos_user -d pedidosdb
```

---

## 🎯 **Demostrando Arquitectura Hexagonal**

### **🔄 Cambio de H2 a PostgreSQL:**
Este proyecto demuestra la **flexibilidad de la Arquitectura Hexagonal**:

1. **Mismo código de dominio** (sin cambios)
2. **Mismos use cases** (sin cambios)  
3. **Mismo adaptador JPA** (sin cambios)
4. **Solo cambia la configuración** de base de datos

```bash
# H2 en memoria
docker-compose up

# PostgreSQL real (¡mismo código!)
docker-compose --profile postgresql up
```

### **🏗️ Beneficios Hexagonales Demostrados:**
- ✅ **Portabilidad**: H2 → PostgreSQL sin tocar código
- ✅ **Testabilidad**: Usar H2 para tests, PostgreSQL para producción
- ✅ **Flexibilidad**: Cambiar tecnología sin afectar el núcleo
- ✅ **Escalabilidad**: De desarrollo a producción transparentemente

---

## 📊 **Testing en Docker**

### **Probar la API:**
```bash
# Crear pedido
curl -X POST http://localhost:8080/api/pedidos

# Agregar producto
curl -X POST http://localhost:8080/api/pedidos/{PEDIDO_ID}/productos \
  -H "Content-Type: application/json" \
  -d '{
    "productoId": "LAPTOP-001",
    "nombreProducto": "Laptop Gaming",
    "precio": 1200.00,
    "cantidad": 1
  }'
```

### **Ver Datos Persistidos:**

**Con H2:**
- Ir a http://localhost:8080/h2-console
- Ejecutar: `SELECT * FROM pedidos;`

**Con PostgreSQL:**
- Ir a http://localhost:8081 (Adminer)
- O usar psql: `docker exec -it pedidos-postgres psql -U pedidos_user -d pedidosdb`

---

## 🚀 **Para Producción**

### **Variables de Entorno:**
```bash
# docker-compose.prod.yml
version: '3.8'
services:
  pedidos-app:
    build: .
    environment:
      - SPRING_PROFILES_ACTIVE=prod,postgresql
      - SPRING_DATASOURCE_URL=jdbc:postgresql://prod-db:5432/pedidosdb
      - SPRING_DATASOURCE_USERNAME=${DB_USER}
      - SPRING_DATASOURCE_PASSWORD=${DB_PASSWORD}
      - JAVA_OPTS=-Xmx1024m -Xms512m
```

**🎯 Esta configuración Docker demuestra perfectamente cómo la Arquitectura Hexagonal permite flexibilidad total en el despliegue!** 