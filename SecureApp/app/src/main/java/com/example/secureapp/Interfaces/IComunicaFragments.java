package com.example.secureapp.Interfaces;

import com.example.secureapp.Entidades.Contacto;
import com.example.secureapp.Entidades.Grupo;
import com.example.secureapp.Modelo.MContacto;
import com.example.secureapp.Modelo.MGrupo;

public interface IComunicaFragments {

    public void enviarGrupo(MGrupo grupo);

    public void enviarContacto(MContacto contacto);
}
