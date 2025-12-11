# 📦 Base de Datos Embebida H2 - Guía de Uso

## 🎯 ¿Qué cambió?

El proyecto ahora usa **H2** (base de datos embebida) en lugar de MySQL. Esto significa:

✅ **No necesitas instalar MySQL**  
✅ **No necesitas configurar servidor de BD**  
✅ **No necesitas pagar hosting de base de datos**  
✅ **Todos tienen los mismos datos de prueba**  
✅ **La BD se incluye en el proyecto**

---

## 🚀 Inicio Rápido

### 1. **Clonar el proyecto** (si aún no lo tienes)
```bash
git clone <url-del-repo>
cd ra3_pawner
```

### 2. **Ejecutar la aplicación**
```bash
./gradlew bootRun
```
o desde tu IDE: Run → Spring Boot Application

### 3. **¡Listo!** 
La base de datos se crea automáticamente con 15 mascotas de prueba.

---

## 🗂️ Archivos Importantes

### `src/main/resources/schema.sql`
Define la estructura de la tabla `mascotas`:
```sql
CREATE TABLE mascotas (
    num_chip INT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    tipo_mascota VARCHAR(50) NOT NULL,
    edad INT NOT NULL,
    sexo ENUM('macho', 'hembra') NOT NULL,
    otros_detalles VARCHAR(255)
);
```

### `src/main/resources/data.sql`
Contiene 15 mascotas de prueba:
- **Perros**: Max, Rex, Bella, Rocky, Toby, Zeus, Bruno (7)
- **Gatos**: Luna, Michi, Nieve, Mia (4)
- **Conejos**: Copito, Pelusa, Canela, Coco (4)

Ejemplos:
```sql
INSERT INTO mascotas VALUES (1001, 'Max', 'Perro', 5, 'Macho', 'Golden Retriever...');
INSERT INTO mascotas VALUES (1002, 'Luna', 'Gato', 3, 'Hembra', 'Siamés...');
```

### `src/main/resources/application.yml`
Configuración de la BD H2:
```yaml
spring:
  datasource:
    url: jdbc:h2:file:./data/pawner_db
    driver-class-name: org.h2.Driver
    username: sa
    password: 
```

---

## 🖥️ Consola Web H2

Puedes ver y editar los datos directamente desde el navegador:

### 1. **Iniciar la aplicación**
```bash
./gradlew bootRun
```

### 2. **Abrir en navegador**
```
http://localhost:8083/h2-console
```

### 3. **Configurar conexión**
- **JDBC URL**: `jdbc:h2:file:./data/pawner_db`
- **User Name**: `sa`
- **Password**: *(dejar vacío)*

### 4. **Click en "Connect"**

### 5. **¡Ya puedes ejecutar SQL!**
```sql
-- Ver todas las mascotas
SELECT * FROM mascotas;

-- Contar por tipo
SELECT tipo_mascota, COUNT(*) 
FROM mascotas 
GROUP BY tipo_mascota;

-- Buscar perros
SELECT * FROM mascotas 
WHERE tipo_mascota = 'Perro';
```

---

## 📊 Datos de Prueba Incluidos

| Chip | Nombre | Tipo | Edad | Sexo | Detalles |
|------|--------|------|------|------|----------|
| 1001 | Max | Perro | 5 | Macho | Golden Retriever |
| 1002 | Luna | Gato | 3 | Hembra | Siamés |
| 1003 | Copito | Conejo | 2 | Macho | Conejo enano |
| 1004 | Rex | Perro | 7 | Macho | Pastor Alemán |
| 1005 | Michi | Gato | 4 | Hembra | Gato persa |
| 1006 | Bella | Perro | 2 | Hembra | Labrador |
| 1007 | Pelusa | Conejo | 1 | Hembra | Conejo belier |
| 1008 | Rocky | Perro | 6 | Macho | Bulldog francés |
| 1009 | Nieve | Gato | 5 | Hembra | Angora blanco |
| 1010 | Toby | Perro | 3 | Macho | Beagle |
| 1011 | Canela | Conejo | 2 | Hembra | Conejo marrón |
| 1012 | Zeus | Perro | 8 | Macho | Rottweiler |
| 1013 | Mia | Gato | 2 | Hembra | Común europeo |
| 1014 | Bruno | Perro | 4 | Macho | Boxer |
| 1015 | Coco | Conejo | 3 | Macho | Gigante de Flandes |

**Total**: 15 mascotas (7 perros, 4 gatos, 4 conejos)

---

## 🔄 Modificar Datos

### Opción 1: Editar `data.sql`
1. Abre `src/main/resources/data.sql`
2. Modifica los datos
3. **Borra la carpeta `./data/`** para forzar recreación
4. Reinicia la aplicación

