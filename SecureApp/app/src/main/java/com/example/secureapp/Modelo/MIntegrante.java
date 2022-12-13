package com.example.secureapp.Modelo;

import java.io.Serializable;

public class MIntegrante implements Serializable {

    private String identificador, nombre, apellido, email, telefono, tokenAlerta;

    public MIntegrante(String identificador, String nombre, String apellido, String email, String telefono) {

        this.identificador = identificador;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTokenAlerta() {
        return tokenAlerta;
    }

    public void setTokenAlerta(String tokenAlerta) {
        this.tokenAlerta = tokenAlerta;
    }
}


