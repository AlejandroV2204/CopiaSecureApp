package com.example.secureapp.Interfaces;

import com.example.secureapp.Entidades.Contacto;
import com.example.secureapp.Entidades.Grupo;

public interface IComunicaFragments {

    public void enviarGrupo(Grupo grupo);

    public void enviarContacto(Contacto contacto);
}
