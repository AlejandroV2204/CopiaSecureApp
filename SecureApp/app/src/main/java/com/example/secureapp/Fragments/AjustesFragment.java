package com.example.secureapp.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.secureapp.Activities.RegistroActivity;
import com.example.secureapp.Modelo.MContacto;
import com.example.secureapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class AjustesFragment extends Fragment {

    TextView nombrePersonal, apellidoPersonal, telefonoPersonal, identificacionPersonal, emailPersonal;
    String nombre, apellido, identificacion, telefono, email;
    ImageView imagenDetalle, IV_atras;
    Button btn_guardar;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    FirebaseAuth firebaseAuth;
    AwesomeValidation awesomeValidation;
    private FirebaseFirestore firestore;

    Context contexto;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ajustes,container, false);

        inicializarFireStore();
        validarCampos();
        consultaInformacionPersonal();

        nombrePersonal = view.findViewById(R.id.et_nombrePropio);
        apellidoPersonal = view.findViewById(R.id.et_apellidoPropio);
        telefonoPersonal = view.findViewById(R.id.et_telefonoPropio);
        identificacionPersonal = view.findViewById(R.id.et_identificacionPropia);
        emailPersonal = view.findViewById(R.id.txt_emailPropio);
        imagenDetalle = view.findViewById(R.id.imagen_detalleContacto);
        IV_atras = view.findViewById(R.id.IV_atras_detalleContacto);
        btn_guardar = view.findViewById(R.id.btn_guardarInformacion);

        contexto = view.getContext();

        IV_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                volverAtras();

            }
        });

        btn_guardar.setOnClickListener(new View.OnClickListener() {
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

        awesomeValidation.addValidation((Activity) getContext(), R.id.et_nombrePropio, ".{1,}", R.string.invalid_campo);
        awesomeValidation.addValidation((Activity) getContext(), R.id.et_apellidoPropio, ".{1,}", R.string.invalid_campo);
        awesomeValidation.addValidation((Activity) getContext(), R.id.et_identificacionPropia, ".{7,}", R.string.invalid_identificacion);
        awesomeValidation.addValidation((Activity) getContext(), R.id.et_telefonoPropio, ".{10,}", R.string.invalid_telefono);

    }

    private void validarDatos(){

        nombre = nombrePersonal.getText().toString();
        apellido = apellidoPersonal.getText().toString();
        identificacion = identificacionPersonal.getText().toString();
        telefono = telefonoPersonal.getText().toString();
        email = emailPersonal.getText().toString();

        if (awesomeValidation.validate()){

            guardarInformacion();

        }else {

            Toast.makeText(getContext(), "Completa todos lo datos", Toast.LENGTH_SHORT).show();
        }

    }

    public void guardarInformacion(){

        HashMap<String, Object> usuario = new HashMap<>();
        usuario.put("nombre", nombre);
        usuario.put("apellido", apellido);
        usuario.put("telefono", telefono);
        usuario.put("identificacion", identificacion);

        firestore.collection("usuario").document(email)
                .update(usuario)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(getContext(), "Información de usuario guardada exitosamente", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getContext(), "Error en el registro", Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void volverAtras(){

        MainFragment mainFragment = new MainFragment();

        fragmentManager = ((AppCompatActivity) contexto).getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, mainFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    private void consultaInformacionPersonal(){

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();

        DocumentReference usuarioRef = firestore.collection("usuario").document(email);
        usuarioRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        //Toast.makeText(getContext(), "DocumentSnapshot data:\n " + document.getData(), Toast.LENGTH_SHORT).show();
                        String nombrePropio = document.getString("nombre");
                        String apellidoPropio = document.getString("apellido");
                        String telefonoPropio = document.getString("telefono");
                        String identificacionPropio = document.getString("identificacion");
                        String emailPropio = document.getString("email");

                        nombrePersonal.setText(nombrePropio);
                        apellidoPersonal.setText(apellidoPropio);
                        telefonoPersonal.setText(telefonoPropio);
                        identificacionPersonal.setText(identificacionPropio);
                        emailPersonal.setText(emailPropio);

                    } else {

                        Toast.makeText(getContext(), "get failed with " + task.getException(), Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
    }
}
