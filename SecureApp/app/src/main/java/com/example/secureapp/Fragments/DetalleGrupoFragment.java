package com.example.secureapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secureapp.Adaptadores.AdapterGrupo;
import com.example.secureapp.Adaptadores.AdapterIntegrante;
import com.example.secureapp.Entidades.Grupo;
import com.example.secureapp.Modelo.MGrupo;
import com.example.secureapp.Modelo.MIntegrante;
import com.example.secureapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DetalleGrupoFragment extends Fragment {

    private AdapterIntegrante adapterIntegrante;
    private RecyclerView recyclerViewIntegrantes;
    private ArrayList<MIntegrante> listaIntegrantes= new ArrayList<>();

    TextView nombreDetalle;
    ImageView imagenDetalle;

    private FirebaseFirestore firestore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detalle_grupo,container, false);

        inicializarFireStore();

        nombreDetalle = view.findViewById(R.id.txt_nombreDetalleGrupo);
        imagenDetalle = view.findViewById(R.id.imagen_detalle_grupo);

        recyclerViewIntegrantes = view.findViewById(R.id.RV_integrantes);
        listaIntegrantes = new ArrayList<>();

        //Crear objeto bundle para recibir el objeto enviado por argumentos
        Bundle objetoGrupo = getArguments();
        Grupo grupo = null;

        //Validación para verificar si existen argumentos enviados para mostrar
        if (objetoGrupo != null){

            grupo = (Grupo) objetoGrupo.getSerializable("objetoGrupo");

            //Establecer los datos en las vistas
            nombreDetalle.setText(grupo.getNombre());
            imagenDetalle.setImageResource(grupo.getImagenid());
        }

        //No se si esto sirva
        recyclerViewIntegrantes.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

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

                                String nombreIntegrante = document.getString("nombre");
                                String apellidoIntegrante = document.getString("apellido");
                                String emailIntegrante = document.getString("email");
                                String telefonoIntegrante = document.getString("telefono");

                                listaIntegrantes.add(new MIntegrante(nombreIntegrante, apellidoIntegrante, emailIntegrante, telefonoIntegrante));
                            }

                            adapterIntegrante = new AdapterIntegrante(listaIntegrantes, R.layout.lista_integrantes);
                            recyclerViewIntegrantes.setAdapter(adapterIntegrante);

                        } else {
                            Toast.makeText(getContext(), "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

}
