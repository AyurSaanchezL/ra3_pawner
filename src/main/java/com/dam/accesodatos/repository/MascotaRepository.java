package com.dam.accesodatos.repository;

import com.dam.accesodatos.model.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MascotaRepository extends JpaRepository<Mascota, Integer> {

    
    List<Mascota> findByTipoMascota(String tipoMascota);

    List<Mascota> findBySexo(String sexo);

    @Query("SELECT m FROM Mascota m WHERE LOWER(m.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Mascota> findByNombreContainingIgnoreCase(@Param("nombre") String nombre);
}
