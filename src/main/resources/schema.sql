-- =====================================================
-- SCHEMA.SQL - Proyecto RA3 Pawner
-- Base de datos H2 embebida para desarrollo
-- =====================================================

-- Eliminar tabla si existe (para desarrollo limpio)
DROP TABLE IF EXISTS mascotas;

-- Crear tabla mascotas
CREATE TABLE mascotas (
    num_chip INT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    tipo_mascota VARCHAR(50) NOT NULL,
    edad INT NOT NULL,
    sexo ENUM('macho', 'hembra') NOT NULL,
    otros_detalles VARCHAR(255)
);

-- Índices para mejorar rendimiento en búsquedas frecuentes
CREATE INDEX idx_tipo_mascota ON mascotas(tipo_mascota);
CREATE INDEX idx_sexo ON mascotas(sexo);

-- Comentarios de tabla
COMMENT ON TABLE mascotas IS 'Tabla de mascotas para el sistema Pawner';
COMMENT ON COLUMN mascotas.num_chip IS 'Número de chip identificador único';
COMMENT ON COLUMN mascotas.nombre IS 'Nombre de la mascota';
COMMENT ON COLUMN mascotas.tipo_mascota IS 'Tipo de mascota (Perro, Gato, Conejo, etc.)';
COMMENT ON COLUMN mascotas.edad IS 'Edad de la mascota en años';
COMMENT ON COLUMN mascotas.sexo IS 'Sexo de la mascota (Macho/Hembra)';
COMMENT ON COLUMN mascotas.otros_detalles IS 'Detalles adicionales (raza, vacunas, observaciones, etc.)';
