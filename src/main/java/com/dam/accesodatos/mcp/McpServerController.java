package com.dam.accesodatos.mcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.dam.accesodatos.ra3.HibernateMascotaService;
import com.dam.accesodatos.model.Mascota;
import com.dam.accesodatos.model.MascotaCreateDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/mcp")
@CrossOrigin(origins = "*")
public class McpServerController {

    private static final Logger logger = LoggerFactory.getLogger(McpServerController.class);

    @Autowired
    private HibernateMascotaService hibernateMascotaService;

    @Autowired
    private McpToolRegistry toolRegistry;

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> getHealth() {
        Map<String, String> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "MCP Server RA3 Hibernate/JPA - Mascotas");

        return ResponseEntity.ok(health);
    }

    @GetMapping("/tools")
    public ResponseEntity<Map<String, Object>> getTools() {
        logger.debug("Solicitadas herramientas MCP Hibernate/JPA disponibles");

        List<McpToolRegistry.McpToolInfo> tools = toolRegistry.getRegisteredTools();

        List<Map<String, String>> toolsList = tools.stream()
                .map(tool -> {
                    Map<String, String> toolMap = new HashMap<>();
                    toolMap.put("name", tool.getName());
                    toolMap.put("description", tool.getDescription());
                    return toolMap;
                })
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("tools", toolsList);
        response.put("count", toolsList.size());
        response.put("server", "MCP Server - RA3 Hibernate/JPA DAM");
        response.put("version", "1.0.0");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/test_entity_manager")
    public ResponseEntity<Map<String, Object>> testEntityManager() {
        logger.debug("Probando EntityManager");

        try {
            String result = hibernateMascotaService.testEntityManager();

            Map<String, Object> response = new HashMap<>();
            response.put("tool", "test_entity_manager");
            response.put("result", result);
            response.put("status", "success");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error probando EntityManager", e);

            Map<String, Object> error = new HashMap<>();
            error.put("error", "Error probando EntityManager: " + e.getMessage());
            error.put("tool", "test_entity_manager");
            error.put("status", "error");

            return ResponseEntity.status(500).body(error);
        }
    }

    @PostMapping("/create_mascota")
    public ResponseEntity<Map<String, Object>> createMascota(@RequestBody MascotaCreateDto dto) {
        logger.debug("Creando mascota con Hibernate");

        try {
            Mascota mascota = hibernateMascotaService.createMascota(dto);

            Map<String, Object> response = new HashMap<>();
            response.put("tool", "create_mascota");
            response.put("result", mascota);
            response.put("status", "success");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error creando mascota", e);

            Map<String, Object> error = new HashMap<>();
            error.put("error", "Error creando mascota: " + e.getMessage());
            error.put("tool", "create_mascota");
            error.put("status", "error");

            return ResponseEntity.status(500).body(error);
        }
    }

    @PostMapping("/find_mascota_by_id")
    public ResponseEntity<Map<String, Object>> findMascotaById(@RequestBody Map<String, Object> request) {
        logger.debug("Buscando mascota por ID");

        try {
            Integer mascotaId = ((Number) request.get("mascotaId")).intValue();
            Mascota mascota = hibernateMascotaService.findMascotaById(mascotaId);

            Map<String, Object> response = new HashMap<>();
            response.put("tool", "find_mascota_by_id");
            response.put("result", mascota);
            response.put("status", "success");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error buscando mascota", e);

            Map<String, Object> error = new HashMap<>();
            error.put("error", "Error buscando mascota: " + e.getMessage());
            error.put("tool", "find_mascota_by_id");
            error.put("status", "error");

            return ResponseEntity.status(500).body(error);
        }
    }

    @PostMapping("/find_all_mascotas")
    public ResponseEntity<Map<String, Object>> findAllMascotas() {
        logger.debug("Obteniendo todas las mascotas");

        try {
            List<Mascota> mascotas = hibernateMascotaService.findAll();

            Map<String, Object> response = new HashMap<>();
            response.put("tool", "find_all_mascotas");
            response.put("result", mascotas);
            response.put("count", mascotas.size());
            response.put("status", "success");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error obteniendo mascotas", e);

            Map<String, Object> error = new HashMap<>();
            error.put("error", "Error obteniendo mascotas: " + e.getMessage());
            error.put("tool", "find_all_mascotas");
            error.put("status", "error");

            return ResponseEntity.status(500).body(error);
        }
    }

    @PostMapping("/find_mascotas_by_tipo")
    public ResponseEntity<Map<String, Object>> findMascotasByTipo(@RequestBody Map<String, String> request) {
        logger.debug("Buscando mascotas por tipo");

        try {
            String tipo = request.get("tipo");
            List<Mascota> mascotas = hibernateMascotaService.findMascotasByTipo(tipo);

            Map<String, Object> response = new HashMap<>();
            response.put("tool", "find_mascotas_by_tipo");
            response.put("result", mascotas);
            response.put("count", mascotas.size());
            response.put("status", "success");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error buscando mascotas por tipo", e);

            Map<String, Object> error = new HashMap<>();
            error.put("error", "Error buscando mascotas por tipo: " + e.getMessage());
            error.put("tool", "find_mascotas_by_tipo");
            error.put("status", "error");

            return ResponseEntity.status(500).body(error);
        }
    }
}
