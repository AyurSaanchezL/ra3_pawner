package com.dam.accesodatos.ra3;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dam.accesodatos.model.Mascota;
import com.dam.accesodatos.model.MascotaCreateDto;
import com.dam.accesodatos.model.MascotaQueryDto;
import com.dam.accesodatos.model.MascotaUpdateDto;
import com.dam.accesodatos.repository.MascotaRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

/**
 * Implementación del servicio Hibernate/JPA para gestión de usuarios
 *
 * VERSIÓN MÍNIMOS ESTRICTOS
 *
 * ESTRUCTURA DE IMPLEMENTACIÓN:
 * - ✅ 6 MÉTODOS IMPLEMENTADOS (ejemplos para estudiantes)
 * - ❌ 4 MÉTODOS TODO (estudiantes deben implementar - mínimo para aprobar RA3)
 *
 * MÉTODOS IMPLEMENTADOS (Ejemplos):
 * 1. testEntityManager() - Ejemplo básico de EntityManager
 * 2. createMascota() - INSERT con persist() y @Transactional
 * 3. findMascotaById() - SELECT con find()
 * 4. updateMascota() - UPDATE con merge()
 * 5. findAll() - SELECT all con Repository
 * 6. findMascotasByTipo() - JPQL básico
 *
 * MÉTODOS TODO (Estudiantes implementan - MÍNIMOS):
 * 1. deleteMascota() - EntityManager.remove()
 * 2. searchMascotas() - JPQL dinámico (simplificado, sin Criteria API)
 * 3. transferData() - Transacción múltiple
 * 4. executeCountByTipo() - JPQL COUNT
 */
