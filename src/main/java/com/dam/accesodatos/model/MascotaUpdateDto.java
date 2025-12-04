package com.dam.accesodatos.model;

import jakarta.validation.constraints.*;

public class MascotaUpdateDto {

    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String nombre;

    private String tipoMascota;

    private Integer edad;

    private String sexo;

    private String otrosDetalles;

    public MascotaUpdateDto() {}

    public MascotaUpdateDto(String nombre, String tipoMascota, Integer edad, String sexo, String otrosDetalles) {
        this.nombre = nombre;
        this.tipoMascota = tipoMascota;
        this.edad = edad;
        this.sexo = sexo;
        this.otrosDetalles = otrosDetalles;
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

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
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

    public void applyTo(Mascota mascota) {
        if (this.nombre != null) mascota.setNombre(this.nombre);
        if (this.tipoMascota != null) mascota.setTipoMascota(this.tipoMascota);
        if (this.edad != null) mascota.setEdad(this.edad);
        if (this.sexo != null) mascota.setSexo(this.sexo);
        if (this.otrosDetalles != null) mascota.setOtrosDetalles(this.otrosDetalles);
    }

    @Override
    public String toString() {
        return "MascotaUpdateDto{" +
                "nombre='" + nombre + '\'' +
                ", tipoMascota='" + tipoMascota + '\'' +
                ", edad=" + edad +
                ", sexo='" + sexo + '\'' +
                ", otrosDetalles='" + otrosDetalles + '\'' +
                '}';
    }
}
