-- =====================================================
-- DATA.SQL - Proyecto RA3 Pawner
-- Datos de prueba para desarrollo y testing
-- =====================================================

-- Insertar mascotas de prueba
INSERT INTO mascotas (num_chip, nombre, tipo_mascota, edad, sexo, otros_detalles) VALUES
(1001, 'Max', 'Perro', 5, 'Macho', 'Golden Retriever, castrado, vacunas al día'),
(1002, 'Luna', 'Gato', 3, 'Hembra', 'Siamés, todas las vacunas, esterilizada'),
(1003, 'Copito', 'Conejo', 2, 'Macho', 'Conejo enano, muy juguetón'),
(1004, 'Rex', 'Perro', 7, 'Macho', NULL),
(1005, 'Michi', 'Gato', 4, 'Hembra', 'Gato persa, pelaje largo, necesita cepillado'),
(1006, 'Bella', 'Perro', 2, 'Hembra', NULL),
(1007, 'Pelusa', 'Conejo', 1, 'Hembra', 'Conejo belier, orejas caídas'),
(1008, 'Rocky', 'Perro', 6, 'Macho', 'Bulldog francés, problemas respiratorios leves'),
(1009, 'Nieve', 'Gato', 5, 'Hembra', 'Gato angora blanco, ojos azules'),
(1010, 'Toby', 'Perro', 3, 'Macho', 'Beagle, muy activo, necesita ejercicio diario'),
(1011, 'Canela', 'Conejo', 2, 'Hembra', 'Conejo marrón, dócil y tranquilo'),
(1012, 'Zeus', 'Perro', 8, 'Macho', 'Rottweiler, buen guardián, entrenado'),
(1013, 'Mia', 'Gato', 2, 'Hembra', NULL),
(1014, 'Bruno', 'Perro', 4, 'Macho', 'Boxer, enérgico, le encanta jugar'),
(1015, 'Coco', 'Conejo', 3, 'Macho', 'Conejo gigante de Flandes, muy grande');

-- Verificación de datos insertados
-- SELECT COUNT(*) as total_mascotas FROM mascotas;
-- SELECT tipo_mascota, COUNT(*) as cantidad FROM mascotas GROUP BY tipo_mascota;
