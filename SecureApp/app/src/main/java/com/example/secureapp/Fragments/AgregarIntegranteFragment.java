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

import com.example.secureapp.Adaptadores.AdapterAgregarIntegrante;
import com.example.secureapp.Adaptadores.AdapterContacto;
import com.example.secureapp.Interfaces.IComunicaFragments;
import com.example.secureapp.Modelo.MAgregarIntegrante;
import com.example.secureapp.Modelo.MContacto;
import com.example.secureapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AgregarIntegranteFragment extends Fragment{

    AdapterAgregarIntegrante adapterAgregarIntegrantes;
    RecyclerView recyclerViewAgregarIntegrantes;
    private ArrayList<MAgregarIntegrante> listaAgregarIntegrantes = new ArrayList<>();

    FloatingActionButton btn_agregarIntegrantes;

    //referencias para comunicar fragments
    Activity actividad;
    IComunicaFragments iComunicaFragments;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private FirebaseFirestore firestore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_agregar_integrante,container, false);

        inicializarFireStore();

        recyclerViewAgregarIntegrantes = view.findViewById(R.id.RV_agregarIntegrantes);
        btn_agregarIntegrantes = view.findViewById(R.id.btn_agregarIntegrantes);
        listaAgregarIntegrantes = new ArrayList<>();

        //No se si esto sirva
        recyclerViewAgregarIntegrantes.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        //tomarContactosDeFirebase();
        tomarDatosDeFirestore();

        btn_agregarIntegrantes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                accionBoton();

            }
        });

        return view;

    }


    private void inicializarFireStore(){

        FirebaseApp.initializeApp(getContext());
        firestore = FirebaseFirestore.getInstance();

    }

    private void tomarDatosDeFirestore(){

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();

        firestore.collection("usuario").document(email).collection("contactos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String nombreAgregarIntegrantes = document.getString("nombre");
                                String apellidoAgregarIntegrantes = document.getString("apellido");
                                String emailAgregarIntegrantes = document.getString("email");
                                String telefonoAgregarIntegrantes = document.getString("telefono");

                                listaAgregarIntegrantes.add(new MAgregarIntegrante(nombreAgregarIntegrantes, apellidoAgregarIntegrantes, emailAgregarIntegrantes, telefonoAgregarIntegrantes));
                            }

                            adapterAgregarIntegrantes = new AdapterAgregarIntegrante(listaAgregarIntegrantes, R.layout.lista_agregar_integrantes);
                            recyclerViewAgregarIntegrantes.setAdapter(adapterAgregarIntegrantes);

                        } else {
                            Toast.makeText(getContext(), "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    private void accionBoton(){

        Toast.makeText(getContext(), "Accion en proceso", Toast.LENGTH_SHORT).show();
    }

}
