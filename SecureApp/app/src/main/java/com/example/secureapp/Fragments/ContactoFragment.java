package com.example.secureapp.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secureapp.Adaptadores.AdapterContacto;
import com.example.secureapp.Adaptadores.AdapterGrupo;
import com.example.secureapp.Entidades.Contacto;
import com.example.secureapp.Entidades.Grupo;
import com.example.secureapp.Interfaces.IComunicaFragments;
import com.example.secureapp.R;

import java.util.ArrayList;

public class ContactoFragment extends Fragment{

    AdapterContacto adapterContacto;
    RecyclerView recyclerViewContactos;
    ArrayList<Contacto> listaContactos;

    //referencias para comunicar fragments
    Activity actividad;
    IComunicaFragments iComunicaFragments;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contacto,container, false);

        recyclerViewContactos = view.findViewById(R.id.RV_contactos);
        listaContactos = new ArrayList<>();
        //cargar lista
        cargarLista();
        //mostrar datos
        mostrarDatos();

        return view;

    }

    public void cargarLista(){

        listaContactos.add(new Contacto("Ramiro","3153442635",R.drawable.ic_launcher_foreground));
        listaContactos.add(new Contacto("Fausto","315992736",R.drawable.ic_launcher_nuevo_grupo_foreground));

    }

    public void mostrarDatos(){

        recyclerViewContactos.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterContacto = new AdapterContacto(getContext(), listaContactos);
        recyclerViewContactos.setAdapter(adapterContacto);

        adapterContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = listaContactos.get(recyclerViewContactos.getChildAdapterPosition(view)).getNombre();
                Toast.makeText(getContext(), "Selecciono: " + nombre, Toast.LENGTH_SHORT).show();

                iComunicaFragments.enviarContacto(listaContactos.get(recyclerViewContactos.getChildAdapterPosition(view)));
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof Activity){

            this.actividad = (Activity) context;
                iComunicaFragments = (IComunicaFragments) this.actividad;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
