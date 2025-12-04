# Ejemplo 1: Crear una Mascota con `persist()`

Este ejemplo muestra cómo usar `entityManager.persist()` para guardar una nueva entidad `Mascota` en la base de datos.

## 1. DTO (`MascotaCreateDto`)

Se utiliza un DTO (Data Transfer Object) para recibir los datos del cliente.

```java
// MascotaCreateDto.java
public class MascotaCreateDto {
    @NotNull
    private Integer numChip;
    @NotBlank
    private String nombre;
    @NotBlank
    private String tipoMascota;
    private int edad;
    private String sexo;
    private String otrosDetalles;
    // ... getters y setters
}
```

## 2. Lógica del Servicio (`createMascota`)

El método en `HibernateMascotaServiceImpl` convierte el DTO en una entidad `Mascota` y la persiste.

```java
// HibernateMascotaServiceImpl.java
@Override
@Transactional
public Mascota createMascota(MascotaCreateDto dto) {
    // 1. Crear la entidad a partir del DTO
    Mascota mascota = new Mascota();
    mascota.setNumChip(dto.getNumChip());
    mascota.setNombre(dto.getNombre());
    mascota.setTipoMascota(dto.getTipoMascota());
    mascota.setEdad(dto.getEdad());
    mascota.setSexo(dto.getSexo());
    mascota.setOtrosDetalles(dto.getOtrosDetalles());

    // 2. Persistir la entidad
    entityManager.persist(mascota);

    // 3. Devolver la entidad gestionada
    return mascota;
}
```

**Conceptos Clave:**
- **`@Transactional`**: Esencial para cualquier operación que modifique la base de datos. Spring gestiona el `commit` (si todo va bien) y el `rollback` (si hay errores).
- **`entityManager.persist(objeto)`**: Pone la nueva instancia de `Mascota` en el contexto de persistencia de Hibernate. En el momento del `commit`, Hibernate generará y ejecutará la sentencia `INSERT` SQL.

## 3. Endpoint del Controlador

El controlador expone la funcionalidad a través de una API REST.

```java
// McpServerController.java
@PostMapping("/create_mascota")
public ResponseEntity<Map<String, Object>> createMascota(@RequestBody MascotaCreateDto dto) {
    Mascota mascota = hibernateMascotaService.createMascota(dto);
    // ... construir y devolver la respuesta
}
```

## 4. Petición `curl`

Así se invocaría el endpoint desde la línea de comandos:

```bash
curl -X POST http://localhost:8083/mcp/create_mascota \
  -H "Content-Type: application/json" \
  -d 
  {
    "numChip": 12345,
    "nombre": "Fido",
    "tipoMascota": "Perro",
    "edad": 5,
    "sexo": "Macho",
    "otrosDetalles": "Juguetón y amigable"
  }
```

## 5. SQL Generado

Hibernate genera automáticamente una sentencia SQL similar a esta:

```sql
INSERT INTO mascotas (num_chip, nombre, tipo_mascota, edad, sexo, otros_detalles) 
VALUES (?, ?, ?, ?, ?, ?)
```

Esto demuestra el poder de un ORM: te abstraes de escribir SQL y trabajas directamente con objetos Java.
