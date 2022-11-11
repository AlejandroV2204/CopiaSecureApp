package com.example.secureapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.secureapp.Adaptadores.AdapterAlerta;
import com.example.secureapp.Adaptadores.AdapterContacto;
import com.example.secureapp.Adaptadores.AdapterGrupo;
import com.example.secureapp.Adaptadores.AdapterMain;
import com.example.secureapp.Modelo.MAlerta;
import com.example.secureapp.Modelo.MContacto;
import com.example.secureapp.Modelo.MGrupo;
import com.example.secureapp.Modelo.MMain;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AlertaFragment extends Fragment {

    private AdapterAlerta adapterAlerta;
    private RecyclerView recyclerViewAlertas;
    private ArrayList<MAlerta> listaAlerta = new ArrayList<>();
    private FirebaseFirestore firestore;

    private ArrayList<String> codigoAlerta = new ArrayList<String>();

    private Spinner spinner_gruposAlertas;

    private AdapterGrupo adapterGrupo;
    private RecyclerView recyclerViewGrupos;
    private ArrayList<MGrupo> listaGrupos = new ArrayList<>();
    //ArrayList <String> listaGruposA = new ArrayList<>();
    ArrayAdapter <MGrupo>  listaGruposA;

    String DescripcionAlerta;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_alerta, container, false);

        inicializarFireStore();
        verificarAlertasPropias();
        rellenarSpinnerGrupos();

        recyclerViewAlertas = view.findViewById(R.id.RV_alerta);
        spinner_gruposAlertas = view.findViewById(R.id.spinner_gruposAlertas);
        listaAlerta = new ArrayList<>();

        recyclerViewAlertas.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        tomarDatosDeFirestore();

        String id = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();


        //imagen = (String) view.findViewById(R.drawable.secureapplogo);

        FirebaseMessaging.getInstance().subscribeToTopic("enviaratodos").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //Toast.makeText(getContext(),"Registrado",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }


    private void inicializarFireStore(){

        FirebaseApp.initializeApp(getContext());
        firestore = FirebaseFirestore.getInstance();

    }

    private void tomarDatosDeFirestore(){

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        firestore.collection("usuario").document(email).collection("alertas")
                .orderBy("descripcion")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String identificadorAlerta = document.getId();
                                String descripcionAlerta = document.getString("descripcion");
                                boolean alertaFavorita = document.getBoolean("favorita");

                                listaAlerta.add(new MAlerta(descripcionAlerta, alertaFavorita));

                            }

                            adapterAlerta = new AdapterAlerta(listaAlerta, R.layout.lista_alerta);
                            recyclerViewAlertas.setAdapter(adapterAlerta);

                        } else {
                            Toast.makeText(getContext(), "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

}

    private void verificarAlertasPropias(){

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        firestore.collection("usuario").document(email).collection("alertas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                codigoAlerta.add(document.getString("codigo"));

                            }

                            firestore.collection("alerta")
                                    .whereNotIn("codigo", codigoAlerta)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {

                                                    String identificadorAlerta = document.getId();
                                                    String nombreAlerta = document.getString("nombre");
                                                    String codigoAlerta = document.getString("codigo");
                                                    String descripcionAlerta = document.getString("descripcion");
                                                    boolean alertaFavorita = false;

                                                    HashMap<String, Object> alerta = new HashMap<>();
                                                    alerta.put("identificador", identificadorAlerta);
                                                    alerta.put("nombre", nombreAlerta);
                                                    alerta.put("codigo", codigoAlerta);
                                                    alerta.put("descripcion", descripcionAlerta);
                                                    alerta.put("favorita", alertaFavorita);

                                                    firestore.collection("usuario").document(email).collection("alertas").document(identificadorAlerta)
                                                            .set(alerta)
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

                                                }

                                            } else {
                                                Toast.makeText(getContext(), "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                        } else {
                            Toast.makeText(getContext(), "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void rellenarSpinnerGrupos(){

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

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

                                //listaGruposA.add(nombreGrupo);
                            }

                            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.R_listaAntioquia, android.R.layout.simple_spinner_item);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner_gruposAlertas.setAdapter(adapter);


                        } else {
                            Toast.makeText(getContext(), "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
