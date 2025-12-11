# Tests de HibernateMascotaServiceImpl

## 📋 Descripción

Este directorio contiene dos tipos de tests para validar la implementación de `HibernateMascotaServiceImpl.java`:

1. **Tests Unitarios** (`HibernateMascotaServiceImplTest.java`) - Usando Mockito
2. **Tests de Integración** (`HibernateMascotaServiceIntegrationTest.java`) - Usando Spring Boot con BD H2

Los tests validan el correcto funcionamiento de las operaciones CRUD y consultas JPA/Hibernate.

---

## 🎯 Cobertura de Tests

### Tests Unitarios (17 tests con Mockito)

| Método | Tests | Descripción |
|--------|-------|-------------|
| **testEntityManager()** | 2 | Verifica EntityManager activo/cerrado |
| **createMascota()** | 1 | INSERT con persist() |
| **findMascotaByNumChip()** | 2 | SELECT por ID con find() |
| **updateMascota()** | 2 | UPDATE con merge() |
| **deleteMascota()** | 2 | DELETE con remove() |
| **findAll()** | 1 | SELECT all con Repository |
| **findMascotasByTipo()** | 2 | JPQL básico con filtro |
| **searchMascotas()** | 3 | JPQL dinámico |
| **executeCountByTipo()** | 2 | JPQL COUNT |

### Tests de Integración (14 tests con Spring Boot)

| Categoría | Tests | Descripción |
|-----------|-------|-------------|
| **Conexión** | 1 | Verifica conexión real a BD H2 |
| **Flujo CRUD** | 1 | CREATE → READ → UPDATE → DELETE completo |
| **findAll()** | 1 | Recuperar todas las mascotas |
| **findMascotasByTipo()** | 2 | Filtrar por tipo |
| **searchMascotas()** | 3 | Búsquedas dinámicas |
| **executeCountByTipo()** | 2 | Contar por tipo |
| **transferData()** | 2 | Transacciones (commit y rollback) |
| **Casos límite** | 3 | IDs inexistentes |

**TOTAL: 31 tests** ✅

---

## 📝 Datos de Prueba

### Tests Unitarios
Los tests unitarios usan **mocks** (datos simulados):

```java
// Mascota de prueba - chip 1001 = Max (Perro)
testMascota:
  - numChip: 1001
  - nombre: "Max"
  - tipo: "Perro"
  - edad: 5
  - sexo: "Macho"
  - detalles: "Golden Retriever, castrado"

// DTO para crear - chip 1002 = Luna (Gato)
createDto:
  - numChip: 1002
  - nombre: "Luna"
  - tipo: "Gato"
  - edad: 3
  - sexo: "Hembra"
  - detalles: "Siamés, todas las vacunas"
```

### Tests de Integración
Los tests de integración usan **datos reales** en BD H2:

```java
// Se crean dinámicamente según el test
Ejemplos:
- 1001: Max (Perro, Macho)
- 1002: Luna (Gato, Hembra)
- 1003: Copito (Conejo, Macho)
- 1004: Rex (Perro, Macho)
- 1005: Bella (Perro, Hembra)
```

---

## 💬 Mensajes de Salida

Todos los tests imprimen mensajes informativos cuando pasan:

### Ejemplos de Tests Unitarios:
```
✅ Test PASADO: EntityManager activo | Base de datos: H2 | Test: 1
✅ Test PASADO: Mascota creada con éxito - Luna (ID: 1002)
✅ Test PASADO: Mascota encontrada - Max (ID: 1001)
✅ Test PASADO: Mascota actualizada - Nuevo nombre: Maximus, Nueva edad: 6
✅ Test PASADO: Mascota eliminada con éxito (ID: 1001)
✅ Test PASADO: Encontradas 1 mascota(s)
✅ Test PASADO: Búsqueda dinámica por tipo 'Perro' - 1 resultado(s)
✅ Test PASADO: COUNT de tipo 'Perro' = 5
```

### Ejemplos de Tests de Integración:
```
🔄 Iniciando flujo CRUD completo...
   ✓ CREATE: Mascota creada - Max (ID: 1001)
   ✓ READ: Mascota encontrada - Max
   ✓ UPDATE: Mascota actualizada - Nuevo nombre: Maximus, Nueva edad: 6
   ✓ VERIFY: Cambios persistidos correctamente
   ✓ DELETE: Mascota eliminada (ID: 1001)
   ✓ VERIFY DELETE: Confirmado que la mascota ya no existe
✅ Test PASADO: Flujo CRUD completo exitoso

✅ Test PASADO: Encontradas 3 mascotas en total
✅ Test PASADO: Encontrados 2 perros (Max, Rex) de 5 mascotas totales
✅ Test PASADO: Transacción múltiple exitosa - 3 mascotas insertadas
✅ Test PASADO: Rollback funcionó correctamente - BD mantiene solo 1 mascota tras error
```

---

## 🚀 Cómo Ejecutar los Tests

### Opción 1: Desde tu IDE (Recomendado)

