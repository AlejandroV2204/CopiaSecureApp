package com.example.secureapp.Modelo;

import android.location.Location;

import java.util.HashMap;

public class MGrupo {

    private String nombre, descripción, administrador, emailAdministrador, fechaCreacion, cantidadIntegrantes;
    private HashMap localizacion;

    public MGrupo(String nombre, String descripción, String administrador, String emailAdministrador, String fechaCreacion, String cantidadIntegrantes, HashMap localizacion) {

        this.nombre = nombre;
        this.descripción = descripción;
        this.administrador = administrador;
        this.emailAdministrador = emailAdministrador;
        this.fechaCreacion = fechaCreacion;
        this.cantidadIntegrantes = cantidadIntegrantes;
        this.localizacion = localizacion;

    }


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

    public String getAdministrador() {
        return administrador;
    }

    public void setAdministrador(String administrador) {
        this.administrador = administrador;
    }

    public String getEmailAdministrador() {
        return emailAdministrador;
    }

    public void setEmailAdministrador(String emailAdministrador) {
        this.emailAdministrador = emailAdministrador;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getCantidadIntegrantes() {
        return cantidadIntegrantes;
    }

    public void setCantidadIntegrantes(String cantidadIntegrantes) {
        this.cantidadIntegrantes = cantidadIntegrantes;
    }

    public HashMap getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(HashMap localizacion) {
        this.localizacion = localizacion;
    }

}
