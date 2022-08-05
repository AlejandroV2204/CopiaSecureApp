package com.example.secureapp.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.secureapp.Adaptadores.AdapterAgregarIntegrante;
import com.example.secureapp.Adaptadores.AdapterContacto;
import com.example.secureapp.Interfaces.IComunicaFragments;
import com.example.secureapp.Modelo.MAgregarIntegrante;
import com.example.secureapp.Modelo.MContacto;
import com.example.secureapp.Modelo.MGrupo;
import com.example.secureapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AgregarIntegranteFragment extends Fragment{

    private EditText et_emailAgregarIntegrante;
    private String emailIntegrante, identificadorDetalle;
    AdapterAgregarIntegrante adapterAgregarIntegrantes;
    RecyclerView recyclerViewAgregarIntegrantes;
    private ArrayList<MAgregarIntegrante> listaAgregarIntegrantes = new ArrayList<>();

    FloatingActionButton btn_agregarIntegrantes;

    //referencias para comunicar fragments
    Activity actividad;
    IComunicaFragments iComunicaFragments;

    FirebaseAuth firebaseAuth;
    AwesomeValidation awesomeValidation;

    private FirebaseFirestore firestore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_agregar_integrante,container, false);

        inicializarFireStore();

        et_emailAgregarIntegrante = view.findViewById(R.id.et_emailAgregarIntegrante);
        recyclerViewAgregarIntegrantes = view.findViewById(R.id.RV_agregarIntegrantes);
        btn_agregarIntegrantes = view.findViewById(R.id.btn_agregarIntegrantes);
        listaAgregarIntegrantes = new ArrayList<>();

        //No se si esto sirva
        recyclerViewAgregarIntegrantes.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        //Crear objeto bundle para recibir el objeto enviado por argumentos
        Bundle objetoIntegrante = getArguments();
        MAgregarIntegrante agregarIntegrante = null;

        //Validación para verificar si existen argumentos enviados para mostrar
        if (objetoIntegrante != null){

            agregarIntegrante = (MAgregarIntegrante) objetoIntegrante.getSerializable("objetoIntegrante");

            //Establecer los datos en las vistas
            identificadorDetalle = (agregarIntegrante.getIdentificador());
            //imagenDetalle.setImageResource(grupo.getImagenid());

        }

        //tomarContactosDeFirebase();
        tomarDatosDeFirestore();

        btn_agregarIntegrantes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validarDatos();

            }
        });

        return view;

    }


    private void inicializarFireStore(){

        FirebaseApp.initializeApp(getContext());
        firestore = FirebaseFirestore.getInstance();

    }

    private void validarCampos(){

        firebaseAuth = FirebaseAuth.getInstance();
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(getActivity(), R.id.et_emailAgregarIntegrante, Patterns.EMAIL_ADDRESS, R.string.invalid_email);

    }

    private void validarDatos(){

        emailIntegrante = et_emailAgregarIntegrante.getText().toString();
        validarCampos();

        if (awesomeValidation.validate()){

            //btn_agregarIntegrantes.setVisibility(View.VISIBLE);
            crearIntegrante();
            limpiarCampos();

        }else {

            Toast.makeText(getActivity(), "Completa todos lo datos", Toast.LENGTH_SHORT).show();
        }

    }

    private void agregarIntegrante(){

        Toast.makeText(getContext(), "Accion en proceso menor", Toast.LENGTH_SHORT).show();

    }

    private void crearIntegrante() {

        DocumentReference usuarioRef = firestore.collection("usuario").document(emailIntegrante);
        usuarioRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        //Toast.makeText(getContext(), "DocumentSnapshot data:\n " + document.getData(), Toast.LENGTH_SHORT).show();
                        String identificadorIntegrante = document.getId();
                        String nombreIntegrante = document.getString("nombre");
                        String apellidoIntegrante = document.getString("apellido");
                        String emailIntegrante = document.getString("email");
                        String telefonoIntegrante = document.getString("telefono");

                        MAgregarIntegrante agregarIntegrante = new MAgregarIntegrante(identificadorIntegrante, nombreIntegrante, apellidoIntegrante, emailIntegrante, telefonoIntegrante);
                        agregarIntegrante.setIdentificador(identificadorIntegrante);
                        agregarIntegrante.setNombre(nombreIntegrante);
                        agregarIntegrante.setApellido(apellidoIntegrante);
                        agregarIntegrante.setEmail(emailIntegrante);
                        agregarIntegrante.setTelefono(telefonoIntegrante);

                        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();

                        firestore.collection("usuario").document(email).collection("grupos").document(identificadorDetalle).collection("integrantes").document(emailIntegrante)
                                .set(agregarIntegrante)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        Toast.makeText(getContext(), "Integrante agregado exitosamente", Toast.LENGTH_SHORT).show();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Toast.makeText(getContext(), "Error al agregar a dicho usuario", Toast.LENGTH_SHORT).show();

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

    private void tomarDatosDeFirestore(){

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();

        firestore.collection("usuario").document(email).collection("contactos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String identificadorAgregarIntegrantes = document.getId();
                                String nombreAgregarIntegrantes = document.getString("nombre");
                                String apellidoAgregarIntegrantes = document.getString("apellido");
                                String emailAgregarIntegrantes = document.getString("email");
                                String telefonoAgregarIntegrantes = document.getString("telefono");

                                listaAgregarIntegrantes.add(new MAgregarIntegrante(identificadorAgregarIntegrantes, nombreAgregarIntegrantes, apellidoAgregarIntegrantes, emailAgregarIntegrantes, telefonoAgregarIntegrantes));
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

        validarDatos();
        Toast.makeText(getContext(), "Accion en proceso", Toast.LENGTH_SHORT).show();

    }

    private void limpiarCampos(){

        et_emailAgregarIntegrante.setText("");

    }

}
