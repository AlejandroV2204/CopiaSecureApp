package com.example.secureapp.Modelo;

import java.util.HashMap;

public class MMain {

    private String codigo, descripción,nombre;
    private HashMap localizacion;

    public MMain(String descripción) {

        this.codigo = codigo;
        this.nombre = nombre;
        this.descripción = descripción;
        this.localizacion = localizacion;

    }

    public String getCodigo() {return codigo;}

    public void setCodigo(String codigo) {this.codigo = codigo;}

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripción() {
        return descripción;
    }

    public void setDescripción(String descripción) {
        this.descripción = descripción;
    }

    public HashMap getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(HashMap localizacion) {
        this.localizacion = localizacion;
    }

}
