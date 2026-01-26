package com.dam.accesodatos.ra3;

import com.dam.accesodatos.model.Mascota;
import com.dam.accesodatos.model.MascotaCreateDto;
import com.dam.accesodatos.model.MascotaQueryDto;
import com.dam.accesodatos.model.MascotaUpdateDto;
import com.dam.accesodatos.repository.MascotaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para los métodos IMPLEMENTADOS de HibernateMascotaServiceImpl
 *
 * Estos tests cubren los métodos implementados usando mocks de Mockito.
 * Los estudiantes pueden usarlos como guía para testear sus propias implementaciones.
 *
 * COBERTURA: Tests que validan los métodos implementados:
 * 1. testEntityManager() - 2 tests
 * 2. createMascota() - 1 test
 * 3. findMascotaByNumChip() - 2 tests
 * 4. updateMascota() - 2 tests
 * 5. deleteMascota() - 2 tests
 * 6. findAll() - 1 test
 * 7. findMascotasByTipo() - 2 tests
 * 8. searchMascotas() - 3 tests
 * 9. executeCountByTipo() - 2 tests
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests Unitarios - Métodos Implementados")
class HibernateMascotaServiceImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private MascotaRepository mascotaRepository;

    @InjectMocks
    private HibernateMascotaServiceImpl service;

    private Mascota testMascota;
    private MascotaCreateDto createDto;
    private MascotaUpdateDto updateDto;
    private MascotaQueryDto queryDto;

    @BeforeEach
    void setUp() {
        // Mascota de prueba - chip 1001 = Max (Perro)
        testMascota = new Mascota();
        testMascota.setNumChip(1001);
        testMascota.setNombre("Max");
        testMascota.setTipoMascota("Perro");
        testMascota.setEdad(5);
        testMascota.setSexo("Macho");
        testMascota.setOtrosDetalles("Golden Retriever, castrado");

        // DTO para crear - chip 1002 = Luna (Gato)
        createDto = new MascotaCreateDto();
        createDto.setNumChip(1002);
        createDto.setNombre("Luna");
        createDto.setTipoMascota("Gato");
        createDto.setEdad(3);
        createDto.setSexo("Hembra");
        createDto.setOtrosDetalles("Siamés, todas las vacunas");

        // DTO para actualizar
        updateDto = new MascotaUpdateDto();
        updateDto.setNombre("Maximus");
        updateDto.setEdad(6);

        // DTO para búsqueda
        queryDto = new MascotaQueryDto();
    }

    // ========== Tests para testEntityManager() ==========

    @Test
    @DisplayName("testEntityManager() - Verifica EntityManager activo")
    void testEntityManager_Success() {
        // Given
        when(entityManager.isOpen()).thenReturn(true);
        Query query = mock(Query.class);
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(new Object[]{1, "H2"});

        // When
        String result = service.testEntityManager();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("EntityManager activo"));
        assertTrue(result.contains("H2"));
        verify(entityManager).isOpen();
        verify(entityManager).createNativeQuery(anyString());
        
        System.out.println("✅ Test PASADO: " + result);
    }

    @Test
    @DisplayName("testEntityManager() - Falla si EntityManager cerrado")
    void testEntityManager_ClosedEntityManager() {
        // Given
        when(entityManager.isOpen()).thenReturn(false);

        // When & Then
        assertThrows(RuntimeException.class, () -> service.testEntityManager());
        verify(entityManager).isOpen();
        verify(entityManager, never()).createNativeQuery(anyString());
        
        System.out.println("✅ Test PASADO: EntityManager cerrado lanza excepción correctamente");
    }

    // ========== Tests para createMascota() ==========

    @Test
    @DisplayName("createMascota() - Crea mascota correctamente")
    void createMascota_Success() {
        // Given
        doNothing().when(entityManager).persist(any(Mascota.class));

        // When
        Mascota result = service.createMascota(createDto);

        // Then
        assertNotNull(result);
        assertEquals(createDto.getNumChip(), result.getNumChip());
        assertEquals(createDto.getNombre(), result.getNombre());
        assertEquals(createDto.getTipoMascota(), result.getTipoMascota());
        assertEquals(createDto.getEdad(), result.getEdad());
        assertEquals(createDto.getSexo(), result.getSexo());
        assertEquals(createDto.getOtrosDetalles(), result.getOtrosDetalles());
        verify(entityManager).persist(any(Mascota.class));
        
        System.out.println("✅ Test PASADO: Mascota creada con éxito - " + result.getNombre() + " (ID: " + result.getNumChip() + ")");
    }

    // ========== Tests para findMascotaByNumChip() ==========

    @Test
    @DisplayName("findMascotaByNumChip() - Encuentra mascota existente")
    void findMascotaByNumChip_Found() {
        // Given
        when(entityManager.find(Mascota.class, 1001)).thenReturn(testMascota);

        // When
        Mascota result = service.findMascotaByNumChip(1001);

        // Then
        assertNotNull(result);
        assertEquals(testMascota.getNumChip(), result.getNumChip());
        assertEquals(testMascota.getNombre(), result.getNombre());
        verify(entityManager).find(Mascota.class, 1001);
        
        System.out.println("✅ Test PASADO: Mascota encontrada - " + result.getNombre() + " (ID: " + result.getNumChip() + ")");
    }

    @Test
    @DisplayName("findMascotaByNumChip() - Retorna null si no existe")
    void findMascotaByNumChip_NotFound() {
        // Given
        when(entityManager.find(Mascota.class, 9999)).thenReturn(null);

        // When
        Mascota result = service.findMascotaByNumChip(9999);

        // Then
        assertNull(result);
        verify(entityManager).find(Mascota.class, 9999);
        
        System.out.println("✅ Test PASADO: Mascota no encontrada (ID: 9999) - retorna null correctamente");
    }

    // ========== Tests para updateMascota() ==========

    @Test
    @DisplayName("updateMascota() - Actualiza mascota existente")
    void updateMascota_Success() {
        // Given
        when(entityManager.find(Mascota.class, 1001)).thenReturn(testMascota);
        when(entityManager.merge(any(Mascota.class))).thenReturn(testMascota);

        // When
        Mascota result = service.updateMascota(1001, updateDto);

        // Then
        assertNotNull(result);
        assertEquals(updateDto.getNombre(), testMascota.getNombre());
        assertEquals(updateDto.getEdad(), testMascota.getEdad());
        verify(entityManager).find(Mascota.class, 1001);
        verify(entityManager).merge(any(Mascota.class));
        
        System.out.println("✅ Test PASADO: Mascota actualizada - Nuevo nombre: " + updateDto.getNombre() + ", Nueva edad: " + updateDto.getEdad());
    }

    @Test
    @DisplayName("updateMascota() - Lanza excepción si mascota no existe")
    void updateMascota_NotFound() {
        // Given
        when(entityManager.find(Mascota.class, 9999)).thenReturn(null);

        // When & Then
        assertThrows(RuntimeException.class, () -> service.updateMascota(9999, updateDto));
        verify(entityManager).find(Mascota.class, 9999);
        verify(entityManager, never()).merge(any(Mascota.class));
        
        System.out.println("✅ Test PASADO: Update con ID inexistente (9999) lanza excepción correctamente");
    }

    // ========== Tests para deleteMascota() ==========

    @Test
    @DisplayName("deleteMascota() - Elimina mascota existente")
    void deleteMascota_Success() {
        // Given
        when(entityManager.find(Mascota.class, 1001)).thenReturn(testMascota);
        doNothing().when(entityManager).remove(any(Mascota.class));

        // When
        boolean result = service.deleteMascota(1001);

        // Then
        assertTrue(result);
        verify(entityManager).find(Mascota.class, 1001);
        verify(entityManager).remove(testMascota);
        
        System.out.println("✅ Test PASADO: Mascota eliminada con éxito (ID: 1001)");
    }

    @Test
    @DisplayName("deleteMascota() - Retorna false si mascota no existe")
    void deleteMascota_NotFound() {
        // Given
        when(entityManager.find(Mascota.class, 9999)).thenReturn(null);

        // When
        boolean result = service.deleteMascota(9999);

        // Then
        assertFalse(result);
        verify(entityManager).find(Mascota.class, 9999);
        verify(entityManager, never()).remove(any(Mascota.class));
        
        System.out.println("✅ Test PASADO: Delete con ID inexistente (9999) retorna false correctamente");
    }

    // ========== Tests para findAll() ==========

    @Test
    @DisplayName("findAll() - Retorna todas las mascotas")
    void findAll_Success() {
        // Given
        List<Mascota> mascotas = Arrays.asList(testMascota);
        when(mascotaRepository.findAll()).thenReturn(mascotas);

        // When
        List<Mascota> result = service.findAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testMascota.getNumChip(), result.get(0).getNumChip());
        verify(mascotaRepository).findAll();
        
        System.out.println("✅ Test PASADO: Encontradas " + result.size() + " mascota(s)");
    }

    // ========== Tests para findMascotasByTipo() ==========

    @Test
    @DisplayName("findMascotasByTipo() - Busca por tipo con JPQL")
    void findMascotasByTipo_Success() {
        // Given
        TypedQuery<Mascota> query = mock(TypedQuery.class);
        List<Mascota> mascotas = Arrays.asList(testMascota);
        when(entityManager.createQuery(anyString(), eq(Mascota.class))).thenReturn(query);
        when(query.setParameter(anyString(), anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(mascotas);

        // When
        List<Mascota> result = service.findMascotasByTipo("Perro");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(entityManager).createQuery(anyString(), eq(Mascota.class));
        verify(query).setParameter("tipo", "Perro");
        verify(query).getResultList();
        
        System.out.println("✅ Test PASADO: Encontradas " + result.size() + " mascota(s) de tipo 'Perro'");
    }

    @Test
    @DisplayName("findMascotasByTipo() - Retorna lista vacía si no hay resultados")
    void findMascotasByTipo_EmptyResult() {
        // Given
        TypedQuery<Mascota> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Mascota.class))).thenReturn(query);
        when(query.setParameter(anyString(), anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList());

        // When
        List<Mascota> result = service.findMascotasByTipo("Dinosaurio");

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(query).getResultList();
        
        System.out.println("✅ Test PASADO: No se encontraron mascotas de tipo 'Dinosaurio' - lista vacía correcta");
    }

    // ========== Tests para searchMascotas() ==========

    @Test
    @DisplayName("searchMascotas() - Búsqueda por nombre")
    void searchMascotas_ByNombre() {
        // Given
        queryDto.setNombre("Max");
        TypedQuery<Mascota> query = mock(TypedQuery.class);
        List<Mascota> mascotas = Arrays.asList(testMascota);
        when(entityManager.createQuery(anyString(), eq(Mascota.class))).thenReturn(query);
        when(query.setParameter(anyString(), anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(mascotas);

        // When
        List<Mascota> result = service.searchMascotas(queryDto);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(entityManager).createQuery(anyString(), eq(Mascota.class));
        verify(query).setParameter("nombre", "Max");
        verify(query).getResultList();

        System.out.println("✅ Test PASADO: Búsqueda dinámica por nombre 'Max- " + result.size() + " resultado(s)");
    }

    @Test
    @DisplayName("searchMascotas() - Búsqueda por tipo")
    void searchMascotas_ByTipo() {
        // Given
        queryDto.setTipoMascota("Perro");
        TypedQuery<Mascota> query = mock(TypedQuery.class);
        List<Mascota> mascotas = Arrays.asList(testMascota);
        when(entityManager.createQuery(anyString(), eq(Mascota.class))).thenReturn(query);
        when(query.setParameter(anyString(), anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(mascotas);

        // When
        List<Mascota> result = service.searchMascotas(queryDto);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(entityManager).createQuery(anyString(), eq(Mascota.class));
        verify(query).setParameter("tipo", "Perro");
        verify(query).getResultList();
        
        System.out.println("✅ Test PASADO: Búsqueda dinámica por tipo 'Perro' - " + result.size() + " resultado(s)");
    }

    @Test
    @DisplayName("searchMascotas() - Búsqueda por sexo")
    void searchMascotas_BySexo() {
        // Given
        queryDto.setSexo("Macho");
        TypedQuery<Mascota> query = mock(TypedQuery.class);
        List<Mascota> mascotas = Arrays.asList(testMascota);
        when(entityManager.createQuery(anyString(), eq(Mascota.class))).thenReturn(query);
        when(query.setParameter(anyString(), anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(mascotas);

        // When
        List<Mascota> result = service.searchMascotas(queryDto);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(entityManager).createQuery(anyString(), eq(Mascota.class));
        verify(query).setParameter("sexo", "Macho");
        verify(query).getResultList();
        
        System.out.println("✅ Test PASADO: Búsqueda dinámica por sexo 'Macho' - " + result.size() + " resultado(s)");
    }

    @Test
    @DisplayName("searchMascotas() - Búsqueda combinada nombre, tipo y sexo")
    void searchMascotas_Combined() {
        // Given
        queryDto.setNombre("Max");
        queryDto.setTipoMascota("Perro");
        queryDto.setSexo("Macho");
        TypedQuery<Mascota> query = mock(TypedQuery.class);
        List<Mascota> mascotas = Arrays.asList(testMascota);
        when(entityManager.createQuery(anyString(), eq(Mascota.class))).thenReturn(query);
        when(query.setParameter(anyString(), anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(mascotas);

        // When
        List<Mascota> result = service.searchMascotas(queryDto);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(entityManager).createQuery(anyString(), eq(Mascota.class));
        verify(query, times(3)).setParameter(anyString(), anyString());
        verify(query).getResultList();
        
        System.out.println("✅ Test PASADO: Búsqueda combinada (nombre='Max', tipo='Perro' Y sexo='Macho') - " + result.size() + " resultado(s)");
    }

    // ========== Tests para executeCountByTipo() ==========

    @Test
    @DisplayName("executeCountByTipo() - Cuenta mascotas por tipo")
    void executeCountByTipo_Success() {
        // Given
        TypedQuery<Long> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(query);
        when(query.setParameter(anyString(), anyString())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(5L);

        // When
        long result = service.executeCountByTipo("Perro");

        // Then
        assertEquals(5L, result);
        verify(entityManager).createQuery(anyString(), eq(Long.class));
        verify(query).setParameter("tipo", "Perro");
        verify(query).getSingleResult();
        
        System.out.println("✅ Test PASADO: COUNT de tipo 'Perro' = " + result);
    }

    @Test
    @DisplayName("executeCountByTipo() - Retorna 0 si no hay resultados")
    void executeCountByTipo_NoResults() {
        // Given
        TypedQuery<Long> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(query);
        when(query.setParameter(anyString(), anyString())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(0L);

        // When
        long result = service.executeCountByTipo("Dinosaurio");

        // Then
        assertEquals(0L, result);
        verify(entityManager).createQuery(anyString(), eq(Long.class));
        verify(query).setParameter("tipo", "Dinosaurio");
        verify(query).getSingleResult();
        
        System.out.println("✅ Test PASADO: COUNT de tipo 'Dinosaurio' = 0 (tipo inexistente)");
    }
}
