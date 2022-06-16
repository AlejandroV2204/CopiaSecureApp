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

import com.example.secureapp.Entidades.Contacto;
import com.example.secureapp.Entidades.Grupo;
import com.example.secureapp.R;

public class DetalleContactoFragment extends Fragment {

    TextView nombreDetalle, telefonoDetalle;
    ImageView imagenDetalle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detalle_contacto,container, false);

        nombreDetalle = view.findViewById(R.id.txt_nombreDetalleContacto);
        telefonoDetalle = view.findViewById(R.id.txt_telefonoDetalleContacto);
        imagenDetalle = view.findViewById(R.id.imagen_detalleContacto);

        //Crear objeto bundle para recibir el objeto enviado por argumentos
        Bundle objetoContacto = getArguments();
        Contacto contacto = null;

        //Validación para verificar si existen argumentos enviados para mostrar
        if (objetoContacto != null){

            contacto = (Contacto) objetoContacto.getSerializable("objetoContacto");

            //Establecer los datos en las vistas
            nombreDetalle.setText(contacto.getNombre());
            telefonoDetalle.setText(contacto.getTelefono());
            imagenDetalle.setImageResource(contacto.getImagenid());
        }

        return view;
    }
}
