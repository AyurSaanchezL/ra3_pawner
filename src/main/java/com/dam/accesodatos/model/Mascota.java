package com.dam.accesodatos.model;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Entidad JPA Mascota para RA3 - Hibernate/JPA ORM
 *
 * RA3 - CRITERIO c) Definición de ficheros de mapeo:
 * Esta clase usa anotaciones JPA para mapear el objeto Java a la tabla
 * 'mascota' de la BD.
 *
 * DIFERENCIAS vs RA2 (JDBC):
 * - RA2: POJO simple sin anotaciones, mapeo manual con ResultSet.getLong(),
 * getString(), etc.
 * - RA3: Clase anotada con @Entity, @Table, @Column - Hibernate mapea
 * automáticamente
 *
 * ANOTACIONES JPA UTILIZADAS:
 * - @Entity: Marca la clase como entidad JPA gestionada por Hibernate
 * - @Table: Mapea explícitamente a la tabla 'mascotas' de la BD
 * - @Id: Marca el campo 'num_chip' como clave primaria
 * - @GeneratedValue: Si el numChip fuese autogenerado por la BD (IDENTITY
 * strategy)
 * - @Column: Mapeo explícito de campos a columnas con restricciones
 * - @NotBlank, @Nombre: Validaciones de Bean Validation
 *
 * NOTA PEDAGÓGICA:
 * El constructor sin argumentos es OBLIGATORIO para JPA. Hibernate lo usa
 * para crear instancias mediante reflection al recuperar datos de la BD.
 */
@Entity
@Table(name = "mascotas")
public class Mascota {

    @Id
    @Column(name = "num_chip")
    private int numChip;

    @Column(name = "nombre", nullable = false, length = 50)
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String nombre;

    @Column(name = "tipo_mascota", nullable = false, length = 50)
    @NotBlank(message = "El tipo de mascota es obligatorio")
    private String tipoMascota;

    @Column(name = "edad")
    private int edad;

    @Column(name = "sexo", length = 50)
    private String sexo;

    @Column(name = "otros_detalles", length = 255)
    private String otrosDetalles; // Para datos como: castrado s/n, alergias, preferencias, etc

    // ===== CONSTRUCTOR SIN ARGUMENTOS (OBLIGATORIO PARA JPA) =====

    /**
     * Constructor sin argumentos requerido por JPA.
     * Hibernate lo usa para instanciar objetos al recuperar datos de la BD.
     */
    public Mascota() {
    }

    // ===== CONSTRUCTOR CON ARGUMENTOS (OPCIONAL) =====

    public Mascota(int numChip, String nombre, String tipoMascota, int edad, String sexo, String otrosDetalles) {
        this.numChip = numChip;
        this.nombre = nombre;
        this.tipoMascota = tipoMascota;
        this.edad = edad;
        this.sexo = sexo;
        this.otrosDetalles = otrosDetalles;
    }

    // ===== GETTERS Y SETTERS =====

    public int getNumChip() {
        return numChip;
    }

    public void setNumChip(int numChip) {
        this.numChip = numChip;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoMascota() {
        return tipoMascota;
    }

    public void setTipoMascota(String tipoMascota) {
        this.tipoMascota = tipoMascota;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getOtrosDetalles() {
        return otrosDetalles;
    }

    public void setOtrosDetalles(String otrosDetalles) {
        this.otrosDetalles = otrosDetalles;
    }

    // ===== EQUALS() Y HASHCODE() =====

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Mascota mascota = (Mascota) o;
        return numChip == mascota.numChip;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numChip);
    }

    // ===== TOSTRING() =====

    @Override
    public String toString() {
        return "Mascota{" +
                "numChip=" + numChip +
                ", nombre='" + nombre + '\'' +
                ", tipoMascota='" + tipoMascota + '\'' +
                ", edad=" + edad +
                ", sexo='" + sexo + '\'' +
                ", otrosDetalles='" + otrosDetalles + '\'' +
                '}';
    }
}
