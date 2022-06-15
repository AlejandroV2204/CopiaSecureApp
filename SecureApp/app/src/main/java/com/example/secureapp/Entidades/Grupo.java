package com.example.secureapp.Entidades;

import java.io.Serializable;

public class Grupo implements Serializable {

    private String nombre;
    private String descripcion;
    private int imagenid;

    public Grupo(String nombre, String descripcion, int imagenid) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagenid = imagenid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getImagenid() {
        return imagenid;
    }

    public void setImagenid(int imagenid) {
        this.imagenid = imagenid;
    }
}
