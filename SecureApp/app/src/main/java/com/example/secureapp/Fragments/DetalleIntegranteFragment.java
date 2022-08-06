package com.example.secureapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.secureapp.Modelo.MContacto;
import com.example.secureapp.Modelo.MIntegrante;
import com.example.secureapp.R;

public class DetalleIntegranteFragment extends Fragment {

    TextView nombreDetalle, apellidoDetalle, telefonoDetalle, emailDetalle;
    ImageView imagenDetalle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detalle_integrante,container, false);

        nombreDetalle = view.findViewById(R.id.txt_nombreDetalleIntegrante);
        telefonoDetalle = view.findViewById(R.id.txt_telefonoDetalleIntegrante);
        emailDetalle = view.findViewById(R.id.txt_emailDetalleIntegrante);
        imagenDetalle = view.findViewById(R.id.imagen_detalleIntegrante);

        //Crear objeto bundle para recibir el objeto enviado por argumentos
        Bundle objetoIntegrante = getArguments();
        MIntegrante integrante = null;

        //Validación para verificar si existen argumentos enviados para mostrar
        if (objetoIntegrante != null){

            integrante = (MIntegrante) objetoIntegrante.getSerializable("objetoIntegrante");

            //Establecer los datos en las vistas
            nombreDetalle.setText(integrante.getNombre() + " " + integrante.getApellido());
            emailDetalle.setText(integrante.getEmail());
            telefonoDetalle.setText(integrante.getTelefono());
            //imagenDetalle.setImageResource(contacto.getImagenid());
        }

        return view;
    }
}
