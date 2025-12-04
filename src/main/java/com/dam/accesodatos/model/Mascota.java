package com.dam.accesodatos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.Objects;

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
    private String otrosDetalles;

    public Mascota() {
    }

    public Mascota(int numChip, String nombre, String tipoMascota, int edad, String sexo, String otrosDetalles) {
        this.numChip = numChip;
        this.nombre = nombre;
        this.tipoMascota = tipoMascota;
        this.edad = edad;
        this.sexo = sexo;
        this.otrosDetalles = otrosDetalles;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mascota mascota = (Mascota) o;
        return numChip == mascota.numChip;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numChip);
    }

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

