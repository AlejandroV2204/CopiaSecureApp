package com.example.secureapp.Entidades;

import java.io.Serializable;

public class Contacto implements Serializable {

    private String nombre;
    private String telefono;
    private int imagenid;

    public Contacto(String nombre, String telefono, int imagenid) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.imagenid = imagenid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getImagenid() {
        return imagenid;
    }

    public void setImagenid(int imagenid) {
        this.imagenid = imagenid;
    }
}
