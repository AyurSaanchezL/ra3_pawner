# Arquitectura del Sistema

## Arquitectura General

```
┌─────────────────────────────────────────────────────────┐
│                    CAPA PRESENTACIÓN                     │
│  (REST/MCP Endpoints)                                    │
│  ┌─────────────────────────────────────────────┐        │
│  │       McpServerController                    │        │
│  │  GET  /mcp/health                            │        │
│  │  GET  /mcp/tools                             │        │
│  │  POST /mcp/create_mascota                    │        │
│  │  POST /mcp/find_mascota_by_id                │        │
│  │  ...                                         │        │
│  └─────────────────────────────────────────────┘        │
└──────────────────────┬──────────────────────────────────┘
                       ↓
┌─────────────────────────────────────────────────────────┐
│                    CAPA DE SERVICIO                      │
│  (Lógica de Negocio + ORM)                              │
│  ┌─────────────────────────────────────────────┐        │
│  │    HibernateMascotaServiceImpl               │        │
│  │  @Service                                    │        │
│  │  @Transactional(readOnly=true)               │        │
│  │                                              │        │
│  │  + testEntityManager()                       │        │
│  │  + createMascota() @Transactional            │        │
│  │  + findMascotaById()                         │        │
│  │  + updateMascota() @Transactional (TODO)     │        │
│  │  + deleteMascota() @Transactional (TODO)     │        │
│  │  + findAll()                                 │        │
│  │  + findMascotasByTipo()                      │        │
│  │  ...                                         │        │
│  └─────────────────────────────────────────────┘        │
└──────────────────────┬──────────────────────────────────┘
                       ↓
┌─────────────────────────────────────────────────────────┐
│                CAPA DE PERSISTENCIA                      │
│  (JPA/Hibernate)                                        │
│  ┌──────────────────┐       ┌────────────────────┐      │
│  │  EntityManager   │       │  MascotaRepository │      │
│  │  (JPA)           │       │  (Spring Data JPA) │      │
│  │                  │       │                    │      │
│  │  persist()       │       │  findAll()         │      │
│  │  find()          │       │  findByTipoMascota()│      │
│  │  merge()         │       │  findBySexo()      │      │
│  │  remove()        │       │  ...               │      │
│  │  createQuery()   │       │                    │      │
│  └──────────────────┘       └────────────────────┘      │
└──────────────────────┬──────────────────────────────────┘
                       ↓
┌─────────────────────────────────────────────────────────┐
│                   CAPA DE MODELO                         │
│  (Entidades JPA)                                        │
│  ┌─────────────────────────────────────────────┐        │
│  │  @Entity Mascota                             │        │
│  │  @Table(name = "mascotas")                   │        │
│  │                                              │        │
│  │  @Id int numChip                             │        │
│  │  @Column String nombre                       │        │
│  │  @Column String tipoMascota                  │        │
│  │  ...                                         │        │
│  └─────────────────────────────────────────────┘        │
└──────────────────────┬──────────────────────────────────┘
                       ↓
┌─────────────────────────────────────────────────────────┐
│              BASE DE DATOS (MySQL)                       │
│  jdbc:mysql://localhost:3306/pawner_db                  │
│                                                         │
│  TABLE mascotas (                                       │
│    num_chip INT PRIMARY KEY,                             │
│    nombre VARCHAR(50),                                   │
│    tipo_mascota VARCHAR(50),                             │
│    ...                                                  │
│  )                                                      │
└─────────────────────────────────────────────────────────┘
```

## Patrones de Diseño

### 1. Repository Pattern

```
HibernateMascotaServiceImpl
        ↓
  MascotaRepository (interface)
        ↓
  JpaRepository<Mascota, Integer> (Spring Data)
        ↓
  Implementación automática por Spring
```

**Beneficios:**
- Abstracción del acceso a datos
- Código más testeable
- Métodos CRUD sin implementación manual

### 2. Service Layer Pattern

```
Controller → Service (interface) → ServiceImpl
```

**Responsabilidades:**
- **Controller**: Manejo HTTP, validación entrada
- **Service**: Lógica de negocio, transacciones
- **Repository**: Acceso a datos

### 3. DTO Pattern (Data Transfer Object)

```
HTTP Request (JSON)
     ↓
MascotaCreateDto (validaciones)
     ↓
Service (mapeo a Entity)
     ↓
Mascota (entity)
     ↓
EntityManager.persist()
```

**DTOs usados:**
- `MascotaCreateDto`: Crear mascotas
- `MascotaUpdateDto`: Actualizar (campos opcionales)
- `MascotaQueryDto`: Búsquedas con filtros

