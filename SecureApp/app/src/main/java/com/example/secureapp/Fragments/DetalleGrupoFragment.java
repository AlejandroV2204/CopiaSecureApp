package com.example.secureapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secureapp.Adaptadores.AdapterGrupo;
import com.example.secureapp.Adaptadores.AdapterIntegrante;
import com.example.secureapp.Entidades.Grupo;
import com.example.secureapp.Modelo.MAgregarIntegrante;
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

import java.io.Serializable;
import java.util.ArrayList;

public class DetalleGrupoFragment extends Fragment {

    private AdapterIntegrante adapterIntegrante;
    private RecyclerView recyclerViewIntegrantes;
    private ArrayList<MIntegrante> listaIntegrantes= new ArrayList<>();
    private ArrayList<MAgregarIntegrante> listaAgregarIntegrantes= new ArrayList<>();

    TextView nombreDetalle, descripcionDetalle, agregarIntegrantes;
    String identificadorDetalle;
    ImageView imagenDetalle, IV_atras;

    private FirebaseFirestore firestore;

    private String identificador, nombre, apellido, email, telefono;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    Context contexto;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detalle_grupo,container, false);

        inicializarFireStore();

        nombreDetalle = view.findViewById(R.id.txt_nombreDetalleGrupo);
        agregarIntegrantes = view.findViewById(R.id.txt_agregarIntegrantes);
        IV_atras = view.findViewById(R.id.IV_atras_detalleGrupo);

        //imagenDetalle = view.findViewById(R.id.imagen_detalle_grupo);

        recyclerViewIntegrantes = view.findViewById(R.id.RV_integrantes);
        listaIntegrantes = new ArrayList<>();

        //Crear objeto bundle para recibir el objeto enviado por argumentos
        Bundle objetoGrupo = getArguments();
        MGrupo grupo = null;

        contexto = view.getContext();

        //Validación para verificar si existen argumentos enviados para mostrar
        if (objetoGrupo != null){

            grupo = (MGrupo) objetoGrupo.getSerializable("objetoGrupo");

            //Establecer los datos en las vistas
            nombreDetalle.setText(grupo.getNombre());
            identificadorDetalle = (grupo.getIdentificador());
            //imagenDetalle.setImageResource(grupo.getImagenid());

            //viewHolder.txt_identificadorGrupo.setText(grupo.getIdentificador());

            IV_atras.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    irAGrupos();

                }
            });

            agregarIntegrantes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){

                    MAgregarIntegrante agregarIntegrante = new MAgregarIntegrante(identificadorDetalle, nombre,  apellido, email, telefono);
                    agregarIntegrante.setIdentificador(identificadorDetalle);

                    irAgregarIntegrantes(agregarIntegrante);

                }
            });
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

        firestore.collection("usuario").document(email).collection("grupos").document(identificadorDetalle).collection("integrantes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String identificadorIntegrante = document.getId();
                                String nombreIntegrante = document.getString("nombre");
                                String apellidoIntegrante = document.getString("apellido");
                                String emailIntegrante = document.getString("email");
                                String telefonoIntegrante = document.getString("telefono");

                                listaIntegrantes.add(new MIntegrante(identificadorIntegrante, nombreIntegrante, apellidoIntegrante, emailIntegrante, telefonoIntegrante));
                            }

                            adapterIntegrante = new AdapterIntegrante(listaIntegrantes, R.layout.lista_integrantes);
                            recyclerViewIntegrantes.setAdapter(adapterIntegrante);

                        } else {
                            Toast.makeText(getContext(), "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    private void irAGrupos(){

        GrupoFragment grupoFragment = new GrupoFragment();

        fragmentManager = ((AppCompatActivity) contexto).getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, grupoFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    private void irAgregarIntegrantes(MAgregarIntegrante agregarIntegrante){

        this.identificadorDetalle = identificadorDetalle;

            //Aquí se realiza la lógica necesaria para poder realizar el envio
            AgregarIntegranteFragment agregarIntegranteFragment = new AgregarIntegranteFragment();

            //Objeto bundle para transportar la información
            Bundle bundleEnvio = new Bundle();

            //Enviar el objeto que está llegando con Serializable
            bundleEnvio.putSerializable("objetoNuevoIntegrante", agregarIntegrante);

            agregarIntegranteFragment.setArguments(bundleEnvio);

            //abrir fragment
            fragmentManager = ((AppCompatActivity) contexto).getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, agregarIntegranteFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();


    }

}
