# Bitácora de Aprendizaje — Java + Quarkus

**Proyecto:** ecommerce-api  
**Período:** Junio 2026  
**Stack:** Java 21, Quarkus 3.36.2, PostgreSQL 16, Docker

---

## Tabla de contenidos

1. [Java — Fundamentos](#1-java--fundamentos)
2. [Programación Orientada a Objetos](#2-programación-orientada-a-objetos)
3. [Java Moderno](#3-java-moderno)
4. [Quarkus — Conceptos base](#4-quarkus--conceptos-base)
5. [JPA y Hibernate Panache](#5-jpa-y-hibernate-panache)
6. [Anotaciones de referencia](#6-anotaciones-de-referencia)
7. [Tipos de datos](#7-tipos-de-datos)
8. [Transacciones](#8-transacciones)
9. [Manejo de errores](#9-manejo-de-errores)
10. [Testing](#10-testing)
11. [Errores cometidos y lecciones aprendidas](#11-errores-cometidos-y-lecciones-aprendidas)

---

## 1. Java — Fundamentos

### Variables y tipos primitivos
```java
int stock = 50;           // entero
double precio = 19.99;    // decimal (evitar para dinero)
boolean activo = true;    // verdadero/falso
char inicial = 'M';       // un solo carácter
```

### BigDecimal — para dinero
Nunca usar `double` para valores monetarios — tiene errores de precisión.
```java
BigDecimal precio = new BigDecimal("999.99");
BigDecimal subtotal = precio.multiply(BigDecimal.valueOf(cantidad));
BigDecimal total = BigDecimal.ZERO;
total = total.add(subtotal); // BigDecimal es inmutable — reasignar siempre
```

### String — text blocks (Java 15+)
```java
String json = """
    {
        "name": "iPhone",
        "price": 999.99
    }
    """;

// Interpolación con .formatted()
String body = """
    { "categoryId": %d, "name": "%s" }
    """.formatted(categoryId, nombre);
// %d = entero, %s = String, %f = decimal
```

### Condicionales
```java
if (stock < cantidad) {
    throw new BusinessException("Stock insuficiente");
}

// Ternario — if/else en una línea
String mensaje = activo ? "Activo" : "Inactivo";

// Switch moderno (Java 14+)
String descripcion = switch (status) {
    case PENDING  -> "Pendiente";
    case PAID     -> "Pagado";
    case SHIPPED  -> "Enviado";
    case DELIVERED -> "Entregado";
};
```

### Bucles
```java
// for-each — iterar colecciones (el más usado en este proyecto)
for (OrderItemRequest itemRequest : request.items) {
    // procesar cada item
}

// for clásico — cuando necesitas el índice
for (int i = 0; i < lista.size(); i++) { }

// while — cuando no sabes cuántas iteraciones
while (intentos < 3) { intentos++; }
```

### Métodos
```java
// Anatomía: [modificador] [tipo retorno] nombre(parámetros)
public BigDecimal calcularTotal(List<OrderItem> items) {
    BigDecimal total = BigDecimal.ZERO;
    for (OrderItem item : items) {
        total = total.add(item.unitPrice.multiply(BigDecimal.valueOf(item.quantity)));
    }
    return total;
}

// void — sin retorno
public void descontarStock(Product product, int cantidad) {
    product.stock -= cantidad;
}
```

---

## 2. Programación Orientada a Objetos

### Clases y objetos
```java
// Clase = plantilla/molde
public class Product extends BaseEntity {
    public String name;
    public BigDecimal price;
    public int stock;
}

// Objeto = instancia de la clase
Product product = new Product();
product.name = "iPhone 15";
product.price = new BigDecimal("999.99");
```

### Herencia — `extends`
Una clase puede heredar campos y métodos de otra.
```java
// Clase padre — campos comunes a todas las entidades
@MappedSuperclass
public abstract class BaseEntity extends PanacheEntity {
    public boolean active = true;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
}

// Clase hija — hereda active, createdAt, updatedAt y los lifecycle hooks
public class Product extends BaseEntity {
    public String name;  // agrega sus propios campos
    public BigDecimal price;
}
```

**Lección aprendida:** Poner en `BaseEntity` solo lo que es verdaderamente común a TODAS las entidades. `name` y `description` NO van en la base porque cada entidad tiene constraints diferentes (`unique`, longitudes distintas).

### Interfaces
Contrato que garantiza que una clase tendrá ciertos métodos.
```java
public interface Notificable {
    void enviarNotificacion(String mensaje);
}

public class EmailService implements Notificable {
    public void enviarNotificacion(String mensaje) {
        // implementación específica
    }
}
```

### Enum
Tipo con un conjunto fijo de valores.
```java
public enum OrderStatus {
    PENDING,
    PAID,
    SHIPPED,
    DELIVERED
}

// Uso
order.status = OrderStatus.PENDING;

// En JPA — guardar como texto, no como número
@Enumerated(EnumType.STRING)
@Column(name = "status")
public OrderStatus status;
```

**Por qué `EnumType.STRING`:** Si se guarda como número (default) y se reordena el enum, los datos existentes en BD se corrompen. Como texto es legible y resistente a cambios.

### Patrón Factory Method
Método estático que encapsula la creación de un objeto — usado en todos los DTOs de Response.
```java
public class CategoryResponse {
    public Long id;
    public String name;

    // Factory method — único lugar donde ocurre la conversión entidad → DTO
    public static CategoryResponse from(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.id   = category.id;
        response.name = category.name;
        return response;
    }
}

// Uso
CategoryResponse dto = CategoryResponse.from(category);
```

**Por qué:** Si mañana cambia la estructura, hay un solo lugar donde modificar la conversión.

---

## 3. Java Moderno

### Stream API
Procesamiento funcional de colecciones.
```java
// Convertir lista de entidades a lista de DTOs
List<CategoryResponse> responses = Category.listAll()
    .stream()
    .map(entity -> CategoryResponse.from((Category) entity))
    .toList();

// Filtrar + transformar
List<Product> activos = products.stream()
    .filter(p -> p.active)
    .sorted(Comparator.comparing(p -> p.name))
    .toList();
```

### Optional
Evita NullPointerException en consultas que pueden no encontrar resultado.
```java
// Sin Optional — peligroso
Category category = Category.findById(id); // puede ser null
category.name; // NullPointerException si no existe

// Con Optional — seguro
Category category = (Category) Category.findByIdOptional(id)
    .orElseThrow(() -> new ResourceNotFoundException("Category", id));
```

### UUID para tests
Genera un identificador único universal — útil para evitar conflictos en tests.
```java
String email = "test-" + UUID.randomUUID() + "@test.com";
// → "test-550e8400-e29b-41d4-a716-446655440000@test.com"
```

---

## 4. Quarkus — Conceptos base

### ¿Qué es Quarkus?
Framework Java diseñado para cloud y containers. Dos ventajas clave sobre Spring Boot:
- Arranque ultrarrápido (ms en modo nativo vs segundos)
- Bajo consumo de memoria

### Dev mode con live reload
```bash
./mvnw quarkus:dev
# o
quarkus dev
```
Quarkus recompila automáticamente al guardar cualquier archivo. No hay que reiniciar.

### Inyección de dependencias — CDI
```java
@ApplicationScoped  // una sola instancia para toda la app (singleton)
public class CategoryService { }

// En el resource — Quarkus crea e inyecta el servicio automáticamente
@Inject
CategoryService categoryService;
// Nunca: new CategoryService() — Quarkus lo gestiona
```

### Configuración por perfil
```properties
# application.properties
quarkus.hibernate-orm.log.sql=false      # valor base

%dev.quarkus.hibernate-orm.log.sql=true  # sobreescribe en dev
%prod.quarkus.hibernate-orm.log.sql=false # sobreescribe en prod
```

Activar perfil:
```bash
./mvnw quarkus:dev                          # activa %dev automáticamente
./mvnw quarkus:dev -Dquarkus.profile=prod   # activa %prod
```

---

## 5. JPA y Hibernate Panache

### Entidad básica
```java
@Entity
@Table(name = "categories")
public class Category extends PanacheEntity {
    // PanacheEntity provee: id (Long), persist(), delete(), findAll(), findById()...

    @Column(name = "name", nullable = false, unique = true, length = 100)
    public String name;
}
```

### Lifecycle hooks
Se ejecutan automáticamente antes de operaciones de BD.
```java
@PrePersist   // justo antes del INSERT
public void prePersist() {
    createdAt = LocalDateTime.now();
}

@PreUpdate    // justo antes del UPDATE
public void preUpdate() {
    updatedAt = LocalDateTime.now();
}
```

### Queries con Panache
```java
// Buscar todos
Category.listAll();

// Buscar por campo
Category.list("active", true);

// Query JPQL con parámetro posicional
Category.find("LOWER(name)", nombre.toLowerCase()).firstResult();

// Con condición compuesta
Product.list("active = true AND category.id = ?1", categoryId);

// Optional — para manejar ausencia
Category.findByIdOptional(id).orElseThrow(() -> new ResourceNotFoundException(...));
```

### Relaciones entre entidades

#### @ManyToOne — el lado que TIENE la foreign key
```java
// En OrderItem — tiene las columnas order_id y product_id
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "order_id", nullable = false)
public Order order;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "product_id", nullable = false)
public Product product;
```

#### @OneToMany — el lado que NO tiene la foreign key
```java
// En Order — no tiene columna extra, solo referencia la relación
@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
public List<OrderItem> items;
// mappedBy = "order" → nombre del campo @ManyToOne en OrderItem
// CascadeType.ALL → persist/delete se propaga a los items automáticamente
```

#### Regla mental
```
¿Esta entidad tiene la FK en su tabla?
  SÍ → @ManyToOne + @JoinColumn(name = "xxx_id")
  NO → @OneToMany(mappedBy = "nombre_del_campo_en_el_otro_lado")
```

#### FetchType
```java
FetchType.LAZY  // carga la relación solo cuando se accede (recomendado)
FetchType.EAGER // carga la relación siempre, aunque no se use (evitar)
```

### Dirty checking
Hibernate detecta cambios en entidades gestionadas dentro de una transacción y los sincroniza automáticamente con la BD al cerrarla. No es necesario llamar `.persist()` en updates.
```java
@Transactional
public void descontarStock(Long id, int cantidad) {
    Product product = Product.findById(id);
    product.stock -= cantidad; // Hibernate detecta el cambio y hace UPDATE automático
    // NO necesitas: product.persist() ni product.update()
}
```

---

## 6. Anotaciones de referencia

### JPA / Hibernate
| Anotación | Descripción |
|---|---|
| `@Entity` | Marca la clase como tabla en BD |
| `@Table(name = "...")` | Define el nombre de la tabla |
| `@Column(...)` | Configura una columna |
| `@MappedSuperclass` | Clase base que no genera tabla propia |
| `@ManyToOne` | Relación muchos-a-uno (el lado con FK) |
| `@OneToMany` | Relación uno-a-muchos (el lado sin FK) |
| `@JoinColumn(name = "...")` | Define el nombre de la columna FK |
| `@Enumerated(EnumType.STRING)` | Guarda enum como texto en BD |
| `@PrePersist` | Hook ejecutado antes del INSERT |
| `@PreUpdate` | Hook ejecutado antes del UPDATE |

### Quarkus / CDI
| Anotación | Descripción |
|---|---|
| `@ApplicationScoped` | Singleton — una instancia por app |
| `@Inject` | Inyecta una dependencia gestionada por Quarkus |
| `@Transactional` | Abre transacción al entrar, commit al salir, rollback si hay excepción |

### JAX-RS (endpoints REST)
| Anotación | Descripción |
|---|---|
| `@Path("/api/...")` | Define la URL del recurso o método |
| `@GET` `@POST` `@PUT` `@DELETE` `@PATCH` | Verbo HTTP del endpoint |
| `@Produces(MediaType.APPLICATION_JSON)` | El response es JSON |
| `@Consumes(MediaType.APPLICATION_JSON)` | El request body es JSON |
| `@PathParam("id")` | Captura `{id}` de la URL |
| `@QueryParam("name")` | Captura `?name=valor` de la URL |
| `@Valid` | Activa las validaciones del DTO |

### Jakarta Validation
| Anotación | Descripción |
|---|---|
| `@NotBlank` | No nulo y no vacío (para Strings) |
| `@NotNull` | No nulo (para cualquier tipo) |
| `@Size(min, max)` | Longitud del String |
| `@Min(value)` | Valor mínimo numérico |
| `@DecimalMin(value)` | Valor mínimo decimal |
| `@Email` | Formato de email válido |

### OpenAPI
| Anotación | Descripción |
|---|---|
| `@Tag(name, description)` | Agrupa endpoints en Swagger |
| `@Operation(summary, description)` | Documenta un endpoint |
| `@Schema(description, example)` | Documenta un campo de DTO |

### Testing
| Anotación | Descripción |
|---|---|
| `@QuarkusTest` | Levanta la app completa para tests |
| `@Test` | Marca un método como test |

---

## 7. Tipos de datos

| Tipo Java | Tipo SQL | Uso |
|---|---|---|
| `Long` | `BIGINT` | IDs de entidades |
| `String` | `VARCHAR(n)` | Texto con longitud máxima |
| `int` | `INTEGER` | Stock, cantidades |
| `BigDecimal` | `DECIMAL(p,s)` | Precios, totales — nunca `double` para dinero |
| `boolean` | `BOOLEAN` | Flags como `active` |
| `LocalDateTime` | `TIMESTAMP` | Fechas con hora |
| `List<T>` | — | Colecciones en memoria (relaciones @OneToMany) |

---

## 8. Transacciones

Una transacción garantiza que un conjunto de operaciones se ejecuta **todo o nada**.

```java
@Transactional
public OrderResponse create(OrderRequest request) {
    // Todo lo que ocurra aquí es una sola transacción
    // Si cualquier línea lanza una excepción → rollback automático
    // Nada se guarda en BD

    Order order = new Order();
    order.persist();

    for (OrderItemRequest itemRequest : request.items) {
        Product product = Product.findById(itemRequest.productId);
        product.stock -= itemRequest.quantity; // dirty checking → UPDATE automático

        OrderItem item = new OrderItem();
        item.order = order;
        item.persist(); // o CascadeType.ALL lo hace automáticamente
    }

    // Si llegamos aquí sin excepciones → COMMIT → todo se guarda
    return OrderResponse.from(order);
}
```

**Regla:** Todo método que modifica datos (INSERT, UPDATE, DELETE) debe tener `@Transactional`. Los métodos de solo lectura no lo necesitan.

---

## 9. Manejo de errores

### Excepciones personalizadas
```java
// Para errores de lógica de negocio → HTTP 409
public class BusinessException extends RuntimeException {
    public BusinessException(String message) { super(message); }
}

// Para recursos no encontrados → HTTP 404
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, Long id) {
        super(resource + " with id " + id + " not found");
    }
}
```

### ExceptionMapper global
Intercepta TODAS las excepciones no capturadas y las convierte en respuestas HTTP consistentes.
```java
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<RuntimeException> {
    @Override
    public Response toResponse(RuntimeException exception) {
        if (exception instanceof ResourceNotFoundException) {
            return Response.status(404).entity(ErrorResponse.of(404, "Not Found", exception.getMessage())).build();
        }
        if (exception instanceof BusinessException) {
            return Response.status(409).entity(ErrorResponse.of(409, "Conflict", exception.getMessage())).build();
        }
        return Response.status(500).entity(ErrorResponse.of(500, "Internal Server Error", "Unexpected error")).build();
    }
}
```

**Sin ExceptionMapper:** cualquier excepción no capturada devuelve HTTP 500 con el stack trace completo — información sensible que nunca debe llegar al cliente.

### DTO de error estándar
```java
public class ErrorResponse {
    public int status;
    public String error;
    public String message;
    public LocalDateTime timestamp;

    public static ErrorResponse of(int status, String error, String message) {
        ErrorResponse r = new ErrorResponse();
        r.status    = status;
        r.error     = error;
        r.message   = message;
        r.timestamp = LocalDateTime.now();
        return r;
    }
}
```

---

## 10. Testing

### Estructura básica
```java
@QuarkusTest
public class OrderFlowTest {

    @Test
    public void testCompleteOrderFlow() {
        // given — preparar datos
        // when — ejecutar acción
        // then — verificar resultado
    }
}
```

### REST Assured — sintaxis fluida
```java
// Crear recurso y capturar ID
Long categoryId = given()
    .contentType("application/json")
    .body(categoryBody)
.when()
    .post("/api/categories")
.then()
    .statusCode(201)
    .body("name", equalTo("Electronics"))  // verificar campo
    .extract().jsonPath().getLong("id");    // capturar valor

// Verificar estado
given()
    .when()
    .get("/api/products/" + productId)
    .then()
    .statusCode(200)
    .body("stock", equalTo(48));
```

### Evitar conflictos entre ejecuciones
```java
// Email único por ejecución — evita error de duplicado
String email = "test-" + UUID.randomUUID() + "@test.com";
```

### Comparación de tipos en Hamcrest
```java
equalTo(201)       // int
equalTo(48)        // int
equalTo(1999.98f)  // float — Hamcrest compara decimales como float por defecto
equalTo("PENDING") // String
```

---

## 11. Errores cometidos y lecciones aprendidas

| Error | Causa | Lección |
|---|---|---|
| `@OneToMany` con `@JoinColumn` | Confusión sobre qué lado tiene la FK | El lado con `@OneToMany` NO tiene FK — usa `mappedBy` |
| `@Column` en una relación | Confundir campo simple con relación | Para entidades: `@ManyToOne` + `@JoinColumn`. `@Column` solo para tipos primitivos y String |
| Variable con mismo nombre que la clase | `Category Category = new Category()` | Siempre camelCase para variables: `Category category = new Category()` |
| Factory method asignando a la entidad | `category.name = category.name` en lugar de `response.name = category.name` | El factory method construye el `response`, no modifica la entidad |
| `%d` para un String en `.formatted()` | Confusión de placeholders | `%d` = entero, `%s` = String, `%f` = decimal |
| Variables en text blocks sin `.formatted()` | `"categoryId": categoryId` — Java no interpola | Usar `.formatted(variable)` con `%d` o `%s` como placeholder |
| Puerto 5432 ocupado | Otro contenedor PostgreSQL usaba el mismo puerto | Fijar puerto explícito en `docker-compose.yml` y respetar el mapeo |
| `@PathParam("ID")` con mayúscula | No coincidía con `{id}` en `@Path` | El nombre en `@PathParam` debe coincidir exactamente con la variable en `@Path` |
| `findById` sobreescrito incorrectamente | Redefinir un método que ya existe en `PanacheEntity` con lógica incorrecta | Verificar qué métodos hereda `PanacheEntity` antes de reescribir |
| Package name reflejando estructura de carpetas | `com.michael.dev.web.ecommerce` | El package name identifica el proyecto globalmente — no la estructura local de archivos |
