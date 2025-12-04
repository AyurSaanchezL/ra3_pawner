package com.dam.accesodatos.ra3;

import com.dam.accesodatos.model.Mascota;
import com.dam.accesodatos.model.MascotaCreateDto;
import com.dam.accesodatos.model.MascotaUpdateDto;
import com.dam.accesodatos.model.MascotaQueryDto;
import com.dam.accesodatos.repository.MascotaRepository;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class HibernateMascotaServiceImpl implements HibernateMascotaService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private MascotaRepository mascotaRepository;

    @Override
    public String testEntityManager() {
        if (!entityManager.isOpen()) {
            throw new RuntimeException("EntityManager está cerrado");
        }
        Query query = entityManager.createNativeQuery("SELECT 1 as test, DATABASE() as db_name");
        Object[] result = (Object[]) query.getSingleResult();
        return String.format("✓ EntityManager activo | Base de datos: %s | Test: %s", result[1], result[0]);
    }

    @Override
    @Transactional
    public Mascota createMascota(MascotaCreateDto dto) {
        Mascota mascota = new Mascota();
        mascota.setNumChip(dto.getNumChip());
        mascota.setNombre(dto.getNombre());
        mascota.setTipoMascota(dto.getTipoMascota());
        mascota.setEdad(dto.getEdad());
        mascota.setSexo(dto.getSexo());
        mascota.setOtrosDetalles(dto.getOtrosDetalles());
        entityManager.persist(mascota);
        return mascota;
    }

    @Override
    public Mascota findMascotaById(Integer id) {
        return entityManager.find(Mascota.class, id);
    }

    @Override
    @Transactional
    public Mascota updateMascota(Integer id, MascotaUpdateDto dto) {
        Mascota existing = findMascotaById(id);
        if (existing == null) {
            throw new RuntimeException("No se encontró mascota con número de chip " + id);
        }
        dto.applyTo(existing);
        return entityManager.merge(existing);
    }

    @Override
    @Transactional
    public boolean deleteMascota(Integer id) {
        Mascota mascota = findMascotaById(id);
        if (mascota == null) {
            return false;
        }
        entityManager.remove(mascota);
        return true;
    }

    @Override
    public List<Mascota> findAll() {
        return mascotaRepository.findAll();
    }

    @Override
    public List<Mascota> findMascotasByTipo(String tipo) {
        String jpql = "SELECT m FROM Mascota m WHERE m.tipoMascota = :tipo";
        TypedQuery<Mascota> query = entityManager.createQuery(jpql, Mascota.class);
        query.setParameter("tipo", tipo);
        return query.getResultList();
    }

    @Override
    public List<Mascota> searchMascotas(MascotaQueryDto queryDto) {
        StringBuilder jpql = new StringBuilder("SELECT m FROM Mascota m WHERE 1=1");
        if (queryDto.getTipoMascota() != null) {
            jpql.append(" AND m.tipoMascota = :tipo");
        }
        if (queryDto.getSexo() != null) {
            jpql.append(" AND m.sexo = :sexo");
        }
        TypedQuery<Mascota> query = entityManager.createQuery(jpql.toString(), Mascota.class);
        if (queryDto.getTipoMascota() != null) {
            query.setParameter("tipo", queryDto.getTipoMascota());
        }
        if (queryDto.getSexo() != null) {
            query.setParameter("sexo", queryDto.getSexo());
        }
        return query.getResultList();
    }

    @Override
    @Transactional
    public boolean transferData(List<Mascota> mascotas) {
        for (Mascota mascota : mascotas) {
            entityManager.persist(mascota);
        }
        return true;
    }

    @Override
    public long executeCountByTipo(String tipo) {
        String jpql = "SELECT COUNT(m) FROM Mascota m WHERE m.tipoMascota = :tipo";
        TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
        query.setParameter("tipo", tipo);
        return query.getSingleResult();
    }
}
