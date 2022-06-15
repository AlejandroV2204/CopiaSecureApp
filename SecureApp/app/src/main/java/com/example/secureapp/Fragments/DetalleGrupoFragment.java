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

import com.example.secureapp.Entidades.Grupo;
import com.example.secureapp.R;

public class DetalleGrupoFragment extends Fragment {

    TextView nombreDetalle;
    ImageView imagenDetalle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detalle_grupo,container, false);

        nombreDetalle = view.findViewById(R.id.txt_nombreDetalle);
        imagenDetalle = view.findViewById(R.id.imagen_detalle);

        //Crear objeto bundle para recibir el objeto enviado por argumentos
        Bundle objetoGrupo = getArguments();
        Grupo grupo = null;

        //Validación para verificar si existen argumentos enviados para mostrar
        if (objetoGrupo != null){

            grupo = (Grupo) objetoGrupo.getSerializable("objeto");

            //Establecer los datos en las vistas
            nombreDetalle.setText(grupo.getNombre());
            imagenDetalle.setImageResource(grupo.getImagenid());
        }

        return view;
    }
}
