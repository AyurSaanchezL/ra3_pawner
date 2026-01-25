package com.dam.accesodatos.ra3;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.dam.accesodatos.model.Mascota;
import com.dam.accesodatos.model.MascotaCreateDto;
import com.dam.accesodatos.model.MascotaQueryDto;
import com.dam.accesodatos.model.MascotaUpdateDto;
import com.dam.accesodatos.repository.MascotaRepository;

/**
 * Tests de integración para los métodos IMPLEMENTADOS de
 * HibernateMascotaServiceImpl
 *
 * @SpringBootTest carga el contexto completo de Spring con base de datos H2
 *                 real.
 *                 Estos tests validan que los métodos implementados funcionan
 *                 correctamente end-to-end.
 *
 *                 COBERTURA: Tests que validan los métodos implementados:
 *                 1. testEntityManager() - 1 test
 *                 2. createMascota() + findMascotaByNumChip() + updateMascota()
 *                 + deleteMascota() - 1 test de flujo completo
 *                 3. findAll() - 1 test
 *                 4. findMascotasByTipo() - 2 tests
 *                 5. searchMascotas() - 4 tests
 *                 6. executeCountByTipo() - 2 tests
 *                 7. transferData() - 2 tests (transacción y rollback)
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("Tests Integración - Métodos Implementados")
class HibernateMascotaServiceIntegrationTest {

    @Autowired
    private HibernateMascotaService service;

    @Autowired
    private MascotaRepository mascotaRepository;

    @BeforeEach
    void setUp() {
        // Limpiar BD antes de cada test
        mascotaRepository.deleteAll();
    }

    // ========== Tests de conexión y configuración ==========

    @Test
    @DisplayName("testEntityManager() - Conexión real funciona")
    void testEntityManager_RealConnection_Success() {
        // When
        String result = service.testEntityManager();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("EntityManager activo"));
        assertTrue(result.length() > 20, "El resultado debe contener información de la BD");

        System.out.println("✅ Test PASADO: " + result);
    }

    // ========== Tests de flujo CRUD completo ==========

    @Test
    @DisplayName("Flujo CRUD completo - Create, Read, Update, Delete")
    void crudFlow_CompleteLifecycle_Success() {
        System.out.println("\n🔄 Iniciando flujo CRUD completo...");

        // 1. CREATE - Crear mascota con createMascota()
        MascotaCreateDto createDto = new MascotaCreateDto();
        createDto.setNumChip(1001);
        createDto.setNombre("Max");
        createDto.setTipoMascota("Perro");
        createDto.setEdad(5);
        createDto.setSexo("Macho");
        createDto.setOtrosDetalles("Golden Retriever, castrado");

        Mascota created = service.createMascota(createDto);
        assertNotNull(created.getNumChip());
        assertEquals("Max", created.getNombre());
        assertEquals("Perro", created.getTipoMascota());
        System.out.println(
                "   ✓ CREATE: Mascota creada - " + created.getNombre() + " (ID: " + created.getNumChip() + ")");

        // 2. READ - Buscar mascota con findMascotaByNumChip()
        Mascota found = service.findMascotaByNumChip(created.getNumChip());
        assertNotNull(found);
        assertEquals(created.getNumChip(), found.getNumChip());
        assertEquals("Max", found.getNombre());
        System.out.println("   ✓ READ: Mascota encontrada - " + found.getNombre());

        // 3. UPDATE - Actualizar mascota con updateMascota()
        MascotaUpdateDto updateDto = new MascotaUpdateDto();
        updateDto.setNombre("Maximus");
        updateDto.setEdad(6);

        Mascota updated = service.updateMascota(created.getNumChip(), updateDto);
        assertEquals("Maximus", updated.getNombre());
        assertEquals(6, updated.getEdad());
        assertEquals(created.getNumChip(), updated.getNumChip());
        System.out.println("   ✓ UPDATE: Mascota actualizada - Nuevo nombre: " + updated.getNombre() + ", Nueva edad: "
                + updated.getEdad());

        // 4. VERIFY - Verificar que los cambios persisten
        Mascota verified = service.findMascotaByNumChip(created.getNumChip());
        assertEquals("Maximus", verified.getNombre());
        assertEquals(6, verified.getEdad());
        System.out.println("   ✓ VERIFY: Cambios persistidos correctamente");

        // 5. DELETE - Eliminar mascota con deleteMascota()
        boolean deleted = service.deleteMascota(created.getNumChip());
        assertTrue(deleted);
        System.out.println("   ✓ DELETE: Mascota eliminada (ID: " + created.getNumChip() + ")");

        // 6. VERIFY DELETE - Verificar que la mascota fue eliminada
        Mascota notFound = service.findMascotaByNumChip(created.getNumChip());
        assertNull(notFound);
        System.out.println("   ✓ VERIFY DELETE: Confirmado que la mascota ya no existe");

        System.out.println("✅ Test PASADO: Flujo CRUD completo exitoso\n");
    }

    // ========== Tests de findAll() ==========

    @Test
    @DisplayName("findAll() - Retorna todas las mascotas creadas")
    void findAll_ReturnsAllMascotas() {
        // Given - Crear varias mascotas
        createTestMascota(1001, "Max", "Perro");
        createTestMascota(1002, "Luna", "Gato");
        createTestMascota(1003, "Copito", "Conejo");

        // When
        List<Mascota> allMascotas = service.findAll();

        // Then
        assertNotNull(allMascotas);
        assertEquals(3, allMascotas.size());

        System.out.println("✅ Test PASADO: Encontradas " + allMascotas.size() + " mascotas en total");
    }

    // ========== Tests de findMascotasByTipo() ==========

    @Test
    @DisplayName("findMascotasByTipo() - Filtra por tipo correctamente")
    void findMascotasByTipo_WithRealData_Success() {
        // Given - Crear mascotas de diferentes tipos
        createTestMascota(1001, "Max", "Perro");
        createTestMascota(1002, "Rex", "Perro");
        createTestMascota(1003, "Luna", "Gato");
        createTestMascota(1004, "Michi", "Gato");
        createTestMascota(1005, "Copito", "Conejo");

        // When - Buscar solo Perros
        List<Mascota> perros = service.findMascotasByTipo("Perro");

        // Then
        assertNotNull(perros);
        assertEquals(2, perros.size());
        assertTrue(perros.stream().allMatch(m -> "Perro".equals(m.getTipoMascota())));
        assertTrue(perros.stream().anyMatch(m -> "Max".equals(m.getNombre())));
        assertTrue(perros.stream().anyMatch(m -> "Rex".equals(m.getNombre())));

        System.out.println("✅ Test PASADO: Encontrados " + perros.size() + " perros (Max, Rex) de 5 mascotas totales");
    }

    @Test
    @DisplayName("findMascotasByTipo() - Retorna lista vacía si no hay match")
    void findMascotasByTipo_NoMatch_EmptyList() {
        // Given
        createTestMascota(1001, "Max", "Perro");

        // When
        List<Mascota> result = service.findMascotasByTipo("Dinosaurio");

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        System.out.println("✅ Test PASADO: No se encontraron 'Dinosaurio' - lista vacía correcta");
    }

    // ========== Tests de searchMascotas() ==========

    @Test
    @DisplayName("searchMascotas() - Búsqueda por nombre")
    void searchMascotas_ByNombre_Success() {
        // Given
        createTestMascota(1001, "Guillermo", "Cabra", "Macho");
        createTestMascota(1002, "Mariana", "Oveja", "Hembra");

        MascotaQueryDto query = new MascotaQueryDto();
        query.setNombre("Guillermo");

        // When
        List<Mascota> result = service.searchMascotas(query);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Cabra", result.get(0).getTipoMascota());

        System.out.println(
                "✅ Test PASADO: Búsqueda dinámica por nombre 'Guillermo' - encontrado: "
                        + result.get(0).getTipoMascota());
    }

    @Test
    @DisplayName("searchMascotas() - Búsqueda por tipo")
    void searchMascotas_ByTipo_Success() {
        // Given
        createTestMascota(1001, "Max", "Perro", "Macho");
        createTestMascota(1002, "Luna", "Gato", "Hembra");

        MascotaQueryDto query = new MascotaQueryDto();
        query.setTipoMascota("Perro");

        // When
        List<Mascota> result = service.searchMascotas(query);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Max", result.get(0).getNombre());

        System.out.println(
                "✅ Test PASADO: Búsqueda dinámica por tipo 'Perro' - encontrado: " + result.get(0).getNombre());
    }

    @Test
    @DisplayName("searchMascotas() - Búsqueda por sexo")
    void searchMascotas_BySexo_Success() {
        // Given
        createTestMascota(1001, "Max", "Perro", "Macho");
        createTestMascota(1002, "Luna", "Gato", "Hembra");
        createTestMascota(1003, "Rex", "Perro", "Macho");

        MascotaQueryDto query = new MascotaQueryDto();
        query.setSexo("Macho");

        // When
        List<Mascota> result = service.searchMascotas(query);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(m -> "Macho".equals(m.getSexo())));

        System.out.println(
                "✅ Test PASADO: Búsqueda dinámica por sexo 'Macho' - encontrados: " + result.size() + " (Max, Rex)");
    }

    @Test
    @DisplayName("searchMascotas() - Búsqueda combinada tipo, sexo y nombre")
    void searchMascotas_Combined_Success() {
        // Given
        createTestMascota(1000, "Max", "Perro", "Macho");
        createTestMascota(1001, "Max", "Perro", "Macho");
        createTestMascota(1002, "Luna", "Gato", "Hembra");
        createTestMascota(1003, "Bella", "Perro", "Hembra");

        MascotaQueryDto query = new MascotaQueryDto();
        query.setNombre("Max");
        query.setTipoMascota("Perro");
        query.setSexo("Macho");

        // When
        List<Mascota> result = service.searchMascotas(query);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Max", result.get(0).getNombre());
        assertEquals("Max", result.get(1).getNombre());

        System.out
                .println("✅ Test PASADO: Búsqueda combinada (nombre='Max', tipo='Perro' Y sexo='Macho') - resultados: "
                        + result.size() + " - encontrados: ");

        for (int i = 0; i < result.size(); i++) {
            System.out.println(result.get(i).toString());
        }
    }

    // ========== Tests de executeCountByTipo() ==========

    @Test
    @DisplayName("executeCountByTipo() - Cuenta mascotas por tipo")
    void executeCountByTipo_Success() {
        // Given
        createTestMascota(1001, "Max", "Perro");
        createTestMascota(1002, "Rex", "Perro");
        createTestMascota(1003, "Luna", "Gato");

        // When
        long count = service.executeCountByTipo("Perro");

        // Then
        assertEquals(2, count);

        System.out.println("✅ Test PASADO: COUNT de tipo 'Perro' = " + count);
    }

    @Test
    @DisplayName("executeCountByTipo() - Retorna 0 si no hay resultados")
    void executeCountByTipo_NoResults() {
        // Given
        createTestMascota(1001, "Max", "Perro");

        // When
        long count = service.executeCountByTipo("Dinosaurio");

        // Then
        assertEquals(0, count);

        System.out.println("✅ Test PASADO: COUNT de tipo 'Dinosaurio' = 0 (tipo inexistente)");
    }

    // ========== Tests de transferData() (transacciones) ==========

    @Test
    @DisplayName("transferData() - Transacción múltiple exitosa")
    void transferData_Success() {
        // Given - Crear mascotas
        Mascota mascota1 = new Mascota();
        mascota1.setNumChip(1001);
        mascota1.setNombre("Max");
        mascota1.setTipoMascota("Perro");
        mascota1.setEdad(5);
        mascota1.setSexo("Macho");

        Mascota mascota2 = new Mascota();
        mascota2.setNumChip(1002);
        mascota2.setNombre("Luna");
        mascota2.setTipoMascota("Gato");
        mascota2.setEdad(3);
        mascota2.setSexo("Hembra");

        Mascota mascota3 = new Mascota();
        mascota3.setNumChip(1003);
        mascota3.setNombre("Copito");
        mascota3.setTipoMascota("Conejo");
        mascota3.setEdad(2);
        mascota3.setSexo("Macho");

        List<Mascota> mascotas = Arrays.asList(mascota1, mascota2, mascota3);

        // When
        boolean result = service.transferData(mascotas);

        // Then
        assertTrue(result);
        assertEquals(3, mascotaRepository.count());

        System.out
                .println("✅ Test PASADO: Transacción múltiple exitosa - 3 mascotas insertadas en una sola transacción");
    }

    @Test
    @DisplayName("transferData() - Rollback en caso de error")
    void transferData_Rollback() {
        // Given - Crear una mascota primero
        createTestMascota(1001, "Max", "Perro");

        // Intentar insertar un lote que incluye un duplicado
        Mascota duplicada = new Mascota();
        duplicada.setNumChip(1001); // Mismo ID - causará error
        duplicada.setNombre("Duplicado");
        duplicada.setTipoMascota("Perro");
        duplicada.setEdad(5);
        duplicada.setSexo("Macho");

        Mascota nueva = new Mascota();
        nueva.setNumChip(1002);
        nueva.setNombre("Luna");
        nueva.setTipoMascota("Gato");
        nueva.setEdad(3);
        nueva.setSexo("Hembra");

        List<Mascota> mascotas = Arrays.asList(duplicada, nueva);

        // When & Then
        assertThrows(Exception.class, () -> service.transferData(mascotas));

        // Verificar que el rollback funcionó - solo debe haber 1 mascota (la original)
        assertEquals(1, mascotaRepository.count());

        System.out.println(
                "✅ Test PASADO: Rollback funcionó correctamente - BD mantiene solo 1 mascota tras error de duplicado");
    }

    // ========== Tests de casos límite ==========

    @Test
    @DisplayName("findMascotaByNumChip() - Retorna null para ID inexistente")
    void findMascotaByNumChip_NonExistent_ReturnsNull() {
        // When
        Mascota result = service.findMascotaByNumChip(9999);

        // Then
        assertNull(result);

        System.out.println("✅ Test PASADO: Búsqueda con ID inexistente (9999) retorna null correctamente");
    }

    @Test
    @DisplayName("updateMascota() - Falla con ID inexistente")
    void updateMascota_NonExistent_ThrowsException() {
        // Given
        MascotaUpdateDto updateDto = new MascotaUpdateDto();
        updateDto.setNombre("Nuevo Nombre");

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            service.updateMascota(9999, updateDto);
        });

        System.out.println("✅ Test PASADO: Update con ID inexistente (9999) lanza excepción correctamente");
    }

    @Test
    @DisplayName("deleteMascota() - Retorna false para ID inexistente")
    void deleteMascota_NonExistent_ReturnsFalse() {
        // When
        boolean result = service.deleteMascota(9999);

        // Then
        assertFalse(result);

        System.out.println("✅ Test PASADO: Delete con ID inexistente (9999) retorna false correctamente");
    }

    // ========== Métodos auxiliares ==========

    private Mascota createTestMascota(int numChip, String nombre, String tipo) {
        return createTestMascota(numChip, nombre, tipo, "Macho");
    }

    private Mascota createTestMascota(int numChip, String nombre, String tipo, String sexo) {
        MascotaCreateDto dto = new MascotaCreateDto();
        dto.setNumChip(numChip);
        dto.setNombre(nombre);
        dto.setTipoMascota(tipo);
        dto.setEdad(5);
        dto.setSexo(sexo);
        dto.setOtrosDetalles("Test mascota");

        return service.createMascota(dto);
    }
}
