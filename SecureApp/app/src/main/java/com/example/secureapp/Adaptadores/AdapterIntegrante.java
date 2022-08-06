package com.example.secureapp.Adaptadores;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secureapp.Fragments.DetalleContactoFragment;
import com.example.secureapp.Fragments.DetalleGrupoFragment;
import com.example.secureapp.Fragments.DetalleIntegranteFragment;
import com.example.secureapp.Modelo.MAgregarIntegrante;
import com.example.secureapp.Modelo.MContacto;
import com.example.secureapp.Modelo.MIntegrante;
import com.example.secureapp.R;

import java.util.ArrayList;

public class AdapterIntegrante extends RecyclerView.Adapter<AdapterIntegrante.ViewHolder> implements View.OnClickListener  {

    private final int resource;
    LayoutInflater inflater;
    ArrayList<MIntegrante> integrantesList;

    //Listener
    private View.OnClickListener listener;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    Context contexto;


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            private TextView txt_nombreIntegrante, txt_apellidoIntegrante, txt_emailIntegrante, txt_telefonoIntegrante;
            public View view;

            private View.OnClickListener listener;

            public ViewHolder(View view){
                super(view);

                this.view = view;
                this.txt_nombreIntegrante = view.findViewById(R.id.txt_nombreIntegrante);
                this.txt_apellidoIntegrante = view.findViewById(R.id.txt_apellidoIntegrante);
                this.txt_emailIntegrante = view.findViewById(R.id.txt_emailIntegrante);
                this.txt_telefonoIntegrante = view.findViewById(R.id.txt_telefonoContacto);

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

    public AdapterIntegrante(ArrayList<MIntegrante> integrantesList, int resource){

        this.integrantesList = integrantesList;
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
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int index) {

        MIntegrante integrante = integrantesList.get(index);

        viewHolder.txt_nombreIntegrante.setText(integrante.getNombre());
        viewHolder.txt_apellidoIntegrante.setText(integrante.getApellido());
        viewHolder.txt_emailIntegrante.setText(integrante.getEmail());
        viewHolder.txt_telefonoIntegrante.setText(integrante.getTelefono());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                enviarIntegrante(integrante);

            }
        });

    }

    @Override
    public int getItemCount() {
        return integrantesList.size();
    }

    @Override
    public void onClick(View view) {

    }

    public void enviarIntegrante(MIntegrante integrante) {

        //Aquí se realiza la lógica necesaria para poder realizar el envio
        DetalleIntegranteFragment detalleIntegranteFragment = new DetalleIntegranteFragment();

        //Objeto bundle para transportar la información
        Bundle bundleEnvio = new Bundle();

        //Enviar el objeto que está llegando con Serializable
        bundleEnvio.putSerializable("objetoIntegrante", integrante);

        detalleIntegranteFragment.setArguments(bundleEnvio);

        //abrir fragment

        fragmentManager = ((AppCompatActivity) contexto).getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, detalleIntegranteFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

}
