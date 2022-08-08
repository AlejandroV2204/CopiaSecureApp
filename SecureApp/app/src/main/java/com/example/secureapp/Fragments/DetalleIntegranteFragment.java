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

import com.example.secureapp.Modelo.MGrupo;
import com.example.secureapp.Modelo.MIntegrante;
import com.example.secureapp.R;

public class DetalleIntegranteFragment extends Fragment {

    TextView nombreIntegranteDetalle, apellidoIntegranteDetalle, telefonoIntegranteDetalle, emailIntegranteDetalle;
    ImageView imagenIntegranteDetalle, IV_atras;

    String identificadorGrupoDetalle;
    TextView nombreGrupoDetalle;
    TextView descricionGrupoDetalle;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    Context contexto;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detalle_integrante,container, false);

        nombreIntegranteDetalle = view.findViewById(R.id.txt_nombreDetalleIntegrante);
        telefonoIntegranteDetalle = view.findViewById(R.id.txt_telefonoDetalleIntegrante);
        emailIntegranteDetalle = view.findViewById(R.id.txt_emailDetalleIntegrante);
        imagenIntegranteDetalle = view.findViewById(R.id.imagen_detalleIntegrante);
        IV_atras = view.findViewById(R.id.IV_atras_detalleIntegrante);
        contexto = view.getContext();

        //Crear objeto bundle para recibir el objeto enviado por argumentos
        Bundle objetoIntegrante = getArguments();
        MIntegrante integrante = null;

        //Validación para verificar si existen argumentos enviados para mostrar
        if (objetoIntegrante != null){

            integrante = (MIntegrante) objetoIntegrante.getSerializable("objetoIntegrante");

            //Establecer los datos en las vistas
            nombreIntegranteDetalle.setText(integrante.getNombre() + " " + integrante.getApellido());
            emailIntegranteDetalle.setText(integrante.getEmail());
            telefonoIntegranteDetalle.setText(integrante.getTelefono());
            //imagenDetalle.setImageResource(contacto.getImagenid());
        }



            IV_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(getContext(), "Accion en proceso", Toast.LENGTH_SHORT).show();
                irADetalleGrupo();

            }
        });

        return view;
    }

    private void irADetalleGrupo(){

        fragmentManager = ((AppCompatActivity) contexto).getSupportFragmentManager();
        fragmentManager.popBackStack();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.commit();

    }
}
