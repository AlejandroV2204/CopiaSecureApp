package com.example.secureapp.Fragments;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
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
import com.example.secureapp.Modelo.MGrupo;
import com.example.secureapp.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class GrupoFragment extends Fragment{

    private AdapterGrupo adapterGrupo;
    private RecyclerView recyclerViewGrupos;
    private ArrayList<MGrupo> listaGrupos = new ArrayList<>();

    //referencias para comunicar fragments
    Activity actividad;
    IComunicaFragments iComunicaFragments;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_grupo,container, false);

        inicializarFirebase();

        recyclerViewGrupos = view.findViewById(R.id.RV_grupos);
        listaGrupos = new ArrayList<>();

        //No se si esto sirva
        recyclerViewGrupos.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        tomarGruposDeFirebase();

        return view;

    }

    private void inicializarFirebase(){

        FirebaseApp.initializeApp(getContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

    }

    private void tomarGruposDeFirebase() {

        databaseReference.child("grupo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot ds : snapshot.getChildren()) {

                        String nombreGrupo = ds.child("nombre").getValue().toString();
                        String descripcionGrupo = ds.child("nombre").getValue().toString();
                        String administradorGrupo = ds.child("nombre").getValue().toString();
                        String emailAdministrador = ds.child("nombre").getValue().toString();
                        String fechaCreacion = ds.child("nombre").getValue().toString();
                        String cantidadIntegrantes = ds.child("nombre").getValue().toString();
                        HashMap localizacionGrupo = (HashMap) ds.child("localizacion").getValue();

                        listaGrupos.add(new MGrupo(nombreGrupo, descripcionGrupo, administradorGrupo, emailAdministrador, fechaCreacion, cantidadIntegrantes, localizacionGrupo));

                    }

                    adapterGrupo = new AdapterGrupo(listaGrupos, R.layout.lista_grupos);
                    recyclerViewGrupos.setAdapter(adapterGrupo);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}