**Beneficios:**
- Separación de API externa vs modelo interno
- Validaciones con Bean Validation
- Actualizaciones parciales

### 4. Dependency Injection

**Spring inyecta dependencias:**

```java
@Service
public class HibernateMascotaServiceImpl {
    @PersistenceContext
    private EntityManager entityManager;  // ← Inyectado por Spring

    @Autowired
    private MascotaRepository mascotaRepository;  // ← Inyectado por Spring
}
```

**Tipos de inyección:**
- `@PersistenceContext`: Para EntityManager (JPA)
- `@Autowired`: Para beans de Spring (field injection)
- Constructor injection (recomendado en producción)

## Flujo de una Solicitud

### Ejemplo: `POST /mcp/create_mascota`

```
1. HTTP Request (JSON)
   ↓
2. McpServerController.createMascota(@RequestBody MascotaCreateDto dto)
   ↓
3. Validación del DTO (Bean Validation)
   ↓
4. service.createMascota(dto)
   ↓
5. HibernateMascotaServiceImpl.createMascota()
   - Crear objeto Mascota
   - entityManager.persist(mascota)
   ↓
6. Hibernate genera SQL INSERT
   ↓
7. Spring hace commit de transacción
   ↓
8. Hibernate ejecuta INSERT en BD
   ↓
9. Hibernate setea ID generado en el objeto (si aplica)
   ↓
10. Retornar Mascota al controller
    ↓
11. Jackson serializa Mascota a JSON
    ↓
12. HTTP Response 200 OK (JSON)
```

## Gestión de Transacciones

### Configuración de Transacciones

```java
@Service
@Transactional(readOnly = true)  // Default para toda la clase
public class HibernateMascotaServiceImpl {

    @Transactional  // Sobrescribe: readOnly=false
    public Mascota createMascota(MascotaCreateDto dto) {
        // Operación de escritura
    }

    public Mascota findMascotaById(Integer id) {
        // Usa transacción readOnly del nivel de clase
    }
}
```

## Componentes Principales

### 1. Mascota (Entidad)

**Archivo:** `src/main/java/com/dam/accesodatos/model/Mascota.java`

```java
@Entity
@Table(name = "mascotas")
public class Mascota {
    @Id
    @Column(name = "num_chip")
    private int numChip;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    // ... campos
}
```

**Responsabilidad:** Mapeo objeto-relacional

### 2. MascotaRepository

**Archivo:** `src/main/java/com/dam/accesodatos/repository/MascotaRepository.java`

```java
@Repository
public interface MascotaRepository extends JpaRepository<Mascota, Integer> {
    List<Mascota> findByTipoMascota(String tipoMascota);
    List<Mascota> findBySexo(String sexo);
}
```

**Responsabilidad:** Acceso a datos con Spring Data JPA

### 3. HibernateMascotaService (Interface)

**Archivo:** `src/main/java/com/dam/accesodatos/ra3/HibernateMascotaService.java`

**Responsabilidad:** Contrato de métodos (interface)

### 4. HibernateMascotaServiceImpl

**Archivo:** `src/main/java/com/dam/accesodatos/ra3/HibernateMascotaServiceImpl.java`

**Responsabilidad:** Implementación de lógica de negocio + ORM

## Ciclo de Vida de Entidades JPA

```
NEW (Transient)
  ↓ persist()
MANAGED (en contexto de persistencia)
  ↓ commit()
PERSISTED (en BD)
  ↓ clear() / close()
DETACHED (fuera de contexto)
  ↓ merge()
MANAGED
  ↓ remove()
REMOVED
  ↓ commit()
DELETED (eliminado de BD)
```

**Estados:**
- **NEW**: Recién creado con `new Mascota()`
- **MANAGED**: Hibernate rastreando cambios (persist, find, merge)
- **DETACHED**: Fuera del contexto (transacción cerrada)
- **REMOVED**: Marcado para eliminar (remove())

## Configuración de Spring Boot

### application.yml

```yaml
server:
  port: 8083

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/pawner_db?createDatabaseIfNotExist=true
    username: tu_usuario
    password: tu_password

  jpa:
    database-platform: org.hibernate.dialect.MySQLDialect
    hibernate:
      ddl-auto: update
    show-sql: true
```

### build.gradle

```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    runtimeOnly 'com.mysql:mysql-connector-j'
}
```

---

Esta arquitectura sigue los principios de:
- **Separación de responsabilidades** (capas)
- **Inversión de dependencias** (interfaces + DI)
- **Abstracción del acceso a datos** (Repository pattern)
- **Gestión declarativa de transacciones** (@Transactional)

Para más información, consulta [GUIA_ESTUDIANTE.md](docs/GUIA_ESTUDIANTE.md).