### Opción 2: Usar la Consola H2
1. Accede a `http://localhost:8083/h2-console`
2. Ejecuta tus consultas SQL directamente
3. Los cambios se guardan automáticamente

### Opción 3: Usar la API
1. Usa Postman o curl para hacer peticiones
2. Los cambios se persisten en `./data/pawner_db.mv.db`

---

## 🗄️ Ubicación de la Base de Datos

La base de datos se guarda en:
```
./data/pawner_db.mv.db
```

**Importante**:
- ✅ Esta carpeta está en `.gitignore` (no se sube a Git)
- ✅ Cada desarrollador tiene su propia copia local
- ✅ Para empezar desde cero, borra la carpeta `./data/`

---

## 🔁 Resetear la Base de Datos

Si quieres volver a los datos iniciales:

### Método 1: Borrar la carpeta data
```bash
# Windows
rmdir /s data

# Linux/Mac
rm -rf data
```
Luego reinicia la aplicación y `data.sql` se ejecutará de nuevo.

### Método 2: Desde H2 Console
```sql
-- Borrar todos los datos
DELETE FROM mascotas;

-- Ejecutar de nuevo los INSERT de data.sql
-- (copiar y pegar los INSERT desde data.sql)
```

---

## ⚙️ Configuración Avanzada

### Cambiar de archivo a memoria

En `application.yml`:
```yaml
# Opción 1: Archivo (persiste) - ACTUAL
url: jdbc:h2:file:./data/pawner_db

# Opción 2: Memoria (se borra al cerrar)
url: jdbc:h2:mem:pawner_db
```

### Activar/Desactivar carga automática

En `application.yml`:
```yaml
spring:
  sql:
    init:
      mode: always  # Cambiar a 'never' para no cargar datos
```

### Cambiar ubicación de archivos BD

```yaml
# Guardar en otra carpeta
url: jdbc:h2:file:./mi-carpeta/mi-bd
```

---

## 🐛 Solución de Problemas

### ❌ Error: "Database already exists"
**Solución**: Borra la carpeta `./data/` y reinicia

### ❌ Error: "Table MASCOTAS already exists"
**Solución**: 
1. Verifica que `ddl-auto` esté en `none` en `application.yml`
2. O cambia a `validate` si quieres que Hibernate verifique el schema

### ❌ Error: "Script execution failed"
**Solución**: 
1. Revisa la sintaxis de `schema.sql` o `data.sql`
2. Mira los logs en consola para ver el error específico

### ❌ No puedo acceder a H2 Console
**Solución**: 
1. Verifica que la app esté corriendo: `http://localhost:8083`
2. Verifica que `h2.console.enabled: true` en `application.yml`
3. Usa exactamente esta URL: `http://localhost:8083/h2-console`

### ❌ Los datos no se cargan
**Solución**:
1. Verifica que `data.sql` esté en `src/main/resources/`
2. Verifica que `sql.init.mode: always` en `application.yml`
3. Borra `./data/` para forzar recreación

---

## 📚 Recursos

- [H2 Database Documentation](https://www.h2database.com/html/main.html)
- [Spring Boot + H2 Guide](https://spring.io/guides/gs/accessing-data-jpa/)
- [H2 SQL Grammar](https://www.h2database.com/html/grammar.html)

---

## ✅ Ventajas de H2

| Característica | MySQL | H2 |
|----------------|-------|-----|
| **Instalación** | Servidor separado | Incluida en proyecto |
| **Configuración** | IP, puerto, usuario, password | Automática |
| **Costo** | Hosting necesario | Gratis |
| **Portabilidad** | Requiere BD externa | Todo en el proyecto |
| **Desarrollo** | Cada uno necesita MySQL | Funciona out-of-the-box |
| **Tests** | Necesita BD separada | Misma BD |
| **Velocidad** | Red | Archivo local (más rápido) |

---

## 🔄 Migrar de H2 a MySQL (Producción)

Si en el futuro quieres usar MySQL en producción:

1. **Cambiar configuración en `application.yml`**:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/pawner_db
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: tu_usuario
    password: tu_password
  jpa:
    database-platform: org.hibernate.dialect.MySQLDialect
```

2. **Ejecutar scripts en MySQL**:
```bash
mysql -u root -p < src/main/resources/schema.sql
mysql -u root -p < src/main/resources/data.sql
```

3. **¡Listo!** El código de la aplicación no cambia.

---

**¿Preguntas?** Revisa los logs en la consola cuando inicies la aplicación. Ahí verás si los scripts se ejecutaron correctamente.
