package com.example.secureapp.Adaptadores;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.example.secureapp.Activities.MainActivity;
import com.example.secureapp.Entidades.Grupo;
import com.example.secureapp.Fragments.AlertaFragment;
import com.example.secureapp.Fragments.DetalleGrupoFragment;
import com.example.secureapp.Fragments.DetalleIntegranteFragment;
import com.example.secureapp.Interfaces.IComunicaFragments;
import com.example.secureapp.Modelo.MGrupo;
import com.example.secureapp.R;

import java.util.ArrayList;

public class AdapterGrupo extends RecyclerView.Adapter<AdapterGrupo.ViewHolder> implements View.OnClickListener{

    private final int resource;
    private View.OnClickListener listener;

    LayoutInflater inflater;
    ArrayList<MGrupo> gruposList;
    IComunicaFragments iComunicaFragments;

    //variables para cargar el fragment principal
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    Context contexto;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView txt_nombreGrupo, txt_descripcionGrupo;
        private TextView txt_identificadorGrupo;
        public View view;

        private View.OnClickListener listener;

        public ViewHolder(View view){
            super(view);

            this.view = view;
            //this.txt_identificadorGrupo = view.findViewById(R.id.);
            this.txt_nombreGrupo = view.findViewById(R.id.txt_nombreGrupo);
            this.txt_descripcionGrupo = view.findViewById(R.id.txt_descripcionGrupo);


        }

        public void setOnClickListener(View.OnClickListener listener){

            this.listener = listener;

        }

        @Override
        public void onClick(View view) {

            if (listener != null){

                listener.onClick(view);

            }

        }
    }


    //Listener
    //private View.OnClickListener listener;

    public AdapterGrupo(ArrayList<MGrupo> gruposList, int resource){

        this.gruposList = gruposList;
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

        MGrupo grupo = gruposList.get(position);

        //viewHolder.txt_identificadorGrupo.setText(grupo.getIdentificador());
        viewHolder.txt_nombreGrupo.setText(grupo.getNombre());
        viewHolder.txt_descripcionGrupo.setText(grupo.getDescripcion());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                enviarGrupo(grupo);

            }
        });


    }

    @Override
    public int getItemCount() {
        return gruposList.size();
    }


    @Override
    public void onClick(View view) {}

    public void enviarGrupo(MGrupo grupo) {

        //Aquí se realiza la lógica necesaria para poder realizar el envio
        DetalleGrupoFragment detalleGrupoFragment = new DetalleGrupoFragment();
        DetalleIntegranteFragment detalleIntegranteFragment = new DetalleIntegranteFragment();

        //Objeto bundle para transportar la información
        Bundle bundleEnvio = new Bundle();

        //Enviar el objeto que está llegando con Serializable
        bundleEnvio.putSerializable("objetoGrupo", grupo);

        detalleGrupoFragment.setArguments(bundleEnvio);
        detalleIntegranteFragment.setArguments(bundleEnvio);

        //abrir fragment
        fragmentManager = ((AppCompatActivity) contexto).getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, detalleGrupoFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    //private void RecyclerHolder extends RecyclerView.ViewHolder(){}

}




