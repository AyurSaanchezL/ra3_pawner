# Ejemplo 4: Eliminar una Mascota con `remove()`

Este ejemplo muestra cómo usar `entityManager.remove()` para eliminar una entidad `Mascota` de la base de datos.

> **Nota:** Aunque la lógica en el servicio está definida como un **TODO** para el estudiante, el endpoint en `McpServerController` para esta funcionalidad **no está implementado**. Este ejemplo se centra en la lógica de Hibernate que se debe aplicar.

## 1. Lógica del Servicio (`deleteMascota`)

El patrón para eliminar es:
1.  Recuperar la entidad que se va a eliminar para que esté en estado "gestionado".
2.  Pasar el objeto gestionado a `entityManager.remove()`.

```java
// HibernateMascotaServiceImpl.java
@Override
@Transactional
public boolean deleteMascota(Integer id) {
    // 1. Recuperar la entidad para ponerla en estado gestionado
    Mascota mascota = findMascotaById(id);

    // 2. Si no existe, no se puede eliminar
    if (mascota == null) {
        return false;
    }

    // 3. Pasa la entidad gestionada a remove()
    entityManager.remove(mascota);

    return true;
}
```

**Conceptos Clave:**
- **`@Transactional`**: Absolutamente necesario, ya que `remove()` es una operación de escritura.
- **Requisito de Entidad Gestionada**: `entityManager.remove()` solo funciona con entidades que están en el contexto de persistencia (es decir, "gestionadas"). Por eso es crucial llamar a `find()` o `getReference()` primero. No se puede hacer `remove(new Mascota())`.
- **Operación en Cascada (`CascadeType`)**: Si la entidad `Mascota` tuviera relaciones con otras entidades (por ejemplo, `HistorialMedico`) y estuvieran configuradas con `CascadeType.REMOVE`, al eliminar la mascota también se eliminarían sus registros relacionados.

## 2. SQL Generado

Hibernate generará la sentencia `DELETE` correspondiente.

```sql
DELETE 
FROM
    mascotas 
WHERE
    num_chip=?
```

## Comparación con `executeUpdate` de JPQL

También es posible eliminar con una consulta JPQL, pero existen diferencias importantes:

**`entityManager.remove(objeto)`:**
-   **Ventajas**: Dispara los eventos del ciclo de vida de JPA (como `@PreRemove`). Funciona correctamente con la caché de primer nivel de Hibernate. Es la forma "orientada a objetos" de eliminar.
-   **Desventajas**: Requiere una consulta `SELECT` previa para obtener el objeto.

**`Query.executeUpdate()`:**
-   **Ventajas**: Puede ser más eficiente para eliminaciones masivas, ya que se ejecuta directamente en la base de datos sin traer los objetos a memoria.
-   **Desventajas**: **No** dispara los eventos del ciclo de vida. Puede dejar la caché de persistencia desactualizada.

**Ejemplo con JPQL (alternativo, no recomendado para un solo objeto):**
```java
@Transactional
public int deleteMascotaWithJpql(Integer id) {
    Query query = entityManager.createQuery("DELETE FROM Mascota m WHERE m.numChip = :id");
    query.setParameter("id", id);
    return query.executeUpdate(); // Devuelve el número de filas afectadas
}
```

Para eliminar un solo objeto, el enfoque con `find()` y `remove()` es generalmente el preferido.

## Estado del Endpoint
Actualmente, no hay un endpoint `/mcp/delete_mascota` en `McpServerController`. Implementarlo sería un excelente ejercicio de ampliación para el estudiante.
