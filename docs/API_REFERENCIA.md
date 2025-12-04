# API Referencia - mcp-hibernate (Mascotas)

Referencia técnica completa de los endpoints MCP y métodos del servicio para la gestión de mascotas.

## Endpoints MCP (Base URL: http://localhost:8083/mcp)

### GET /health
Health check del servidor MCP.

**Response:**
```json
{"status":"UP","service":"MCP Server RA3 Hibernate/JPA - Mascotas"}
```

### GET /tools
Lista todas las herramientas MCP disponibles.

**Response (Ejemplo):**
```json
{
  "tools": [
    {"name": "test_entity_manager", "description": "Prueba la conexión con EntityManager..."},
    {"name": "create_mascota", "description": "Persiste una nueva mascota..."},
    {"name": "find_mascota_by_id", "description": "Busca una mascota por su número de chip..."}
  ],
  "count": 7
}
```

### POST /test_entity_manager
Verifica la conexión con `EntityManager`.

**Request:** No requiere body

**Response:**
```json
{
    "tool": "test_entity_manager",
    "result": "✓ EntityManager activo | Base de datos: pawner_db | Test: 1",
    "status": "success"
}
```

### POST /create_mascota
Crea una nueva mascota.

**Request:**
```json
{
  "numChip": 12345,
  "nombre": "Fido",
  "tipoMascota": "Perro",
  "edad": 5,
  "sexo": "Macho",
  "otrosDetalles": "Amigable con los niños"
}
```

**Response:**
```json
{
    "tool": "create_mascota",
    "result": {
        "numChip": 12345,
        "nombre": "Fido",
        "tipoMascota": "Perro",
        "edad": 5,
        "sexo": "Macho",
        "otrosDetalles": "Amigable con los niños"
    },
    "status": "success"
}
```

### POST /find_mascota_by_id
Busca una mascota por su número de chip.

**Request:**
```json
{
  "mascotaId": 12345
}
```

**Response (encontrado):**
```json
{
  "tool": "find_mascota_by_id",
  "result": {
    "numChip": 12345,
    "nombre": "Fido",
    ...
  },
  "status": "success"
}
```

### POST /update_mascota
**Endpoint no implementado todavía.**

### POST /delete_mascota
**Endpoint no implementado todavía.**

### POST /find_all_mascotas
Obtiene una lista de todas las mascotas.

**Request:** No requiere body

**Response:**
```json
{
    "tool": "find_all_mascotas",
    "result": [
        {"numChip": 12345, "nombre": "Fido", ...},
        {"numChip": 67890, "nombre": "Misty", ...}
    ],
    "count": 2,
    "status": "success"
}
```

### POST /find_mascotas_by_tipo
Busca mascotas por tipo (ej. "Perro", "Gato").

**Request:**
```json
{
  "tipo": "Perro"
}
```

**Response:** Array de mascotas del tipo especificado.

## Métodos del Servicio

### HibernateMascotaService
Todos los métodos están documentados en `src/main/java/com/dam/accesodatos/ra3/HibernateMascotaService.java`.
Ver [GUIA_ESTUDIANTE.md](GUIA_ESTUDIANTE.md) para detalles de implementación.

## Modelos de Datos

### Mascota (Entidad)
```java
@Entity
@Table(name = "mascotas")
public class Mascota {
    @Id
    @Column(name = "num_chip")
    private int numChip;

    @Column(nullable = false, length = 50)
    private String nombre;
    
    @Column(name = "tipo_mascota", nullable = false, length = 50)
    private String tipoMascota;
    
    @Column
    private int edad;
    
    @Column(length = 50)
    private String sexo;
    
    @Column(name = "otros_detalles", length = 255)
    private String otrosDetalles;
}
```

### MascotaCreateDto
```java
{
  "numChip": "integer (requerido)",
  "nombre": "string (requerido, 2-50 chars)",
  "tipoMascota": "string (requerido)",
  "edad": "integer",
  "sexo": "string",
  "otrosDetalles": "string"
}
```

### MascotaUpdateDto
```java
{
  "nombre": "string (opcional, 2-50 chars)",
  "tipoMascota": "string (opcional)",
  "edad": "integer (opcional)",
  "sexo": "string (opcional)",
  "otrosDetalles": "string (opcional)"
}
```

### MascotaQueryDto
```java
{
  "tipoMascota": "string (opcional)",
  "sexo": "string (opcional)",
  "limit": "integer (opcional)",
  "offset": "integer (opcional)"
}
```

## Conexión a Base de Datos MySQL
Para examinar la base de datos, se recomienda utilizar un cliente de MySQL como **DBeaver**, **DataGrip** o **MySQL Workbench**.

**Datos de conexión (por defecto):**
- **Host**: `localhost`
- **Puerto**: `3306`
- **Base de Datos**: `pawner_db`
- **Usuario**: (el que configuraste en `application.yml`)
- **Contraseña**: (la que configuraste en `application.yml`)

**Query de ejemplo:**
```sql
-- Todas las mascotas
SELECT * FROM mascotas;

-- Por tipo
SELECT * FROM mascotas WHERE tipo_mascota = 'Perro';
```

## Error Handling

**Formato de Error:**
```json
{
    "error": "Error creando mascota: ...detalle del error...",
    "tool": "create_mascota",
    "status": "error"
}
```

Para más información, consulta [GUIA_HERRAMIENTAS_MCP.md](GUIA_HERRAMIENTAS_MCP.md).