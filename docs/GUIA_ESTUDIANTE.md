# Guía del Estudiante - mcp-hibernate

¡Bienvenido al proyecto **mcp-hibernate**! Esta guía te acompañará paso a paso en tu aprendizaje de Hibernate/JPA (RA3) para el módulo de Acceso a Datos.

## Tabla de Contenidos

1. [Bienvenida y Objetivos](#1-bienvenida-y-objetivos)
2. [Conceptos Fundamentales](#2-conceptos-fundamentales)
3. [Navegando el Código](#3-navegando-el-código)
4. [Los Métodos Implementados (ESTUDIAR)](#4-los-métodos-implementados-estudiar)
5. [Los Métodos TODO (IMPLEMENTAR)](#5-los-métodos-todo-implementar)
6. [Ejecutar y Entender los Tests](#6-ejecutar-y-entender-los-tests)
7. [Flujo de Trabajo Recomendado](#7-flujo-de-trabajo-recomendado)
8. [Recursos Adicionales](#8-recursos-adicionales)

---

## 1. Bienvenida y Objetivos

### ¿Qué vas a aprender?

Este proyecto te enseñará a:

✅ **Entender ORM** (Object-Relational Mapping) y por qué simplifica el acceso a datos.
✅ **Usar Hibernate/JPA** para operaciones CRUD sin escribir SQL manualmente.
✅ **Escribir consultas JPQL** (Java Persistence Query Language).
✅ **Gestionar transacciones** con la anotación `@Transactional`.
✅ **Mapear entidades** a tablas con anotaciones JPA.
✅ **Comprender la diferencia** entre JDBC (RA2) y Hibernate (RA3).

### Resultados de Aprendizaje (RA3)

Este proyecto cubre completamente el **RA3**:

> **RA3:** Gestiona la persistencia de los datos identificando herramientas de mapeo objeto relacional (ORM) y desarrollando aplicaciones que las utilizan.

Consulta [CRITERIOS_RA3_DETALLADO.md](CRITERIOS_RA3_DETALLADO.md) para ver cómo cada método del código cubre un criterio específico.

---

## 2. Conceptos Fundamentales

### ¿Qué es un ORM?

**Antes (RA2 - JDBC):** Escribías SQL manualmente y mapeabas `ResultSet` a objetos, campo por campo.

```java
// RA2: JDBC manual
String sql = "SELECT * FROM mascotas WHERE num_chip = ?";
PreparedStatement ps = conn.prepareStatement(sql);
ps.setInt(1, numChip);
ResultSet rs = ps.executeQuery();
if (rs.next()) {
    Mascota mascota = new Mascota();
    mascota.setNumChip(rs.getInt("num_chip"));
    mascota.setNombre(rs.getString("nombre"));
    // ... más campos ...
}
```

**Ahora (RA3 - Hibernate):** Hibernate hace todo esto automáticamente.

```java
// RA3: Hibernate automático
Mascota mascota = entityManager.find(Mascota.class, numChip);
// ¡Una línea! Hibernate genera el SQL y mapea los resultados por ti.
```

### Diferencias Clave RA2 vs RA3

| Concepto | RA2 (JDBC) | RA3 (Hibernate/JPA) |
|----------|------------|---------------------|
| **Lenguaje** | SQL (tablas y columnas) | JPQL (entidades y atributos) |
| **Mapeo** | Manual con `ResultSet` | Automático con `@Entity` |
| **INSERT** | `PreparedStatement` + `setString()`... | `persist(objeto)` |
| **SELECT** | `executeQuery()` + bucle `while(rs.next())` | `find(id)` o una consulta JPQL |
| **UPDATE** | `UPDATE mascotas SET ...` | `merge(objeto)` + *dirty checking* |
| **DELETE** | `DELETE FROM mascotas ...` | `remove(objeto)` |
| **Transacciones**| `commit()`/`rollback()` manual | `@Transactional` automático |

**Para profundizar:**
Lee [Explicacion_Clase_Hibernate.md](../Explicacion_Clase_Hibernate.md) para una explicación didáctica completa de ORM y Hibernate.

---

## 3. Navegando el Código

### Estructura de Paquetes

```
src/main/java/com/dam/accesodatos/
├── McpAccesoDatosRa3Application.java  → Clase principal
├── model/
│   ├── Mascota.java                     → 🎯 Entidad JPA (mapeo @Entity)
│   ├── MascotaCreateDto.java            → DTO para crear mascotas
│   ├── MascotaUpdateDto.java            → DTO para actualizar
│   └── MascotaQueryDto.java             → DTO para búsquedas
├── ra3/
│   ├── HibernateMascotaService.java     → 📋 Interface (contratos de métodos)
│   └── HibernateMascotaServiceImpl.java → 🎯 IMPLEMENTACIÓN (tu código va aquí)
├── repository/
│   └── MascotaRepository.java           → 🎯 Spring Data JPA Repository
└── mcp/
    ├── McpServerController.java       → Endpoints REST
    └── McpToolRegistry.java           → Registro de herramientas MCP
```

### Archivos Clave que Debes Estudiar

#### 1. `Mascota.java` - La Entidad
Este archivo define el mapeo entre la clase Java `Mascota` y la tabla `mascotas` en la base de datos.

```java
@Entity                          // ← Marca como entidad JPA
@Table(name = "mascotas")        // ← Mapea a la tabla "mascotas"
public class Mascota {
    @Id                          // ← Clave primaria
    @Column(name = "num_chip")   // ← Mapeo de columna
    private int numChip;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "tipo_mascota", nullable = false)
    private String tipoMascota;

    // ... más campos
}
```
**Archivo:** `src/main/java/com/dam/accesodatos/model/Mascota.java`

#### 2. `HibernateMascotaServiceImpl.java` - La Implementación
Este es el archivo **MÁS IMPORTANTE**. Aquí encontrarás los ejemplos que debes estudiar y los métodos `TODO` que debes implementar.
**Archivo:** `src/main/java/com/dam/accesodatos/ra3/HibernateMascotaServiceImpl.java`

#### 3. `MascotaRepository.java` - Spring Data JPA
Una interfaz que extiende `JpaRepository`. Spring Data JPA genera la implementación de los métodos automáticamente a partir de su nombre.

```java
@Repository
public interface MascotaRepository extends JpaRepository<Mascota, Integer> {
    // Spring Data JPA genera la implementación automáticamente
    List<Mascota> findByTipoMascota(String tipoMascota); // ← Query derivado del nombre
    List<Mascota> findBySexo(String sexo);
}
```
**Archivo:** `src/main/java/com/dam/accesodatos/repository/MascotaRepository.java`

---

## 4. Los Métodos Implementados (ESTUDIAR)

Estos métodos están listos para que los estudies y entiendas cómo funcionan.

### 4.1. Método 1: `testEntityManager()` - Verificar Conexión
Verifica que `EntityManager` está activo y conectado a la base de datos.

**Conceptos Clave:**
- `EntityManager`: Interfaz principal de JPA.
- `createNativeQuery()`: Ejecuta SQL nativo (útil para pruebas de conexión).

### 4.2. Método 2: `createMascota()` - INSERT con `persist()`
Crea una nueva mascota en la base de datos.

```java
@Override
@Transactional  // ← OBLIGATORIO para modificar la BD
public Mascota createMascota(MascotaCreateDto dto) {
    // 1. Crear entidad desde DTO
    Mascota mascota = new Mascota();
    mascota.setNumChip(dto.getNumChip());
    mascota.setNombre(dto.getNombre());
    mascota.setTipoMascota(dto.getTipoMascota());
    // ... otros campos ...

    // 2. Persistir (Hibernate genera el INSERT automáticamente)
    entityManager.persist(mascota);

    // 3. Retornar la mascota persistida
    return mascota;
}
```
**Conceptos Clave:**
- `@Transactional`: Spring maneja `commit/rollback` automáticamente.
- `persist(objeto)`: Pone el objeto en el contexto de persistencia para ser guardado.

### 4.3. Método 3: `findMascotaById()` - SELECT por ID
Busca una mascota por su clave primaria (`numChip`).

```java
@Override
public Mascota findMascotaById(Integer id) {
    // find() es la forma más simple de buscar por clave primaria
    return entityManager.find(Mascota.class, id);
}
```
**Conceptos Clave:**
- `find(Clase.class, id)`: Busca por clave primaria.
- Retorna `null` si no se encuentra (no lanza excepción).

### 4.4. Método 4: `findAll()` - SELECT todos con Repository
Obtiene todas las mascotas usando el repositorio de Spring Data JPA.

```java
@Override
public List<Mascota> findAll() {
    // Spring Data JPA nos da este método ¡gratis!
    return mascotaRepository.findAll();
}
```
**Conceptos Clave:**
- `JpaRepository`: Provee métodos CRUD (`save`, `findById`, `findAll`, `deleteById`, etc.) sin necesidad de implementarlos.

### 4.5. Método 5: `findMascotasByTipo()` - JPQL Básico
Busca mascotas de un tipo específico usando una consulta JPQL.

```java
@Override
public List<Mascota> findMascotasByTipo(String tipo) {
    // JPQL: Query orientado a objetos (entidades y atributos)
    String jpql = "SELECT m FROM Mascota m WHERE m.tipoMascota = :tipo";

    TypedQuery<Mascota> query = entityManager.createQuery(jpql, Mascota.class);
    query.setParameter("tipo", tipo);

    return query.getResultList();
}
```
**Conceptos Clave:**
- **JPQL**: Java Persistence Query Language, similar a SQL pero orientado a objetos.
- Se consulta sobre la entidad (`Mascota`) y sus atributos (`tipoMascota`), no sobre la tabla (`mascotas`) y sus columnas.
- `:tipo`: Parámetro nombrado para evitar inyección SQL.

---

## 5. Los Métodos TODO (IMPLEMENTAR)

Ahora es tu turno. Implementa los métodos marcados con `TODO` en `HibernateMascotaServiceImpl.java`.

### 5.1. TODO 1: `updateMascota()` - UPDATE con `merge()`
**Objetivo:** Actualizar una mascota existente.

**Pistas:**
1. Busca la mascota con `findMascotaById(id)`.
2. Si no existe, lanza una excepción.
3. Usa el método `dto.applyTo(existingMascota)` que ya hemos creado para aplicar los cambios.
4. Llama a `entityManager.merge(existingMascota)` para guardar los cambios.
5. No olvides la anotación `@Transactional`.

### 5.2. TODO 2: `deleteMascota()` - Eliminar con `remove()`
**Objetivo:** Eliminar una mascota por su ID.

**Pistas:**
1. Busca la mascota con `findMascotaById(id)`.
2. Si existe, elimínala con `entityManager.remove(mascota)`.
3. Devuelve `true` si se eliminó, `false` si no se encontró.
4. No olvides la anotación `@Transactional`.

### 5.3. TODO 3: `searchMascotas()` - JPQL Dinámico
**Objetivo:** Buscar mascotas con filtros opcionales (por `tipoMascota` y/o `sexo`).

**Pistas:**
1. Usa un `StringBuilder` para construir la consulta JPQL base: `"SELECT m FROM Mascota m WHERE 1=1"`.
2. Si `queryDto.getTipoMascota()` no es nulo, añade `" AND m.tipoMascota = :tipo"` al `StringBuilder`.
3. Si `queryDto.getSexo()` no es nulo, añade `" AND m.sexo = :sexo"`.
4. Crea la `TypedQuery` y asigna los parámetros solo si los filtros correspondientes no son nulos.

### 5.4. TODO 4: `executeCountByTipo()` - COUNT en JPQL
**Objetivo:** Contar cuántas mascotas hay de un tipo específico.

**Pistas:**
1. La consulta JPQL es `"SELECT COUNT(m) FROM Mascota m WHERE m.tipoMascota = :tipo"`.
2. El resultado de `COUNT` es un `Long`. Crea una `TypedQuery<Long>`.
3. Usa `getSingleResult()` para obtener el resultado, no `getResultList()`.

---

## 6. Ejecutar y Entender los Tests
Los tests son tu mejor amigo para validar que tu código funciona.

### Ejecutar Todos los Tests
```bash
./gradlew test
```

### Estructura de un Test de Integración
```java
@SpringBootTest
@ActiveProfiles("test") // Usa la base de datos en memoria H2
class HibernateMascotaServiceIntegrationTest {

    @Autowired
    private HibernateMascotaService service;

    @Test
    @Transactional // Cada test tiene su propia transacción que se revierte al final
    void createMascota_ValidDto_Success() {
        // ARRANGE: Preparar datos
        MascotaCreateDto dto = new MascotaCreateDto(1, "Buddy", "Perro", 3, "Macho", "");

        // ACT: Ejecutar método
        Mascota created = service.createMascota(dto);

        // ASSERT: Verificar resultado
        assertNotNull(created);
        assertEquals("Buddy", created.getNombre());
    }
}
```

---

## 7. Flujo de Trabajo Recomendado

1.  **Estudia un ejemplo:** Elige un método ya implementado que se parezca al `TODO` que quieres resolver.
2.  **Lee las pistas:** En `HibernateMascotaServiceImpl.java`, lee los comentarios del `TODO`.
3.  **Implementa:** Escribe tu solución.
4.  **Ejecuta el test:** Valida tu código con `./gradlew test`.
5.  **Observa el SQL:** Mira la consola para entender qué hace Hibernate por debajo.
6.  **Verifica en la base de datos:** Conecta tu cliente MySQL y comprueba los datos.

---

## 8. Recursos Adicionales
- **[API_REFERENCIA.md](API_REFERENCIA.md)**: Referencia técnica de endpoints.
- **[ARQUITECTURA.md](ARQUITECTURA.md)**: Diseño del sistema.
- **[PREGUNTAS_FRECUENTES.md](PREGUNTAS_FRECUENTES.md)**: Solución a problemas comunes.
- **[Explicacion_Clase_Hibernate.md](../Explicacion_Clase_Hibernate.md)**: Guía teórica de ORM.

**¡Felicidades!** Estás listo para dominar Hibernate/JPA.