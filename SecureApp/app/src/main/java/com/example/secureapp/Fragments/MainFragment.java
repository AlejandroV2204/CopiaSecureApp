package com.example.secureapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.secureapp.Adaptadores.AdapterGrupo;
import com.example.secureapp.Adaptadores.AdapterMain;
import com.example.secureapp.Modelo.MAlerta;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainFragment extends Fragment {

    private AdapterMain adapterMain;
    private RecyclerView recyclerViewMain;
    private ArrayList<MMain> listaMain = new ArrayList<>();
    private FirebaseFirestore firestore;

    private ArrayList<String> codigoAlerta = new ArrayList<String>();
    private String identificadorGrupo;

    String DescripcionAlerta;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_main, container, false);

        inicializarFireStore();
        verificarAlertasPropias();
        verificarGruposIntegrados();

        recyclerViewMain = view.findViewById(R.id.RV_main);
        listaMain = new ArrayList<>();


        //recyclerViewMain.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

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
                .whereEqualTo("favorita", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String identificadorAlerta = document.getId();

                                DocumentReference grupoRef = firestore.collection("usuario").document(email).collection("alertas").document(identificadorAlerta);
                                grupoRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {

                                                String descripcionAlerta = document.getString("descripcion");

                                                listaMain.add(new MMain(descripcionAlerta));

                                            }

                                            else{

                                                Toast.makeText(getContext(), "No such document", Toast.LENGTH_SHORT).show();

                                            }

                                            adapterMain = new AdapterMain(listaMain, R.layout.lista_main);
                                            recyclerViewMain.setAdapter(adapterMain);

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

                            if (codigoAlerta.size() == 0){

                                firestore.collection("alerta")
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

                            }
                            else{

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
                            }

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

                                                                                                HashMap<String, Object> integrante = new HashMap<>();
                                                                                                integrante.put("nombre", nombreIntegrante);
                                                                                                integrante.put("apellido", apellidoIntegrante);
                                                                                                integrante.put("email", emailIntegrante);
                                                                                                integrante.put("telefono", telefonoIntegrante);
                                                                                                integrante.put("identificadorGrupo", documentoIntegrante);

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
