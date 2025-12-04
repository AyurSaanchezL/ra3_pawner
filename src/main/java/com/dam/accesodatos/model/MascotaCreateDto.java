package com.dam.accesodatos.model;

import jakarta.validation.constraints.*;

public class MascotaCreateDto {

    @NotNull(message = "El número de chip es obligatorio")
    private Integer numChip;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String nombre;

    @NotBlank(message = "El tipo de mascota es obligatorio")
    private String tipoMascota;

    private int edad;

    private String sexo;

    private String otrosDetalles;

    public MascotaCreateDto() {}

    public MascotaCreateDto(Integer numChip, String nombre, String tipoMascota, int edad, String sexo, String otrosDetalles) {
        this.numChip = numChip;
        this.nombre = nombre;
        this.tipoMascota = tipoMascota;
        this.edad = edad;
        this.sexo = sexo;
        this.otrosDetalles = otrosDetalles;
    }

    public Integer getNumChip() {
        return numChip;
    }

    public void setNumChip(Integer numChip) {
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

    public Mascota toMascota() {
        return new Mascota(this.numChip, this.nombre, this.tipoMascota, this.edad, this.sexo, this.otrosDetalles);
    }

    @Override
    public String toString() {
        return "MascotaCreateDto{" +
                "numChip=" + numChip +
                ", nombre='" + nombre + '\'' +
                ", tipoMascota='" + tipoMascota + '\'' +
                ", edad=" + edad +
                ", sexo='" + sexo + '\'' +
                ", otrosDetalles='" + otrosDetalles + '\'' +
                '}';
    }
}
