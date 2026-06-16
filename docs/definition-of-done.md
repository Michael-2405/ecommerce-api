# Definition of Done — ecommerce-api

## Objetivo

Aprender Java 21 y Quarkus 3 construyendo una API REST de e-commerce completa y funcional,
demostrable como proyecto de portafolio.

---

## DoD v1 — ✅ COMPLETADO (Junio 2026)

Alcance mínimo para considerar Java + Quarkus "aprendido" para uso profesional básico.

### Criterios

- [x] **Order completo**
    - [x] Entidades `Order` y `OrderItem` con relaciones JPA (`@ManyToOne`, `@OneToMany`)
    - [x] `OrderRequest` / `OrderResponse` DTOs con validaciones
    - [x] `OrderService.create()` con `@Transactional`:
        - [x] Validar que el cliente existe (404 si no)
        - [x] Validar que cada producto existe (404 si no)
        - [x] Validar stock suficiente por producto (409 si insuficiente)
        - [x] Descontar stock al crear la orden
        - [x] Calcular `unitPrice` desde el producto (no del request)
        - [x] Calcular `total` como suma de `quantity × unitPrice` por item
    - [x] `OrderResource` con GET, POST, DELETE y `PATCH /{id}/status`

- [x] **Profiles de configuración**
    - [x] `application.properties` con perfiles `%dev` y `%prod`
    - [x] Al menos una diferencia real por perfil:
        - SQL logging: habilitado en dev, deshabilitado en prod
        - Swagger UI: habilitado en dev, deshabilitado en prod
        - Log level: DEBUG en dev, WARNING en prod
        - Schema generation: `update` en dev, `none` en prod
        - Datasource URL: `localhost` en dev, nombre de servicio Docker en prod

- [x] **Test de integración**
    - [x] `@QuarkusTest` + REST Assured
    - [x] Flujo completo de Order:
        1. Crear categoría → capturar ID
        2. Crear producto con stock conocido → capturar ID
        3. Crear cliente con email único (UUID) → capturar ID
        4. Crear orden → verificar total calculado correctamente
        5. Verificar que el stock se descontó en el producto

---

## Estado del proyecto al completar DoD v1

### Recursos implementados
| Recurso | CRUD | Validaciones | Soft Delete | Swagger |
|---|---|---|---|---|
| Category | ✅ | nombre único | ✅ | ✅ |
| Product | ✅ | precio > 0, categoría existe | ✅ | ✅ |
| Customer | ✅ | email único, formato email | ✅ | ✅ |
| Order | ✅ + PATCH status | stock, cliente/producto existen | ✅ | ✅ |

### Infraestructura
| Componente | Estado |
|---|---|
| BaseEntity con audit fields | ✅ |
| GlobalExceptionMapper (404/409/500) | ✅ |
| ErrorResponse DTO estándar | ✅ |
| Docker Compose + PostgreSQL 16 | ✅ |
| OpenAPI / Swagger UI | ✅ |
| GitHub con commits semánticos | ✅ |
| Profiles dev/prod | ✅ |
| Test de integración | ✅ |

---

## Backlog v2 — fuera del alcance de DoD v1

Ideas surgidas durante el desarrollo. No implementar hasta completar otros objetivos de aprendizaje.

### Refactor
- [ ] `GenericService<T, Req, Res>` con Java Generics para eliminar código repetido entre servicios

### Testing
- [ ] Tests para Category, Product y Customer
- [ ] Test de validación de stock insuficiente
- [ ] Test de transición de estados de Order

### Base de datos
- [ ] Integrar Flyway para migrations y datos iniciales reproducibles
- [ ] Cambiar `hibernate-orm.database.generation` a `none` y gestionar schema con Flyway

### Seguridad
- [ ] Autenticación con JWT (SmallRye JWT)
- [ ] Endpoints protegidos por rol (ADMIN / CUSTOMER)

### Funcionalidades
- [ ] Paginación en listados (`?page=0&size=20`)
- [ ] Filtros adicionales en productos (precio mín/máx, stock disponible)
- [ ] Endpoint para reactivar recursos eliminados (soft delete reversible)
- [ ] Historial de cambios de estado en Order

### Documentación
- [ ] README con diagrama de entidades (Mermaid)
- [ ] Ejemplos de requests/responses en README
- [ ] Documentar cómo correr en modo producción

---

## Conceptos dominados al completar DoD v1

| Concepto | Nivel |
|---|---|
| Clases, objetos, herencia, interfaces | ✅ Sólido |
| Enums | ✅ Sólido |
| Collections (List, stream, map, filter) | ✅ Sólido |
| BigDecimal para valores monetarios | ✅ Sólido |
| Text blocks y `.formatted()` | ✅ Sólido |
| Entidades JPA con Panache | ✅ Sólido |
| Relaciones @ManyToOne / @OneToMany | ✅ Sólido |
| DTOs + Factory Method pattern | ✅ Sólido |
| Inyección de dependencias (@Inject, @ApplicationScoped) | ✅ Sólido |
| Endpoints REST (JAX-RS) | ✅ Sólido |
| Validaciones (Jakarta Validation) | ✅ Sólido |
| Transacciones (@Transactional, dirty checking) | ✅ Sólido |
| Manejo global de errores (ExceptionMapper) | ✅ Sólido |
| Profiles de configuración (dev/prod) | ✅ Sólido |
| Testing con @QuarkusTest + REST Assured | ✅ Introducción |
| Java Generics | ⬜ Pendiente (v2) |
| Flyway / Migrations | ⬜ Pendiente (v2) |
| JWT / Seguridad | ⬜ Pendiente (v2) |