#### IntelliJ IDEA:
1. Abre el archivo de test (`HibernateMascotaServiceImplTest.java` o `HibernateMascotaServiceIntegrationTest.java`)
2. Para ejecutar **todos los tests de la clase**:
   - Click derecho en el nombre de la clase
   - Selecciona "Run 'NombreDelTest'"
3. Para ejecutar **un test individual**:
   - Click derecho en el nombre del método de test
   - Selecciona "Run 'nombreDelMetodo()'"
4. Verás los mensajes en la pestaña "Run" ✅

#### Eclipse:
1. Click derecho en el archivo de test
2. Run As → JUnit Test
3. Verás los resultados en la vista JUnit

#### Visual Studio Code:
1. Instala la extensión "Test Runner for Java"
2. Click en el icono de "play" junto a cada test

### Opción 2: Desde línea de comandos con Gradle

```bash
# Ejecutar TODOS los tests
./gradlew test

# Ejecutar solo tests UNITARIOS
./gradlew test --tests "com.dam.accesodatos.ra3.HibernateMascotaServiceImplTest"

# Ejecutar solo tests de INTEGRACIÓN
./gradlew test --tests "com.dam.accesodatos.ra3.HibernateMascotaServiceIntegrationTest"

# Ejecutar un test ESPECÍFICO
./gradlew test --tests "com.dam.accesodatos.ra3.HibernateMascotaServiceImplTest.createMascota_Success"
```

#### Windows PowerShell:
```powershell
.\gradlew.bat test --tests "com.dam.accesodatos.ra3.HibernateMascotaServiceImplTest"
```

---

## ⚙️ Configuración de Tests

### Tests Unitarios (Mockito)
- **No requieren** base de datos real
- **No cargan** el contexto de Spring
- **Muy rápidos** (< 2 segundos)
- Usan `@ExtendWith(MockitoExtension.class)`
- Simulan comportamiento con `@Mock`

### Tests de Integración (Spring Boot)
- **Usan** base de datos H2 en memoria
- **Cargan** el contexto completo de Spring
- **Más lentos** (~10-30 segundos)
- Configuración en: `src/test/resources/application-test.properties`

```properties
# Base de datos H2 en memoria
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
```

**Ventajas de H2 en memoria**:
- ✅ No requiere instalación de MySQL
- ✅ Rápido (todo en RAM)
- ✅ Se crea y destruye automáticamente
- ✅ No contamina la BD de desarrollo

---

## 🔍 Detalles de Implementación

### Tests Unitarios - Uso de Mocks

Los tests unitarios usan **Mockito** para simular el comportamiento de dependencias:

```java
// Mock del EntityManager
@Mock
private EntityManager entityManager;

// Configurar comportamiento simulado
when(entityManager.find(Mascota.class, 1001)).thenReturn(testMascota);

// Verificar que se llamó al método
verify(entityManager).find(Mascota.class, 1001);
```

**Ventajas**:
- No necesita base de datos
- Tests extremadamente rápidos
- Aislamiento total de dependencias
- Verifica la lógica del servicio

### Tests de Integración - Base de Datos Real

Los tests de integración usan **Spring Boot Test** con BD H2:

```java
@SpringBootTest
@ActiveProfiles("test")
@Transactional  // Rollback automático después de cada test
class HibernateMascotaServiceIntegrationTest {
    
    @Autowired
    private HibernateMascotaService service;  // Servicio real
    
    @Autowired
    private MascotaRepository repository;  // Repository real
}
```

**Ventajas**:
- Valida el flujo completo end-to-end
- Detecta problemas de integración
- Prueba con BD real (aunque sea H2)
- `@Transactional` hace rollback automático

---

## 📊 Conceptos JPA/Hibernate Testeados

### 1. Operaciones de Persistencia (CE3.d, CE3.e)
| Operación | Método EntityManager | Test |
|-----------|---------------------|------|
| **INSERT** | `persist(entity)` | createMascota |
| **SELECT** | `find(Entity.class, id)` | findMascotaByNumChip |
| **UPDATE** | `merge(entity)` | updateMascota |
| **DELETE** | `remove(entity)` | deleteMascota |

### 2. JPQL - Java Persistence Query Language (CE3.f)

#### Consultas Básicas:
```java
// JPQL usa nombres de entidades y atributos (NO tablas y columnas)
String jpql = "SELECT m FROM Mascota m WHERE m.tipoMascota = :tipo";
```

#### Consultas Dinámicas:
```java
// Construcción dinámica según filtros presentes
StringBuilder jpql = new StringBuilder("SELECT m FROM Mascota m WHERE 1=1");
if (query.getTipo() != null) {
    jpql.append(" AND m.tipoMascota = :tipo");
}
```

#### Agregaciones:
```java
// COUNT
String jpql = "SELECT COUNT(m) FROM Mascota m WHERE m.tipoMascota = :tipo";
```

