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

import com.example.secureapp.Adaptadores.AdapterGrupo;
import com.example.secureapp.Entidades.Grupo;
import com.example.secureapp.Interfaces.IComunicaFragments;
import com.example.secureapp.R;

import java.util.ArrayList;

public class GrupoFragment extends Fragment{

    AdapterGrupo adapterGrupo;
    RecyclerView recyclerViewGrupos;
    ArrayList<Grupo> listaGrupos;

    //referencias para comunicar fragments
    Activity actividad;
    IComunicaFragments iComunicaFragments;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_grupo,container, false);

        recyclerViewGrupos = view.findViewById(R.id.RV_grupos);
        listaGrupos = new ArrayList<>();
        //cargar lista
        cargarLista();
        //mostrar datos
        mostrarDatos();

        return view;

    }

    public void cargarLista(){

        listaGrupos.add(new Grupo("Grupo 1","Este es el grupo #1",R.drawable.ic_launcher_foreground));
        listaGrupos.add(new Grupo("Grupo 2","Este es el grupo #2",R.drawable.ic_launcher_nuevo_grupo_foreground));

    }

    public void mostrarDatos(){

        recyclerViewGrupos.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterGrupo = new AdapterGrupo(getContext(), listaGrupos);
        recyclerViewGrupos.setAdapter(adapterGrupo);

        adapterGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = listaGrupos.get(recyclerViewGrupos.getChildAdapterPosition(view)).getNombre();
                Toast.makeText(getContext(), "Selecciono: " + nombre, Toast.LENGTH_SHORT).show();

                iComunicaFragments.enviarGrupo(listaGrupos.get(recyclerViewGrupos.getChildAdapterPosition(view)));
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
