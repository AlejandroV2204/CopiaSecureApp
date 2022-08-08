package com.example.secureapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.secureapp.Entidades.Contacto;
import com.example.secureapp.Modelo.MContacto;
import com.example.secureapp.R;

public class DetalleContactoFragment extends Fragment {

    TextView nombreDetalle, apellidoDetalle, telefonoDetalle, emailDetalle;
    ImageView imagenDetalle, IV_atras;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    Context contexto;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detalle_contacto,container, false);

        nombreDetalle = view.findViewById(R.id.txt_nombreDetalleContacto);
        telefonoDetalle = view.findViewById(R.id.txt_telefonoDetalleContacto);
        emailDetalle = view.findViewById(R.id.txt_emailDetalleContacto);
        imagenDetalle = view.findViewById(R.id.imagen_detalleContacto);
        IV_atras = view.findViewById(R.id.IV_atras_detalleContacto);

        contexto = view.getContext();

        //Crear objeto bundle para recibir el objeto enviado por argumentos
        Bundle objetoContacto = getArguments();
        MContacto contacto = null;

        //Validación para verificar si existen argumentos enviados para mostrar
        if (objetoContacto != null){

            contacto = (MContacto) objetoContacto.getSerializable("objetoContacto");

            //Establecer los datos en las vistas
            nombreDetalle.setText(contacto.getNombre() + " " + contacto.getApellido());
            emailDetalle.setText(contacto.getEmail());
            telefonoDetalle.setText(contacto.getTelefono());
            //imagenDetalle.setImageResource(contacto.getImagenid());
        }

        IV_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                irAContactos();

            }
        });

        return view;
    }

    private void irAContactos(){

        ContactoFragment contactoFragment = new ContactoFragment();

        fragmentManager = ((AppCompatActivity) contexto).getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, contactoFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
}
