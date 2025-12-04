# Guía de Herramientas MCP (Mascotas)

Guía práctica para usar las herramientas MCP (Model Context Protocol) del proyecto para la gestión de mascotas.

## ¿Qué es MCP?

**Model Context Protocol (MCP)** es un protocolo que permite a los LLMs (Large Language Models) interactuar con aplicaciones mediante "herramientas" (tools). En este proyecto, cada método del servicio Hibernate está expuesto como una herramienta MCP que puede ser invocada vía REST.

## Arquitectura MCP

```
LLM/Cliente HTTP
     ↓
REST Endpoint (ej. /mcp/create_mascota)
     ↓
McpServerController
     ↓
HibernateMascotaService (método anotado con @Tool)
     ↓
Hibernate/JPA → Base de Datos (MySQL)
```

## Herramientas Disponibles

### 1. test_entity_manager

**Propósito:** Verificar que EntityManager de Hibernate está activo y conectado.
**Cuándo usarla:** Al iniciar, para validar la configuración del ORM y la conexión a la BD.
**Endpoint:** `POST /mcp/test_entity_manager`
**Request:** Sin body

**Ejemplo curl:**
```bash
curl -X POST http://localhost:8083/mcp/test_entity_manager
```
**Response:**
```json
{
    "tool": "test_entity_manager",
    "result": "✓ EntityManager activo | Base de datos: pawner_db | Test: 1",
    "status": "success"
}
```

---


### 2. create_mascota

**Propósito:** Crear una nueva mascota en la base de datos.
**Cuándo usarla:** Para insertar nuevos registros.
**Endpoint:** `POST /mcp/create_mascota`

**Request:**
```json
{
  "numChip": 12345,
  "nombre": "Fido",
  "tipoMascota": "Perro",
  "edad": 5,
  "sexo": "Macho"
}
```

**Ejemplo curl:**
```bash
curl -X POST http://localhost:8083/mcp/create_mascota \
  -H "Content-Type: application/json" \
  -d '{"numChip": 12345, "nombre": "Fido", "tipoMascota": "Perro", "edad": 5, "sexo": "Macho"}'
```

**Response:** El objeto de la mascota creada.


### 3. find_mascota_by_id

**Propósito:** Buscar una mascota por su número de chip (ID).
**Cuándo usarla:** Para `SELECT` por clave primaria.
**Endpoint:** `POST /mcp/find_mascota_by_id`

**Request:**
```json
{
  "mascotaId": 12345
}
```

**Ejemplo curl:**
```bash
curl -X POST http://localhost:8083/mcp/find_mascota_by_id \
  -H "Content-Type: application/json" \
  -d '{"mascotaId": 12345}'
```
**Response (encontrado):** El objeto de la mascota encontrada.
**Response (no encontrado):** `null`.


### 4. update_mascota (TODO)

**Propósito:** Actualizar campos de una mascota existente.
**Estado:** ⚠️ TODO - El endpoint no está implementado en el controlador. El servicio sí tiene un método `updateMascota` para implementar.


### 5. delete_mascota (TODO)

**Propósito:** Eliminar una mascota por su ID.
**Estado:** ⚠️ TODO - El endpoint no está implementado en el controlador. El servicio sí tiene un método `deleteMascota` para implementar.


### 6. find_all_mascotas

**Propósito:** Obtener una lista de todas las mascotas.
**Cuándo usarla:** Para un listado completo (`SELECT *`).
**Endpoint:** `POST /mcp/find_all_mascotas`
**Request:** Sin body

**Ejemplo curl:**
```bash
curl -X POST http://localhost:8083/mcp/find_all_mascotas
```
**Response:** Un array con todas las mascotas.


### 7. find_mascotas_by_tipo

**Propósito:** Buscar mascotas de un tipo específico.
**Cuándo usarla:** Para consultas con filtro (`WHERE`).
**Endpoint:** `POST /mcp/find_mascotas_by_tipo`

**Request:**
```json
{
  "tipo": "Perro"
}
```

**Ejemplo curl:**
```bash
curl -X POST http://localhost:8083/mcp/find_mascotas_by_tipo \
  -H "Content-Type: application/json" \
  -d '{"tipo": "Perro"}'
```
**Response:** Un array de mascotas que coinciden con el tipo.
**JPQL Usado:** `SELECT m FROM Mascota m WHERE m.tipoMascota = :tipo`


### 8. search_mascotas (TODO)

**Propósito:** Búsqueda con múltiples filtros opcionales.
**Estado:** ⚠️ TODO - El endpoint no está implementado. El servicio tiene un método `searchMascotas` para implementar.


### 9. transfer_data (TODO)

**Propósito:** Insertar múltiples mascotas en una transacción atómica.
**Estado:** ⚠️ TODO - El endpoint no está implementado. El servicio tiene un método `transferData` para implementar.


### 10. execute_count_by_tipo (TODO)

**Propósito:** Contar mascotas de un tipo específico.
**Estado:** ⚠️ TODO - El endpoint no está implementado. El servicio tiene un método `executeCountByTipo` para implementar.


## Cómo Funciona el Registro de Herramientas

### McpToolRegistry
El componente `McpToolRegistry` escanea la aplicación al arrancar en busca de métodos anotados con `@Tool` y los registra para que puedan ser listados por el endpoint `/tools`.

### Anotación @Tool
Permite definir un nombre y descripción para un método de servicio, convirtiéndolo en una "herramienta" MCP.

**Uso:**
```java
// En HibernateMascotaService.java
@Tool(name = "create_mascota", description = "Persiste una nueva mascota...")
Mascota createMascota(MascotaCreateDto dto);
```

### McpServerController
Expone los endpoints REST que invocan a los métodos del servicio correspondientes.

```java
@RestController
@RequestMapping("/mcp")
public class McpServerController {
    @PostMapping("/create_mascota")
    public ResponseEntity<Map<String, Object>> createMascota(@RequestBody MascotaCreateDto dto) {
        Mascota mascota = hibernateMascotaService.createMascota(dto);
        // ... empaquetar y devolver respuesta
    }
    // ... más endpoints
}
```

---


## Testing de Herramientas MCP

### Desde Terminal (curl)

```bash
# Health check
curl http://localhost:8083/mcp/health

# Listar herramientas
curl http://localhost:8083/mcp/tools

# Probar la creación de una mascota
curl -X POST http://localhost:8083/mcp/create_mascota \
  -H "Content-Type: application/json" \
  -d '{"numChip": 999, "nombre": "Tester", "tipoMascota": "Gato"}'
```

### Desde Postman o Insomnia
Puedes usar cualquier cliente REST para probar los endpoints. Configura la URL base a `http://localhost:8083/mcp` y crea peticiones `POST` con los cuerpos (JSON) correspondientes.

---


Para más detalles técnicos de cada endpoint, consulta [API_REFERENCIA.md](API_REFERENCIA.md).