### 3. Transacciones (CE3.g)
```java
@Transactional  // Commit automático al finalizar
public Mascota createMascota(MascotaCreateDto dto) {
    entityManager.persist(mascota);
    return mascota;  // Spring hace commit aquí
}

@Transactional  // Rollback automático si hay excepción
public boolean transferData(List<Mascota> mascotas) {
    for (Mascota m : mascotas) {
        entityManager.persist(m);  // Si falla, rollback de TODAS
    }
    return true;
}
```

### 4. Spring Data JPA (CE3.d)
```java
// Spring genera automáticamente la implementación
public interface MascotaRepository extends JpaRepository<Mascota, Integer> {
    // Métodos como findAll(), count(), findById() ya están implementados
}
```

---

## 🐛 Solución de Problemas

### ❌ Error: "No se puede conectar a la base de datos"
**Causa**: Falta la dependencia de H2 en modo test.  
**Solución**: Verifica que esté en `build.gradle`:
```gradle
testRuntimeOnly 'com.h2database:h2'
```

### ❌ Error: "EntityManager está cerrado"
**Causa**: El contexto de Spring no se está cargando correctamente.  
**Solución**: Verifica que la anotación `@SpringBootTest` esté presente en tests de integración.

### ❌ Error: "Constraint violation" o "Duplicate entry"
**Causa**: Datos previos en la BD de test.  
**Solución**: 
- Tests unitarios: Los mocks no tienen este problema
- Tests integración: `@Transactional` hace rollback automático

### ❌ Error: "Test passed but no output message"
**Causa**: Los mensajes `System.out.println()` pueden no mostrarse según configuración del IDE.  
**Solución**: 
- IntelliJ: Los mensajes aparecen en la pestaña "Run"
- Gradle: Usa `--info` para ver todos los logs: `./gradlew test --info`

### ❌ Tests pasan en IDE pero fallan en Gradle
**Causa**: Diferencias en el classpath o configuración.  
**Solución**: Ejecuta `./gradlew clean test` para limpiar build cache.

---

## 📚 Diferencias: Tests Unitarios vs Integración

| Aspecto | Tests Unitarios | Tests Integración |
|---------|----------------|-------------------|
| **Framework** | Mockito | Spring Boot Test |
| **Base de datos** | Mock (simulada) | H2 real |
| **Velocidad** | Muy rápida (< 2s) | Más lenta (~10-30s) |
| **Contexto Spring** | NO se carga | SÍ se carga |
| **Propósito** | Validar lógica aislada | Validar integración completa |
| **Cuando usar** | Desarrollo rápido | Antes de commit/deploy |
| **Cobertura** | Lógica de negocio | Flujos end-to-end |

---

## 📈 Reporte de Resultados

### Desde Gradle
Después de ejecutar `./gradlew test`, se genera un reporte HTML en:
```
build/reports/tests/test/index.html
```

Abre este archivo en tu navegador para ver:
- ✅ Tests pasados/fallados
- ⏱️ Tiempo de ejecución
- 📊 Porcentaje de éxito
- 🔍 Stack traces de errores (si hay)

### Desde IDE
- **IntelliJ IDEA**: Pestaña "Run" muestra resultados en tiempo real
- **Eclipse**: Vista "JUnit" muestra árbol de tests
- Ambos muestran ✅ verde (pasado) o ❌ rojo (fallado)

---

## ✅ Lista de Verificación Antes de Entregar

Antes de considerar que tu implementación está completa, asegúrate de que:

- [ ] **Todos los 17 tests unitarios pasan** ✅
- [ ] **Todos los 14 tests de integración pasan** ✅
- [ ] Los mensajes de salida son informativos y claros
- [ ] No hay warnings de compilación
- [ ] El código sigue las convenciones de nomenclatura
- [ ] Los comentarios explican el propósito de cada método
- [ ] Has ejecutado `./gradlew clean test` con éxito

---

## 🎓 Para el Estudiante

Estos tests demuestran:

1. **Separación de responsabilidades**: Tests unitarios prueban lógica, tests de integración prueban flujos completos
2. **Uso de mocks**: Mockito permite aislar la clase bajo prueba
3. **Testing transaccional**: `@Transactional` garantiza que los tests no contaminen la BD
4. **Casos límite**: No solo happy path, también errores y situaciones inesperadas
5. **Mejores prácticas**: Naming descriptivo, mensajes informativos, estructura clara

### Recursos Adicionales

- [Documentación JPA/Hibernate](https://hibernate.org/orm/documentation/)
- [Spring Data JPA Reference](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Spring Boot Testing](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing)

---

## 👨‍💻 Autor y Mantenimiento

Estos tests fueron creados como parte del RA3 (Acceso a Datos con ORM) siguiendo las implementaciones de `HibernateMascotaServiceImpl.java`.

**Última actualización**: Tests modificados para seguir el patrón de ejemplos User y con mensajes informativos de salida.

---

**¡Todos los tests deberían pasar! ✅**

Si algún test falla, revisa la implementación del método correspondiente en `HibernateMascotaServiceImpl.java` y compara con los comentarios y ejemplos proporcionados en el código.
