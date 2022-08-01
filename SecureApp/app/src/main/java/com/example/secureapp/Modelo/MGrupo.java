package com.example.secureapp.Modelo;

import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;

public class MGrupo implements Serializable{

    private String identificador, nombre, descripcion, administrador, emailAdministrador, fechaCreacion, cantidadIntegrantes;
    private GeoPoint localizacion;

    public MGrupo(String identificador, String nombre, String descripción, String administrador, String emailAdministrador, String fechaCreacion, String cantidadIntegrantes, GeoPoint localizacion) {

        this.identificador = identificador;
        this.nombre = nombre;
        this.descripcion = descripción;
        this.administrador = administrador;
        this.emailAdministrador = emailAdministrador;
        this.fechaCreacion = fechaCreacion;
        this.cantidadIntegrantes = cantidadIntegrantes;
        this.localizacion = localizacion;

    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
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

    public GeoPoint getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(GeoPoint localizacion) {
        this.localizacion = localizacion;
    }

}