@Service
@Transactional(readOnly = true) // Transacciones de solo lectura por defecto
public class HibernateMascotaServiceImpl implements HibernateMascotaService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private MascotaRepository mascotaRepository;

    // ========== CE3.a: Configuración y Conexión ORM ==========

    /**
     * ✅ EJEMPLO IMPLEMENTADO 1/6: Prueba EntityManager
     *
     * Este método muestra el patrón fundamental de JPA:
     * 1. Verificar que EntityManager esté abierto
     * 2. Ejecutar una query nativa simple
     * 3. Procesar resultados
     *
     * DIFERENCIAS vs RA2 (JDBC):
     * - RA2: Connection.isClosed(), Statement.executeQuery("SELECT 1")
     * - RA3: EntityManager.isOpen(), createNativeQuery()
     */
    @Override
    public String testEntityManager() {
        if (!entityManager.isOpen()) {
            throw new RuntimeException("EntityManager está cerrado");
        }
        // Ejecutar query nativa simple (SQL directo, no JPQL)
        Query query = entityManager.createNativeQuery("SELECT 1 as test, DATABASE() as db_name");
        Object[] result = (Object[]) query.getSingleResult();
        return String.format("✓ EntityManager activo | Base de datos: %s | Test: %s", result[1], result[0]);
    }

    // ========== CE3.d, CE3.e: Operaciones CRUD ==========

    /**
     * ✅ EJEMPLO IMPLEMENTADO 2/6: INSERT con persist()
     *
     * Muestra cómo Hibernate simplifica INSERT:
     * - NO necesitas escribir SQL INSERT
     * - NO necesitas mapear parámetros manualmente
     * - NO necesitas getGeneratedKeys()
     * - Hibernate lo hace todo automáticamente
     *
     * DIFERENCIAS vs RA2 (JDBC):
     * - RA2: PreparedStatement.setString(), executeUpdate(), getGeneratedKeys()
     * - RA3: entityManager.persist(mascota), todo automático
     *
     * IMPORTANTE: @Transactional es obligatorio para operaciones que modifican BD
     */
    @Override
    @Transactional // ← CRÍTICO: Modifica BD, necesita transacción
    public Mascota createMascota(MascotaCreateDto dto) {
        // Crear entidad desde DTO
        Mascota mascota = new Mascota();
        mascota.setNumChip(dto.getNumChip());
        mascota.setNombre(dto.getNombre());
        mascota.setTipoMascota(dto.getTipoMascota());
        mascota.setEdad(dto.getEdad());
        mascota.setSexo(dto.getSexo());
        mascota.setOtrosDetalles(dto.getOtrosDetalles());

        // persist() guarda la entidad en el contexto de persistencia
        // Hibernate genera automáticamente:
        // INSERT INTO mascota (numChip, nombre, tipoMascota, edad, sexo, otrosDetalles)
        // VALUES (?, ?, ?, ?, ?, ?)
        entityManager.persist(mascota);

        // Al finalizar el método, Spring hace commit automáticamente
        // Hibernate ejecuta el INSERT y setea el ID generado
        return mascota;
    }

    /**
     * ✅ EJEMPLO IMPLEMENTADO 3/6: SELECT por ID con find()
     *
     * Muestra la forma más simple de recuperar una entidad por ID.
     *
     * DIFERENCIAS vs RA2 (JDBC):
     * - RA2: PreparedStatement con "SELECT * FROM users WHERE id = ?", mapeo manual de ResultSet
     * - RA3: entityManager.find(Mascota.class, id), todo automático
     *
     * NOTA: find() retorna null si no existe (no lanza excepción)
     */
    @Override
    public Mascota findMascotaByNumChip(Integer numChip) {
        // find() es la forma más directa de buscar por ID
        // Hibernate genera: SELECT ... FROM users WHERE id = ?
        // y mapea automáticamente las columnas a los atributos de User        
        return entityManager.find(Mascota.class, numChip);
    }

    /**
     * ✅ EJEMPLO IMPLEMENTADO 4/6: UPDATE con merge()
     *
     * Muestra cómo actualizar una entidad existente.
     *
     * DIFERENCIAS vs RA2 (JDBC):
     * - RA2: PreparedStatement con "UPDATE users SET ... WHERE id = ?"
     * - RA3: entityManager.merge(mascota), Hibernate detecta cambios automáticamente
     *
     * PATRÓN IMPORTANTE:
     * 1. Buscar entidad existente
     * 2. Modificar atributos
     * 3. merge() sincroniza cambios con BD
     */
    @Override
    @Transactional  // ← Modifica BD
    public Mascota updateMascota(Integer numChip, MascotaUpdateDto dto) {
        // 1. Buscar entidad existente
        Mascota existing = findMascotaByNumChip(numChip);
        if (existing == null) {
            throw new RuntimeException("No se encontró mascota con número de chip " + numChip);
        }

        // 2. Aplicar cambios del DTO
        dto.applyTo(existing);

        // 3. merge() actualiza la entidad
        // Hibernate detecta qué campos cambiaron y genera UPDATE solo de esos campos
        return entityManager.merge(existing);

        // Al finalizar, Spring hace commit e Hibernate ejecuta el UPDATE
    }

    /*
     * 🚨 PARA PRESENTAR 🚨
     */
    @Override
    @Transactional
    public boolean deleteMascota(Integer numChip) {
        /*
         Guía de implementación:
         1. Buscar mascota: Mascota mascota = findMascotaByNumChip(numChip);
        
         2. Verificar si existe:
            if (mascota == null) return false;
        
         3. Eliminar con remove():
            entityManager.remove(mascota);
        
         4. Retornar true
        
         IMPORTANTE: remove() requiere que la entidad esté managed (en contexto de persistencia)
         Por eso primero la buscamos con find()
        
         DIFERENCIA vs RA2:
         - RA2: DELETE FROM users WHERE id = ?
         - RA3: entityManager.remove(mascota)
        */

    //  Mascota mascota = entityManager.find(Mascota.class, numChip);
        Mascota mascota = findMascotaByNumChip(numChip);
        if (mascota == null) {
            return false;
        }
        entityManager.remove(mascota);
        return true;
    }

    /**
     * ✅ EJEMPLO IMPLEMENTADO 5/6: SELECT all con Repository
     *
     * Muestra cómo usar Spring Data JPA Repository.
     * La forma más simple de obtener todas las entidades.
     *
     * DIFERENCIAS vs RA2 (JDBC):
     * - RA2: while(rs.next()) { mapResultSetToUser(rs); }
     * - RA3: mascotaRepository.findAll(), todo automático
     */
    @Override
    public List<Mascota> findAll() {
        // Spring Data JPA genera automáticamente:
        // SELECT m FROM Mascota m
        // y mapea resultados a List<Mascota>
        return mascotaRepository.findAll();
    }

    // ========== CE3.f: Consultas JPQL ==========

    /**
     * ✅ EJEMPLO IMPLEMENTADO 6/6: JPQL básico
     *
     * Muestra cómo escribir consultas JPQL (Java Persistence Query Language).
     *
     * DIFERENCIAS vs RA2 (JDBC):
     * - RA2: SQL "SELECT * FROM users WHERE department = ?"
     * - RA3: JPQL "SELECT m FROM Mascota m WHERE m.tipo = :tipo"
     *
     * IMPORTANTE: JPQL usa nombres de entidades y atributos, NO tablas y columnas
     * - Correcto: "FROM Mascota m" (entidad), "m.tipo" (atributo)
     * - Incorrecto: "FROM Mascotas m" (tabla), "m.tipo_mascota" (columna)
     */
    @Override
    public List<Mascota> findMascotasByTipo(String tipo) {
        // JPQL: Query language orientado a objetos
        // - Mascota (entidad) en lugar de mascotas (tabla)
        // - m.tipo (atributo) en lugar de tipoMascota (columna)
        String jpql = "SELECT m FROM Mascota m WHERE m.tipoMascota = :tipo";

        // TypedQuery garantiza type-safety
        TypedQuery<Mascota> query = entityManager.createQuery(jpql, Mascota.class);
        query.setParameter("tipo", tipo);

        // getResultList() retorna List<Mascota>
        return query.getResultList();
    }

    /*
     * 🚨 PARA PRESENTAR 🚨
     */
    @Override
    public List<Mascota> searchMascotas(MascotaQueryDto queryDto) {
        /*
           VERSIÓN SIMPLIFICADA: Usa JPQL en lugar de Criteria API
          
           Guía de implementación:
           1. Construir JPQL dinámicamente:
              StringBuilder jpql = new StringBuilder("SELECT u FROM User u WHERE 1=1");
          
           2. Añadir condiciones según filtros presentes:
              if (queryDto.getDepartment() != null) {
                  jpql.append(" AND u.department = :dept");
              }
              if (queryDto.getRole() != null) {
                  jpql.append(" AND u.role = :role");
              }
              if (queryDto.getActive() != null) {
                  jpql.append(" AND u.active = :active");
              }
          
           3. Crear TypedQuery:
              TypedQuery<User> query = entityManager.createQuery(jpql.toString(), User.class);
          
           4. Setear parámetros solo para filtros presentes:
              if (queryDto.getDepartment() != null) {
                  query.setParameter("dept", queryDto.getDepartment());
              }
              // ... repetir para role y active
          
           5. Ejecutar y retornar:
              return query.getResultList();
          
           VENTAJA vs RA2: Parámetros nombrados evitan SQL injection
        */

        StringBuilder jpql = new StringBuilder("SELECT m FROM Mascota m WHERE 1=1");
        if (queryDto.getNombre() != null){
            jpql.append("AND m.nombre = :nombre");
        }
        if (queryDto.getTipoMascota() != null) {
            jpql.append(" AND m.tipoMascota = :tipo");
        }
        if (queryDto.getSexo() != null) {
            jpql.append(" AND m.sexo = :sexo");
        }
        TypedQuery<Mascota> query = entityManager.createQuery(jpql.toString(), Mascota.class);
        if (queryDto.getNombre() != null){
            query.setParameter("nombre", queryDto.getNombre());
        }
        if (queryDto.getTipoMascota() != null) {
            query.setParameter("tipo", queryDto.getTipoMascota());
        }
        if (queryDto.getSexo() != null) {
            query.setParameter("sexo", queryDto.getSexo());
        }
        return query.getResultList();
    }

    // ========== CE3.g: Transacciones ==========

    /*
     * 🚨 PARA PRESENTAR 🚨
     */
    @Override
    @Transactional
    public boolean transferData(List<Mascota> mascotas) {
        /*
           Guía de implementación:
           1. Iterar sobre usuarios:
              for (User user : users) {
                  entityManager.persist(user);
              }
          
           2. Si todo OK, Spring hace commit automáticamente al finalizar el método
          
           3. Si hay error (excepción), Spring hace rollback automáticamente
          
           4. Retornar true
          
           DIFERENCIA vs RA2:
           - RA2: conn.setAutoCommit(false), try-catch con commit()/rollback() manual
           - RA3: @Transactional maneja todo automáticamente
          
           NOTA PEDAGÓGICA:
           Esto demuestra la potencia de @Transactional de Spring:
           - No necesitas setAutoCommit(false)
           - No necesitas commit() manual
           - No necesitas rollback() manual en catch
           - Spring lo hace automáticamente según el resultado del método
        */

        // .persist() guarda la mascota en la base de datos
        for (Mascota mascota : mascotas) {
            entityManager.persist(mascota);
        }
        return true;
    }

    /*
     * 🚨 PARA PRESENTAR 🚨
     */
    @Override
    public long executeCountByTipo(String tipo) {
        /*
           Guía de implementación:
           1. Crear JPQL COUNT query:
              String jpql = "SELECT COUNT(u) FROM User u WHERE u.department = :dept AND u.active = true";
          
           2. Crear TypedQuery<Long>:
              TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
          
           3. Setear parámetro:
              query.setParameter("dept", department);
          
           4. Ejecutar y retornar:
              return query.getSingleResult();
          
           DIFERENCIA vs RA2:
           - RA2: CallableStatement para stored procedure
           - RA3: JPQL COUNT query directo (más simple)
        */

        String jpql = "SELECT COUNT(m) FROM Mascota m WHERE m.tipoMascota = :tipo";
        TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
        query.setParameter("tipo", tipo);
        return query.getSingleResult();
    }
}
