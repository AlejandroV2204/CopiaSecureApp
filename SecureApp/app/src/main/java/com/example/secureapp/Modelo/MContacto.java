package com.example.secureapp.Modelo;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.io.Serializable;

public class MContacto implements Serializable{

    private String nombre, apellido, email, telefono, tokenAlerta;
    Drawable img_contacto;

    public MContacto(String nombre, String apellido, String email, String telefono, String tokenAlerta) {

        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.tokenAlerta = tokenAlerta;
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

    public Drawable getImg_contacto() {
        return img_contacto;
    }

    public void setImg_contacto(Drawable img_contacto) {
        this.img_contacto = img_contacto;
    }
}


