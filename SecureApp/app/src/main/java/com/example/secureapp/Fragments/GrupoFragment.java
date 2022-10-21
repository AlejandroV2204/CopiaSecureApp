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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class GrupoFragment extends Fragment{

    private AdapterGrupo adapterGrupo;
    private RecyclerView recyclerViewGrupos;
    private ArrayList<MGrupo> listaGrupos = new ArrayList<>();

    private String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

    //referencias para comunicar fragments
    Activity actividad;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private FirebaseFirestore firestore;
    private String identificadorGrupo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_grupo,container, false);

        inicializarFireStore();
        verificarGruposIntegrados();

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

        firestore.collection("usuario").document(email).collection("grupos")
                .orderBy("nombre")
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

    private void verificarGruposIntegrados(){

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        firestore.collection("grupo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                identificadorGrupo = document.getId();

                                firestore.collection("grupo").document(identificadorGrupo).collection("integrantes")
                                        .whereEqualTo("email", email)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                                        String identificadorGrupoDos = document.getString("identificadorGrupo");
                                                        //Toast.makeText(getContext(), "" + identificadorGrupo, Toast.LENGTH_SHORT).show();

                                                        DocumentReference grupoRef = firestore.collection("grupo").document(identificadorGrupoDos);
                                                        grupoRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                if (task.isSuccessful()) {
                                                                    DocumentSnapshot document = task.getResult();
                                                                    if (document.exists()) {

                                                                        String identificadorGrupoConsulta = document.getString("identificador");
                                                                        String nombreGrupo = document.getString("nombre");
                                                                        String descripcionGrupo = document.getString("descripcion");
                                                                        String administradorGrupo = document.getString("administrador");
                                                                        String emailAdministrador = document.getString("emailAdministrador");
                                                                        String fechaCreacion = document.getString("fechaCreacion");
                                                                        GeoPoint localizacion = document.getGeoPoint("localizacion");

                                                                        HashMap<String, Object> grupo = new HashMap<>();
                                                                        grupo.put("identificador", identificadorGrupoConsulta);
                                                                        grupo.put("nombre", nombreGrupo);
                                                                        grupo.put("descripcion", descripcionGrupo);
                                                                        grupo.put("administrador", administradorGrupo);
                                                                        grupo.put("emailAdministrador", emailAdministrador);
                                                                        grupo.put("fechaCreacion", fechaCreacion);
                                                                        grupo.put("localizacion", localizacion);

                                                                        firestore.collection("usuario").document(email).collection("grupos").document(identificadorGrupoConsulta)
                                                                                .set(grupo)
                                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void aVoid) {

                                                                                        //Toast.makeText(getContext(), "Grupo agregado exitosamente al usuario", Toast.LENGTH_SHORT).show();

                                                                                    }
                                                                                })
                                                                                .addOnFailureListener(new OnFailureListener() {
                                                                                    @Override
                                                                                    public void onFailure(@NonNull Exception e) {

                                                                                        Toast.makeText(getContext(), "Error en la integración del grupo al usuario", Toast.LENGTH_SHORT).show();

                                                                                    }
                                                                                });

                                                                        firestore.collection("grupo").document(identificadorGrupo).collection("integrantes")
                                                                                .get()
                                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                        if (task.isSuccessful()) {
                                                                                            for (QueryDocumentSnapshot document : task.getResult()) {

                                                                                                String nombreIntegrante = document.getString("nombre");
                                                                                                String apellidoIntegrante = document.getString("apellido");
                                                                                                String emailIntegrante = document.getString("email");
                                                                                                String telefonoIntegrante = document.getString("telefono");
                                                                                                String documentoIntegrante = document.getString("identificadorGrupo");
                                                                                                String tokenIntegrante = document.getString("tokenAlerta");

                                                                                                HashMap<String, Object> integrante = new HashMap<>();
                                                                                                integrante.put("nombre", nombreIntegrante);
                                                                                                integrante.put("apellido", apellidoIntegrante);
                                                                                                integrante.put("email", emailIntegrante);
                                                                                                integrante.put("telefono", telefonoIntegrante);
                                                                                                integrante.put("identificadorGrupo", documentoIntegrante);
                                                                                                integrante.put("tokenAlerta", tokenIntegrante);

                                                                                                firestore.collection("usuario").document(email).collection("grupos").document(identificadorGrupo).collection("integrantes").document(email)
                                                                                                        .set(integrante)
                                                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                            @Override
                                                                                                            public void onSuccess(Void aVoid) {

                                                                                                                //Toast.makeText(getActivity(), "Usuario creado exitosamente", Toast.LENGTH_SHORT).show();

                                                                                                            }
                                                                                                        })
                                                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                                                            @Override
                                                                                                            public void onFailure(@NonNull Exception e) {

                                                                                                                Toast.makeText(getContext(), "Error en el registro", Toast.LENGTH_SHORT).show();

                                                                                                            }
                                                                                                        });

                                                                                            }

                                                                                        } else {
                                                                                            Toast.makeText(getContext(), "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    }
                                                                                });

                                                                    } else {

                                                                        Toast.makeText(getContext(), "No such document", Toast.LENGTH_SHORT).show();

                                                                    }
                                                                } else {

                                                                    Toast.makeText(getContext(), "get failed with " + task.getException(), Toast.LENGTH_SHORT).show();

                                                                }
                                                            }
                                                        });

                                                    }

                                                } else {
                                                    Toast.makeText(getContext(), "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                            }

                        } else {
                            Toast.makeText(getContext(), "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

}

