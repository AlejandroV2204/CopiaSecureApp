package com.example.secureapp.Adaptadores;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secureapp.Fragments.DetalleContactoFragment;

import com.example.secureapp.Modelo.MContacto;
import com.example.secureapp.R;

import java.util.ArrayList;

public class AdapterContacto extends RecyclerView.Adapter<AdapterContacto.ViewHolder> implements View.OnClickListener {

    private final int resource;
    LayoutInflater inflater;
    ArrayList<MContacto> contactosList;

    //Listener
    private View.OnClickListener listener;

    //variables para cargar el fragment principal
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    Context contexto;


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            private TextView txt_nombreContacto, txt_apellidoContacto, txt_emailContacto, txt_telefonoContacto;
            private ImageView imageDetalle;
            public View view;

            private View.OnClickListener listener;

            public ViewHolder(View view){
                super(view);

                this.view = view;
                this.txt_nombreContacto = view.findViewById(R.id.txt_nombreContacto);
                this.txt_apellidoContacto = view.findViewById(R.id.txt_apellidoContacto);
                this.txt_emailContacto = view.findViewById(R.id.txt_emailContacto);
                this.txt_telefonoContacto = view.findViewById(R.id.txt_telefonoContacto);
                this.imageDetalle = view.findViewById(R.id.RV_imagen_contacto);

            }

            public void  setOnClickListener(View.OnClickListener listener){

                this.listener = listener;

            }

            @Override
            public void onClick(View view) {

                if (listener != null){

                    listener.onClick(view);

                }

            }

    }

    public AdapterContacto(ArrayList<MContacto> contactosList, int resource){

        this.contactosList = contactosList;
        this.resource = resource;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);

        view.setOnClickListener(this);

        contexto = parent.getContext();

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        MContacto contacto = contactosList.get(position);

        viewHolder.txt_nombreContacto.setText(contacto.getNombre());
        viewHolder.txt_apellidoContacto.setText(contacto.getApellido());
        viewHolder.txt_emailContacto.setText(contacto.getEmail());
        viewHolder.txt_telefonoContacto.setText(contacto.getTelefono());
        //viewHolder.imageDetalle.setImageDrawable(contacto.ge);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                enviarContacto(contacto);

            }
        });

    }

    @Override
    public int getItemCount() {
        return contactosList.size();
    }

    @Override
    public void onClick(View view) {

    }

    public void enviarContacto(MContacto contacto) {

    //Aquí se realiza la lógica necesaria para poder realizar el envio
    DetalleContactoFragment detalleContactoFragment = new DetalleContactoFragment();

    //Objeto bundle para transportar la información
    Bundle bundleEnvio = new Bundle();

    //Enviar el objeto que está llegando con Serializable
    bundleEnvio.putSerializable("objetoContacto", contacto);

    detalleContactoFragment.setArguments(bundleEnvio);

    //abrir fragment

        fragmentManager = ((AppCompatActivity) contexto).getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, detalleContactoFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

}
