package com.example.secureapp.Fragments;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.secureapp.Activities.RegistroActivity;
import com.example.secureapp.Modelo.MContacto;
import com.example.secureapp.Modelo.MUsuario;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class NuevoContactoFragment extends Fragment{

    EditText et_nombreContacto, et_apellidoContacto, et_emailContacto;
    String nombreContacto, apellidoContacto, emailContacto, telefonoContacto, tokenContacto;

    String nombre, apellido, email, telefono;

    Button btn_crearContacto;

    FirebaseAuth firebaseAuth;
    AwesomeValidation awesomeValidation;

    private FirebaseFirestore firestore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nuevo_contacto,container, false);

        inicializarFireStore();

        et_emailContacto = view.findViewById(R.id.et_emailContacto);
        btn_crearContacto = view.findViewById(R.id.btn_crearContacto);

        btn_crearContacto.setOnClickListener(new View.OnClickListener() {
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

        awesomeValidation.addValidation(getActivity(), R.id.et_emailContacto, Patterns.EMAIL_ADDRESS, R.string.invalid_email);

    }

    private void validarDatos(){

        emailContacto = et_emailContacto.getText().toString();
        validarCampos();

        if (awesomeValidation.validate()){

            crearContacto();
            Toast.makeText(getActivity(), "Contacto creado con exito", Toast.LENGTH_SHORT).show();
            limpiarCampos();

        }else {

            Toast.makeText(getActivity(), "Completa todos lo datos", Toast.LENGTH_SHORT).show();
        }

    }

    private void crearContacto(){

        //Consulta en FireStore para extraer los datos de un usuario, y
        DocumentReference usuarioRef = firestore.collection("usuario").document(emailContacto);
        usuarioRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        //Toast.makeText(getContext(), "DocumentSnapshot data:\n " + document.getData(), Toast.LENGTH_SHORT).show();
                        nombreContacto = document.getString("nombre");
                        apellidoContacto = document.getString("apellido");
                        emailContacto = document.getString("email");
                        telefonoContacto = document.getString("telefono");
                        tokenContacto = document.getString("tokenAlerta");

                        MContacto contacto = new MContacto(nombreContacto, apellidoContacto, emailContacto, telefonoContacto, tokenContacto);

                        contacto.setNombre(nombreContacto);
                        contacto.setApellido(apellidoContacto);
                        contacto.setEmail(emailContacto);
                        contacto.setTelefono(telefonoContacto);
                        contacto.setTokenAlerta(tokenContacto);

                        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();

                        firestore.collection("usuario").document(email).collection("contactos").document(emailContacto)
                                .set(contacto)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        Toast.makeText(getContext(), "Contacto creado exitosamente", Toast.LENGTH_SHORT).show();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Toast.makeText(getContext(), "Error en la creción del contacto", Toast.LENGTH_SHORT).show();

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

    private void limpiarCampos(){

        et_emailContacto.setText("");

    }

}
