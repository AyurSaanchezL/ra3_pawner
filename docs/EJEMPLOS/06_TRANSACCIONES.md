# Ejemplo 6: Transacciones con `@Transactional`

Este ejemplo ilustra cómo Hibernate y Spring gestionan la atomicidad de las operaciones a través de la anotación `@Transactional`.

## Atomicidad: O todo o nada

Una transacción es una secuencia de operaciones que se ejecutan como una única unidad lógica. La propiedad más importante de una transacción es la **atomicidad**: o se completan **todas** las operaciones con éxito, o **ninguna** de ellas tiene efecto.

**Escenario:** Queremos guardar una lista de varias mascotas. Si una de ellas falla (por ejemplo, por tener un `numChip` duplicado), no debería guardarse ninguna de las otras.

## 1. Lógica del Servicio (`transferData`)

El método `transferData` intenta persistir una lista de mascotas. La magia ocurre gracias a `@Transactional`.

```java
// HibernateMascotaServiceImpl.java
@Override
@Transactional
public boolean transferData(List<Mascota> mascotas) {
    // La anotación @Transactional inicia la transacción aquí.
    
    for (Mascota mascota : mascotas) {
        entityManager.persist(mascota);
        // Hibernate añade cada mascota al contexto de persistencia,
        // pero aún no ejecuta los INSERTs.
    }
    
    return true; 
    
    // Al salir del método sin errores, Spring ordena a Hibernate
    // hacer COMMIT. En este punto, se ejecutan todos los INSERTs.
}
```

**Conceptos Clave:**
- **`@Transactional`**: Esta anotación le dice a Spring que envuelva la ejecución de este método en una transacción de base de datos.
- **`BEGIN TRANSACTION`**: Spring la inicia antes de que se ejecute la primera línea del método.
- **`COMMIT`**: Si el método termina y devuelve un valor sin lanzar una excepción, Spring ordena un `COMMIT`. Todos los cambios se hacen permanentes en la base de datos.
- **`ROLLBACK`**: Si el método lanza una `RuntimeException` (o cualquier `Exception` no controlada), Spring ordena un `ROLLBACK`. Todos los cambios hechos dentro del método se deshacen, como si nunca hubieran ocurrido.

## 2. Simulación de un Error

Imaginemos que intentamos insertar dos mascotas, pero la segunda tiene un `numChip` que ya existe en la base de datos, lo que viola una `PRIMARY KEY constraint`.

```java
// En un test...
List<Mascota> mascotas = Arrays.asList(
    new Mascota(99998, "Mascota Buena", "Perro", 2, "Macho", ""),
    new Mascota(12345, "Mascota Mala", "Gato", 1, "Hembra", "") // <-- numChip 12345 ya existe
);

try {
    service.transferData(mascotas);
} catch (Exception e) {
    // La excepción (ej. PersistenceException) será capturada aquí.
}

// Verificación:
Mascota mascotaBuena = service.findMascotaById(99998);
assertNull(mascotaBuena); // <-- Correcto, la primera mascota TAMPOCO se guardó.
```

**Flujo del Error:**
1.  `@Transactional` inicia la transacción.
2.  `persist(mascotaBuena)`: Se añade al contexto, todo OK.
3.  `persist(mascotaMala)`: Se añade al contexto.
4.  El método termina. Spring intenta hacer `COMMIT`.
5.  Hibernate envía los `INSERT` a la base de datos. El segundo `INSERT` falla por clave duplicada.
6.  La base de datos devuelve un error.
7.  Hibernate envuelve este error en una `PersistenceException`.
8.  Como se ha lanzado una excepción, Spring ordena un **`ROLLBACK`** en lugar de `COMMIT`.
9.  La base de datos deshace el primer `INSERT`.
10. El resultado final es que la tabla `mascotas` queda exactamente como estaba antes de llamar al método. Se ha garantizado la atomicidad.

## Comparación con JDBC (RA2)
Con JDBC, tendrías que manejar la transacción manualmente:
```java
Connection conn = ...;
conn.setAutoCommit(false); // <-- Desactivar autocommit
try {
    // Bucle con PreparedStatement...
    // ...
    conn.commit(); // <-- Commit manual
} catch (SQLException e) {
    conn.rollback(); // <-- Rollback manual
} finally {
    conn.setAutoCommit(true); // Restaurar estado
}
```
Con Spring y Hibernate, la anotación `@Transactional` se encarga de toda esta lógica compleja, haciendo el código mucho más limpio y seguro.
