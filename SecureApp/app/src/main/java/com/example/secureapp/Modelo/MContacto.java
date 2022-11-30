package com.example.secureapp.Modelo;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.io.Serializable;

public class MContacto implements Serializable{

    private String nombre, apellido, email, telefono, token;
    Drawable img_contacto;

    public MContacto(String nombre, String apellido, String email, String telefono, String token) {

        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.token = token;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Drawable getImg_contacto() {
        return img_contacto;
    }

    public void setImg_contacto(Drawable img_contacto) {
        this.img_contacto = img_contacto;
    }
}


