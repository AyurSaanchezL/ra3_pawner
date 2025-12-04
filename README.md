# Proyecto RA3 - Acceso a Datos con Hibernate/JPA

> **Servidor Educativo con Spring, Hibernate/JPA y MySQL para la materia de Acceso a Datos.**

Este proyecto es una herramienta de aprendizaje diseñada para estudiantes de DAM (Desarrollo de Aplicaciones Multiplataforma). Su objetivo es demostrar el uso de un ORM (Object-Relational Mapping) como **Hibernate/JPA** para la persistencia de datos, en contraste con el acceso a datos mediante JDBC nativo.

La aplicación implementa un servidor simple que gestiona una entidad `Mascota` y expone varias operaciones CRUD (Crear, Leer, Actualizar, Borrar) a través de una API.

## Tabla de Contenidos

- [Stack Tecnológico](#stack-tecnológico)
- [Requisitos Previos](#requisitos-previos)
- [1. Guía de Conexión a la Base de Datos](#1-guía-de-conexión-a-la-base-de-datos)
- [2. Instalación y Ejecución](#2-instalación-y-ejecución)
- [3. Dudas Comunes y Errores Frecuentes (FAQ)](#3-dudas-comunes-y-errores-frecuentes-faq)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Metodología de Aprendizaje](#metodología-de-aprendizaje)
- [Documentación Complementaria](#documentación-complementaria)

## Stack Tecnológico

| Componente | Versión | Propósito |
|-------------------------|-----------|-----------------------------------|
| **Java** | 21 (LTS) | Lenguaje de programación |
| **Spring Boot** | 3.x.x | Framework de aplicación |
| **Spring Data JPA** | 3.x.x | Abstracción sobre JPA/Hibernate |
| **Hibernate** | 6.x | Implementación ORM (JPA provider) |
| **MySQL** | 8.x | Base de datos principal |
| **H2 Database** | Latest | Base de datos en memoria (para tests) |
| **Gradle** | 8.x | Herramienta de construcción |
| **JUnit 5 (Jupiter)** | 5.10.x | Framework de testing |

## Requisitos Previos

- **Java 21** o superior ([Descargar OpenJDK](https://adoptium.net/)).
- **Servidor de MySQL** instalado y en ejecución. Puedes usar Docker, XAMPP, WAMP o una instalación nativa.
- **Git** para clonar el repositorio.
- **IDE/Editor**: IntelliJ IDEA, VS Code, o Eclipse.

---

## 1. Guía de Conexión a la Base de Datos

Para que la aplicación funcione, necesita conectarse a tu base de datos MySQL. Sigue estos pasos:

### Paso 1: Asegúrate de que MySQL está en ejecución

Verifica que tu servidor de base de datos MySQL está activo y accesible.

### Paso 2: Edita el fichero de configuración

Abre el archivo de configuración principal del proyecto, ubicado en:
`src/main/resources/application.yml`

### Paso 3: Modifica las credenciales de la base de datos

Dentro del archivo, busca la sección `datasource` y actualiza los siguientes campos con tus propios datos:

```yaml
spring:
  # ... (otras configuraciones)

  datasource:
    # 1. Revisa la URL: cambia 'localhost', '3306' y 'pawner_db' si es necesario.
    url: jdbc:mysql://localhost:3306/pawner_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC

    # 2. Reemplaza 'tu_usuario' con tu nombre de usuario de MySQL.
    username: tu_usuario

    # 3. Reemplaza 'tu_password' con tu contraseña de MySQL.
    password: tu_password

  # ... (otras configuraciones)
```

- **`url`**: La dirección de tu base de datos. `createDatabaseIfNotExist=true` creará la base de datos `pawner_db` si no existe, lo cual es muy conveniente.
- **`username`**: Tu usuario de MySQL (a menudo es `root`).
- **`password`**: La contraseña asociada a ese usuario.

**¡Y listo!** Al guardar el archivo, la aplicación ya sabrá cómo conectarse a tu base de datos.

---

## 2. Instalación y Ejecución

```bash
# 1. Clona el repositorio en tu máquina
git clone <URL_DEL_REPOSITORIO_GIT>
cd ra3_pawner

# 2. Verifica tu versión de Java
java -version
# Debe mostrar la versión 21 o superior.

# 3. Ejecuta la aplicación usando el Gradle Wrapper
# (Este comando compila y lanza el servidor)
./gradlew bootRun
```

Si todo ha ido bien, verás en la consola los logs de Spring Boot y de Hibernate. El servidor se estará ejecutando en `http://localhost:8083`.

### Ejecutar los Tests

Para verificar que la lógica de negocio funciona correctamente (sin depender de la configuración de MySQL), puedes ejecutar la suite de tests:

```bash
./gradlew test
```

Los tests están configurados para usar una base de datos H2 en memoria, por lo que no requieren ninguna configuración adicional.

---

## 3. Dudas Comunes y Errores Frecuentes (FAQ)

### P: El `README` antiguo y otros documentos mencionan H2, pero la configuración usa MySQL. ¿Cuál es el correcto?

**R:** La aplicación principal **usa MySQL**. La documentación anterior no estaba actualizada. El proyecto ha sido modificado para usar MySQL como base de datos principal para acercarse a un entorno más realista. La base de datos H2 solo se utiliza en el entorno de testing (`src/test`) para que las pruebas sean rápidas y no necesiten configuración externa.

### P: Me aparece un error `Access denied for user 'tu_usuario'@'localhost'` o `Cannot connect to database`.

**R:** Este es el error más común. Significa que las credenciales en `application.yml` son incorrectas o el servidor MySQL no está disponible.
1.  **Verifica que tu servidor MySQL está en ejecución.**
2.  **Revisa 100%** que el `username` y `password` en `application.yml` son exactamente los mismos que usas para acceder a MySQL.
3.  Asegúrate de que el usuario tiene permisos sobre la base de datos `pawner_db`.

### P: ¿Qué significa `jpa.hibernate.ddl-auto: update` y por qué es importante?

**R:** Esta propiedad le dice a Hibernate que **compare las clases de tu modelo (como `Mascota.java`) con el esquema de la base de datos al arrancar la aplicación**.
- Si una tabla o columna no existe, Hibernate **la creará automáticamente**.
- Si hay cambios, intentará **modificar la tabla**.

Es extremadamente útil para desarrollo, ya que te ahorra tener que escribir sentencias SQL (`ALTER TABLE`, `CREATE TABLE`) manualmente. Sin embargo, en un entorno de producción real, debe usarse con mucho cuidado o desactivarse (`none`), ya que podría causar pérdidas de datos si realiza un cambio inesperado en la estructura.

### P: La aplicación no arranca y dice `Port 8083 was already in use`.

**R:** Significa que otro programa (o una instancia anterior de esta misma aplicación) ya está usando ese puerto. Puedes solucionarlo de dos maneras:
1.  **Detener el otro proceso:** Busca qué aplicación usa el puerto y ciérrala.
2.  **Cambiar el puerto:** En `application.yml`, cambia el valor de `server.port` a otro número, por ejemplo, `8084`.

## Estructura del Proyecto

```
ra3_pawner/
├── src/main/java/com/dam/accesodatos/
│   ├── McpAccesoDatosRa3Application.java  # Aplicación principal
│   ├── model/                              # Entidades JPA y DTOs
│   │   ├── Mascota.java                    # Entidad @Entity mapeada
│   │   └── ...Dto.java                     # DTOs para la API
│   ├── ra3/                                # Servicios con lógica de negocio
│   ├── repository/                         # Repositorios Spring Data JPA
│   └── mcp/                                # Componentes del servidor MCP
├── src/main/resources/
│   └── application.yml                     # ⭐ CONFIGURACIÓN DE BBDD
├── src/test/java/                          # Tests unitarios e integración
├── docs/                                   # Documentación detallada
└── build.gradle                            # Dependencias y configuración de Gradle
```

## Metodología de Aprendizaje

Este proyecto está diseñado para que aprendas haciendo:
1.  **Estudia los métodos ya implementados** en la capa de servicio (`ra3` package). Contienen comentarios que explican el funcionamiento de `persist()`, `find()`, `merge()`, etc.
2.  **Implementa los métodos marcados con `// TODO`**. Son ejercicios diseñados para que apliques lo aprendido.
3.  **Valida tus implementaciones** ejecutando los tests (`./gradlew test`). Hay tests específicos para cada método `TODO`.

## Documentación Complementaria

Este proyecto incluye una carpeta `/docs` con guías muy detalladas. **Es muy recomendable leerlas.**

- **`/docs/GUIA_ESTUDIANTE.md`**: El mejor punto de partida. Te guía paso a paso.
- **`/docs/ARQUITECTURA.md`**: Explica cómo están organizadas las capas del software.
- **`/docs/EJEMPLOS`**: Contiene ejemplos prácticos de uso.