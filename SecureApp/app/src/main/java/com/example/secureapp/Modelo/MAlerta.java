package com.example.secureapp.Modelo;

import java.util.HashMap;

public class MAlerta {

    private String codigo, descripción,nombre;
    private HashMap localizacion;
    private boolean favorita, switchAlerta;

    public MAlerta(String descripción, boolean favorita) {

        this.codigo = codigo;
        this.nombre = nombre;
        this.descripción = descripción;
        this.localizacion = localizacion;
        this.favorita = favorita;

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

    public boolean getFavorita() {
        return favorita;
    }

    public void setFavorita(boolean favorita) {
        this.favorita = favorita;
    }

    public boolean getSwitchAlerta() {
        return switchAlerta;
    }

    public boolean setSwitchAlerta(boolean switchAlerta) {
        this.switchAlerta = switchAlerta;
        return switchAlerta;
    }
}

