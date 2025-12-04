# Ejemplo 2: Buscar una Mascota con `find()`

Este ejemplo muestra cómo usar `entityManager.find()` para recuperar una entidad `Mascota` por su clave primaria.

## 1. Lógica del Servicio (`findMascotaById`)

El método `find()` es la forma más directa de buscar un objeto por su ID.

```java
// HibernateMascotaServiceImpl.java
@Override
public Mascota findMascotaById(Integer id) {
    // Busca una instancia de Mascota cuya clave primaria coincida con el id.
    return entityManager.find(Mascota.class, id);
}
```

**Conceptos Clave:**
- **`entityManager.find(Clase.class, id)`**: El primer argumento es el tipo de la entidad que se busca y el segundo es la clave primaria.
- **Eficiencia**: `find()` es muy eficiente. Si la entidad ya está en el contexto de persistencia (caché de primer nivel), la devuelve directamente sin ir a la base de datos.
- **Nulos**: Si no se encuentra ninguna entidad con ese ID, `find()` devuelve `null` (no lanza una excepción como `getReference()`).

## 2. Endpoint del Controlador

El controlador expone la funcionalidad para ser llamada a través de la API.

```java
// McpServerController.java
@PostMapping("/find_mascota_by_id")
public ResponseEntity<Map<String, Object>> findMascotaById(@RequestBody Map<String, Object> request) {
    Integer mascotaId = ((Number) request.get("mascotaId")).intValue();
    Mascota mascota = hibernateMascotaService.findMascotaById(mascotaId);
    // ... construir y devolver la respuesta
}
```

## 3. Petición `curl`

Para buscar la mascota con el número de chip `12345`:

```bash
curl -X POST http://localhost:8083/mcp/find_mascota_by_id \
  -H "Content-Type: application/json" \
  -d 
  {
    "mascotaId": 12345
  }
```

## 4. SQL Generado

Hibernate genera una sentencia `SELECT` limpia y optimizada.

```sql
SELECT
    m.num_chip,
    m.nombre,
    m.tipo_mascota,
    m.edad,
    m.sexo,
    m.otros_detalles
FROM
    mascotas m
WHERE
    m.num_chip = ?
```

## Comparación con JDBC (RA2)

Con JDBC, tendrías que escribir todo esto:
1.  Escribir la consulta `SELECT` a mano.
2.  Crear un `PreparedStatement`.
3.  Asignar el ID con `ps.setInt()`.
4.  Ejecutar la consulta con `ps.executeQuery()`.
5.  Obtener el `ResultSet`.
6.  Comprobar si hay un resultado con `rs.next()`.
7.  Mapear cada columna a un campo del objeto `Mascota` con `rs.getInt()`, `rs.getString()`, etc.

Con Hibernate, todo se reduce a una sola línea: `entityManager.find(Mascota.class, id)`.

```