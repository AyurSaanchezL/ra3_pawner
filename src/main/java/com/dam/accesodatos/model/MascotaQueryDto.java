package com.dam.accesodatos.model;

public class MascotaQueryDto {

    private String nombreMascota;
    private String tipoMascota;
    private String sexo;
    private Integer limit;
    private Integer offset;

    public MascotaQueryDto() {
        this.limit = 10;
        this.offset = 0;
    }

    public MascotaQueryDto(String nombreMascota,String tipoMascota, String sexo, Integer limit, Integer offset) {
        this.nombreMascota = nombreMascota;
        this.tipoMascota = tipoMascota;
        this.sexo = sexo;
        this.limit = limit != null ? limit : 10;
        this.offset = offset != null ? offset : 0;
    }

    public String getNombreMascota() {
        return nombreMascota;
    }

    public void setNombreMascota(String nombreMascota) {
        this.nombreMascota = nombreMascota;
    }

    public String getTipoMascota() {
        return tipoMascota;
    }

    public void setTipoMascota(String tipoMascota) {
        this.tipoMascota = tipoMascota;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "MascotaQueryDto{" +
                "nombreMascota='" + nombreMascota + '\'' +        
                "tipoMascota='" + tipoMascota + '\'' +
                ", sexo='" + sexo + '\'' +
                ", limit=" + limit +
                ", offset=" + offset +
                '}';
    }
}
