# Ejemplo 3: Actualizar una Mascota con `merge()`

Este ejemplo muestra cómo usar `entityManager.merge()` para actualizar una entidad `Mascota` existente.

> **Nota:** Aunque la lógica en el servicio está definida como un **TODO** para el estudiante, el endpoint en `McpServerController` para esta funcionalidad **no está implementado**. Este ejemplo se centra en la lógica de Hibernate que se debe aplicar.

## 1. DTO (`MascotaUpdateDto`)

Se utiliza un DTO específico para la actualización, donde todos los campos son opcionales.

```java
// MascotaUpdateDto.java
public class MascotaUpdateDto {
    private String nombre;
    private String tipoMascota;
    private Integer edad;
    private String sexo;
    private String otrosDetalles;
    
    // Método para aplicar los cambios a la entidad
    public void applyTo(Mascota mascota) {
        if (this.nombre != null) mascota.setNombre(this.nombre);
        if (this.tipoMascota != null) mascota.setTipoMascota(this.tipoMascota);
        // ... etc. para otros campos
    }
}
```

## 2. Lógica del Servicio (`updateMascota`)

El patrón para actualizar es:
1.  Recuperar la entidad de la base de datos.
2.  Modificar los campos del objeto recuperado.
3.  Usar `merge()` para guardar los cambios.

```java
// HibernateMascotaServiceImpl.java
@Override
@Transactional
public Mascota updateMascota(Integer id, MascotaUpdateDto dto) {
    // 1. Recuperar la entidad gestionada
    Mascota existing = findMascotaById(id);
    if (existing == null) {
        throw new RuntimeException("No se encontró mascota con número de chip " + id);
    }

    // 2. Aplicar los cambios del DTO a la entidad
    dto.applyTo(existing);

    // 3. Guardar los cambios en la base de datos
    return entityManager.merge(existing);
}
```

**Conceptos Clave:**
- **`@Transactional`**: Requerido para la operación de escritura (`UPDATE`).
- **Entidad Gestionada (`managed`)**: El objeto `existing` está "gestionado" por Hibernate porque fue recuperado de la base de datos con `find()`.
- **`Dirty Checking` (Comprobación de "suciedad")**: Cuando se modifica un objeto gestionado dentro de una transacción, Hibernate detecta automáticamente los cambios. Al hacer `commit`, generará una sentencia `UPDATE` solo para los campos que han cambiado.
- **`entityManager.merge(objeto)`**: Sincroniza el estado de un objeto (gestionado o no) con el contexto de persistencia. Si el objeto ya estaba gestionado, `merge` no es estrictamente necesario gracias al *dirty checking*, pero es una buena práctica para asegurar que los cambios se propagan.

## 3. SQL Generado

Si, por ejemplo, solo se actualiza el nombre y la edad de una mascota, Hibernate es lo suficientemente inteligente como para generar un `UPDATE` parcial.

```sql
UPDATE
    mascotas 
SET
    nombre=?,
    edad=? 
WHERE
    num_chip=?
```

Esto es mucho más eficiente que actualizar todos los campos cada vez.

## Estado del Endpoint
Actualmente, no hay un endpoint `/mcp/update_mascota` en `McpServerController`. Implementarlo sería un excelente ejercicio de ampliación para el estudiante.
