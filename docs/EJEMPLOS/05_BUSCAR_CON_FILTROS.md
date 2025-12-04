# Ejemplo 5: Búsqueda con Filtros (JPQL Dinámico)

Este ejemplo muestra cómo construir una consulta JPQL de forma dinámica para filtrar resultados basándose en criterios opcionales.

## 1. DTO (`MascotaQueryDto`)

El DTO contiene los posibles campos por los que se puede filtrar. Son opcionales.

```java
// MascotaQueryDto.java
public class MascotaQueryDto {
    private String tipoMascota;
    private String sexo;
    // Getters y setters...
}
```

## 2. Lógica del Servicio (`searchMascotas`)

El método construye la consulta JPQL pieza por pieza usando un `StringBuilder`.

```java
// HibernateMascotaServiceImpl.java
@Override
public List<Mascota> searchMascotas(MascotaQueryDto queryDto) {
    // 1. Iniciar la consulta base
    StringBuilder jpql = new StringBuilder("SELECT m FROM Mascota m WHERE 1=1");

    // 2. Añadir condiciones dinámicamente
    if (queryDto.getTipoMascota() != null) {
        jpql.append(" AND m.tipoMascota = :tipo");
    }
    if (queryDto.getSexo() != null) {
        jpql.append(" AND m.sexo = :sexo");
    }

    // 3. Crear la query a partir del string construido
    TypedQuery<Mascota> query = entityManager.createQuery(jpql.toString(), Mascota.class);

    // 4. Asignar los parámetros solo si los filtros fueron añadidos
    if (queryDto.getTipoMascota() != null) {
        query.setParameter("tipo", queryDto.getTipoMascota());
    }
    if (queryDto.getSexo() != null) {
        query.setParameter("sexo", queryDto.getSexo());
    }

    // 5. Devolver los resultados
    return query.getResultList();
}
```

**Conceptos Clave:**
- **`StringBuilder`**: Es la herramienta ideal para construir strings complejos de forma eficiente.
- **`WHERE 1=1`**: Un truco común para iniciar una consulta dinámica. Permite añadir todas las condiciones subsiguientes con `AND` sin tener que preocuparse de si es la primera condición o no.
- **Parámetros Nombrados (`:tipo`, `:sexo`)**: Fundamental para la seguridad y para evitar inyección SQL. Los valores se asignan de forma segura con `query.setParameter()`.
- **JPQL Orientado a Objetos**: Nota que se consulta sobre los atributos de la entidad (`m.tipoMascota`), no sobre las columnas de la base de datos.

## 3. SQL Generado

Dependiendo de los filtros proporcionados, Hibernate generará un SQL diferente.

**Caso 1: Sin filtros (`new MascotaQueryDto()`)**
```sql
-- JPQL: SELECT m FROM Mascota m WHERE 1=1
SELECT * FROM mascotas;
```

**Caso 2: Filtro por `tipoMascota`**
```sql
-- JPQL: SELECT m FROM Mascota m WHERE 1=1 AND m.tipoMascota = :tipo
SELECT * FROM mascotas WHERE tipo_mascota = 'Perro';
```

**Caso 3: Filtro por `tipoMascota` y `sexo`**
```sql
-- JPQL: SELECT m FROM Mascota m WHERE 1=1 AND m.tipoMascota = :tipo AND m.sexo = :sexo
SELECT * FROM mascotas WHERE tipo_mascota = 'Gato' AND sexo = 'Hembra';
```

## Estado del Endpoint
Actualmente, no hay un endpoint `/mcp/search_mascotas` en `McpServerController`. Implementarlo sería un excelente ejercicio de ampliación, ya que implicaría recibir el DTO y pasarlo al servicio.
