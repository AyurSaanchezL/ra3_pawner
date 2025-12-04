package com.dam.accesodatos.ra3;

import com.dam.accesodatos.model.Mascota;
import com.dam.accesodatos.model.MascotaCreateDto;
import com.dam.accesodatos.model.MascotaUpdateDto;
import com.dam.accesodatos.model.MascotaQueryDto;
import org.springframework.ai.mcp.server.annotation.Tool;

import java.util.List;

public interface HibernateMascotaService {

    @Tool(name = "test_entity_manager",
          description = "Prueba el EntityManager de Hibernate/JPA")
    String testEntityManager();

    @Tool(name = "create_mascota",
          description = "Persiste una nueva mascota usando EntityManager.persist() y @Transactional")
    Mascota createMascota(MascotaCreateDto dto);

    @Tool(name = "find_mascota_by_id",
          description = "Busca una mascota por su número de chip usando EntityManager.find()")
    Mascota findMascotaById(Integer id);

    @Tool(name = "update_mascota",
          description = "Actualiza una mascota existente usando EntityManager.merge() y @Transactional")
    Mascota updateMascota(Integer id, MascotaUpdateDto dto);

    @Tool(name = "delete_mascota",
          description = "Elimina una mascota usando EntityManager.remove() y @Transactional")
    boolean deleteMascota(Integer id);

    @Tool(name = "find_all_mascotas",
          description = "Obtiene todas las mascotas usando JPA Repository.findAll()")
    List<Mascota> findAll();

    @Tool(name = "find_mascotas_by_tipo",
          description = "Busca mascotas por tipo usando JPQL")
    List<Mascota> findMascotasByTipo(String tipo);

    @Tool(name = "search_mascotas",
          description = "Busca mascotas con filtros dinámicos usando JPQL")
    List<Mascota> searchMascotas(MascotaQueryDto query);

    @Tool(name = "transfer_data",
          description = "Inserta múltiples mascotas en una transacción usando @Transactional")
    boolean transferData(List<Mascota> mascotas);

    @Tool(name = "execute_count_by_tipo",
          description = "Ejecuta consulta COUNT usando JPQL")
    long executeCountByTipo(String tipo);
}
