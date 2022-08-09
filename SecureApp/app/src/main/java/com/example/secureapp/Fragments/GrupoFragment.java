package com.example.secureapp.Fragments;

import android.app.Activity;
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
import com.example.secureapp.Modelo.MGrupo;
import com.example.secureapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class GrupoFragment extends Fragment{

    private AdapterGrupo adapterGrupo;
    private RecyclerView recyclerViewGrupos;
    private ArrayList<MGrupo> listaGrupos = new ArrayList<>();

    //referencias para comunicar fragments
    Activity actividad;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private FirebaseFirestore firestore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_grupo,container, false);

        inicializarFireStore();

        recyclerViewGrupos = view.findViewById(R.id.RV_grupos);
        listaGrupos = new ArrayList<>();

        //No se si esto sirva
        recyclerViewGrupos.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        tomarDatosDeFirestore();

        return view;

    }

    private void inicializarFireStore(){

        FirebaseApp.initializeApp(getContext());
        firestore = FirebaseFirestore.getInstance();

    }

    private void tomarDatosDeFirestore(){

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();

        firestore.collection("usuario").document(email).collection("grupos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String identificadorGrupo = document.getId();
                                String nombreGrupo = document.getString("nombre");
                                String descripcionGrupo = document.getString("descripcion");
                                String administradorGrupo = document.getString("administradorGrupo");
                                String emailAdministrador = document.getString("emailAdministrador");
                                String fechaCreacion= document.getString("fechaCreacion");
                                String cantidadIntegrantes = document.getString("cantidadIntegrantes");
                                GeoPoint localizacionGrupo = document.getGeoPoint("localizacion");

                                listaGrupos.add(new MGrupo(identificadorGrupo, nombreGrupo, descripcionGrupo, administradorGrupo, emailAdministrador, fechaCreacion, cantidadIntegrantes, localizacionGrupo));
                            }

                            adapterGrupo = new AdapterGrupo(listaGrupos, R.layout.lista_grupos);
                            recyclerViewGrupos.setAdapter(adapterGrupo);


                        } else {
                            Toast.makeText(getContext(), "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }


}

