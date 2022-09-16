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
import com.example.secureapp.Adaptadores.AdapterContacto;
import com.example.secureapp.Adaptadores.AdapterGrupo;
import com.example.secureapp.Modelo.MAlerta;
import com.example.secureapp.Modelo.MContacto;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AlertaFragment extends Fragment {

    private AdapterAlerta adapterAlerta;
    private RecyclerView recyclerViewAlertas;
    private ArrayList<MAlerta> listaAlerta = new ArrayList<>();
    private FirebaseFirestore firestore;

    String DescripcionAlerta;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_alerta, container, false);

        inicializarFireStore();

        recyclerViewAlertas = view.findViewById(R.id.RV_alerta);
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

        firestore.collection("alerta")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {


                                String descripcionAlerta = document.getString("descripcion");

                                listaAlerta.add(new MAlerta(descripcionAlerta));
                            }

                            adapterAlerta = new AdapterAlerta(listaAlerta, R.layout.lista_alerta);
                            recyclerViewAlertas.setAdapter(adapterAlerta);

                        } else {
                            Toast.makeText(getContext(), "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

}
