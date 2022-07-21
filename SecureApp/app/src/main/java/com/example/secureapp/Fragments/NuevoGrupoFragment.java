package com.example.secureapp.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.secureapp.Modelo.MGrupo;
import com.example.secureapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;

public class NuevoGrupoFragment extends Fragment {

    EditText et_nombreGrupo, et_direccionGrupo;
    Button btn_crearGrupo;

    FirebaseAuth firebaseAuth;
    AwesomeValidation awesomeValidation;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    String nombreGrupo, descripcionGrupo, administradorGrupo, fechaCreacion, cantidadIntegrantes;
    private HashMap<String, Object> ubicacion;
    private int day;
    private int month;
    private int year;

    private FusedLocationProviderClient fusedLocationClient;

    private GoogleMap map;

    private FirebaseFirestore firestore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nuevo_grupo, container, false);

        inicializarFireStore();
        tomarUbicacionActual();

        firebaseAuth = FirebaseAuth.getInstance();
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(getActivity(), R.id.et_nombreGrupo, ".{1,}", R.string.invalid_campo);
        awesomeValidation.addValidation(getActivity(), R.id.et_descripcionGrupo, ".{1,}", R.string.invalid_campo);

        et_nombreGrupo = view.findViewById(R.id.et_nombreGrupo);
        et_direccionGrupo = view.findViewById(R.id.et_descripcionGrupo);
        btn_crearGrupo = view.findViewById(R.id.btn_crearGrupo);

        btn_crearGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nombreGrupo = et_nombreGrupo.getText().toString();
                descripcionGrupo = et_direccionGrupo.getText().toString();

                if (awesomeValidation.validate()) {

                    tomarDatosUsuario();
                    tomarFechaActual();
                    limpiarCampos();

                } else {

                    Toast.makeText(getActivity(), "Completa todos lo datos", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }

    private void inicializarFireStore() {

        FirebaseApp.initializeApp(getContext());
        firestore = FirebaseFirestore.getInstance();

    }

    private void tomarDatosUsuario() {

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();

        DocumentReference usuarioRef = firestore.collection("usuario").document(email);
        usuarioRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        //Toast.makeText(getContext(), "DocumentSnapshot data:\n " + document.getData(), Toast.LENGTH_SHORT).show();

                        administradorGrupo = document.getString("nombre") + " " +document.getString("apellido");

                        Toast.makeText(getContext(), "Datos obtenido sobre el usuario", Toast.LENGTH_SHORT).show();

                        registrarGrupoFireStore(administradorGrupo, fechaCreacion);


                    } else {

                        Toast.makeText(getContext(), "No such document", Toast.LENGTH_SHORT).show();

                    }
                } else {

                    Toast.makeText(getContext(), "get failed with " + task.getException(), Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private void tomarUbicacionActual() {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {

                            ubicacion = new HashMap<>();
                            ubicacion.put("Latitud", location.getLatitude());
                            ubicacion.put("Longitud", location.getLongitude());

                        }
                    }
                });

    }

    private void tomarFechaActual(){

        Calendar fecha = Calendar.getInstance();

        day = fecha.get(Calendar.DAY_OF_MONTH);
        month = fecha.get(Calendar.MONTH);
        year = fecha.get(Calendar.YEAR);

        fechaCreacion = day + "/" + month + "/" + year;

    }

    private void registrarGrupoFireStore(String administrador, String fecha) {

        this.administradorGrupo = administrador;
        this.fechaCreacion = fecha;

        MGrupo grupo = new MGrupo(nombreGrupo, descripcionGrupo, administradorGrupo, fechaCreacion, cantidadIntegrantes, ubicacion);

        grupo.setNombre(nombreGrupo);
        grupo.setDescripción(descripcionGrupo);
        grupo.setAdministrador(administrador);
        grupo.setFechaCreacion(fecha);
        grupo.setLocalizacion(ubicacion);

        firestore.collection("grupo")
                .add(grupo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        Toast.makeText(getContext(), "Grupo creado exitosamente", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getContext(), "Error en el registro", Toast.LENGTH_SHORT).show();

                    }
                });

        //firestore.collection().document().collection("integrantes")
    }

    private void registrarGrupoAUsuario(){}

    private void limpiarCampos(){

        et_nombreGrupo.setText("");
        et_direccionGrupo.setText("");

    }
}
