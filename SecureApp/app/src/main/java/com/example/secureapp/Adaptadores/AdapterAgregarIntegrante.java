package com.example.secureapp.Adaptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secureapp.Activities.Globales;
import com.example.secureapp.Fragments.AgregarIntegranteFragment;
import com.example.secureapp.Fragments.DetalleGrupoFragment;
import com.example.secureapp.Modelo.MAgregarIntegrante;
import com.example.secureapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AdapterAgregarIntegrante extends RecyclerView.Adapter<AdapterAgregarIntegrante.ViewHolder> implements View.OnClickListener {

    private int resource;
    LayoutInflater inflater;
    ArrayList<MAgregarIntegrante> agregarIntegrantesList;

    private boolean itemSeleccionado = false;
    private ArrayList<MAgregarIntegrante> itemsSeleccionados = new ArrayList<>();
    private ArrayList<String> emailSeleccionados = new ArrayList<>();

    //Listener
    private View.OnClickListener listener;

    //variables para cargar el fragment principal
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    Context contexto;

    private FloatingActionButton btn_agregarIntegrantes;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            private TextView txt_nombreAgregarIntegrante, txt_apellidoAgregarIntegrante,
                    txt_emailAgregarIntegrante, txt_telefonoAgregarIntegrante;
            private ImageView imageDetalle;
            private FloatingActionButton btn_agregarIntegrantes;
            public View view;

            private View.OnClickListener listener;

            public ViewHolder(View view){
                super(view);

                this.view = view;
                this.txt_nombreAgregarIntegrante = view.findViewById(R.id.txt_nombreAgregarIntegrante);
                this.txt_apellidoAgregarIntegrante = view.findViewById(R.id.txt_apellidoAgregarIntegrante);
                this.txt_emailAgregarIntegrante = view.findViewById(R.id.txt_emailAgregarIntegrante);
                this.txt_telefonoAgregarIntegrante = view.findViewById(R.id.txt_telefonoAgregarIntegrante);
                //this.imageDetalle = view.findViewById(R.id.imagen_contacto);

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

    public AdapterAgregarIntegrante(ArrayList<MAgregarIntegrante> agregarIntegrantesList, int resource){

        this.agregarIntegrantesList = agregarIntegrantesList;
        this.resource = resource;

    }

    public AdapterAgregarIntegrante() {}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);

        view.setOnClickListener(this);

        contexto = parent.getContext();
        //btn_agregarIntegrantes = view.findViewById(R.id.btn_agregarIntegrantes);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {

        MAgregarIntegrante agregarIntegrante = agregarIntegrantesList.get(position);

        viewHolder.txt_nombreAgregarIntegrante.setText(agregarIntegrante.getNombre());
        viewHolder.txt_apellidoAgregarIntegrante.setText(agregarIntegrante.getApellido());
        viewHolder.txt_emailAgregarIntegrante.setText(agregarIntegrante.getEmail());
        viewHolder.txt_telefonoAgregarIntegrante.setText(agregarIntegrante.getTelefono());
        //viewHolder.imageDetalle.setImageDrawable(contacto.ge);

        //Objeto bundle para transportar la información
        Bundle bundleEnvio = new Bundle();

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public boolean onLongClick(View view) {

                itemSeleccionado = true;

                if (itemsSeleccionados.contains(agregarIntegrantesList.get(position))){
                viewHolder.itemView.setBackgroundColor(Color.TRANSPARENT);
                itemsSeleccionados.remove(agregarIntegrantesList.get(position));
                emailSeleccionados.remove(agregarIntegrante.getEmail());
                }else{
                    viewHolder.itemView.setBackgroundColor(Color.parseColor("#DADBFF"));
                    itemsSeleccionados.add(agregarIntegrantesList.get(position));
                    emailSeleccionados.add(agregarIntegrante.getEmail());

                }
                if (itemsSeleccionados.size() == 0)
                    itemSeleccionado = false;
                    return true;

            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (itemSeleccionado){
                    if (itemsSeleccionados.contains(agregarIntegrantesList.get(position))){
                        viewHolder.itemView.setBackgroundColor(Color.TRANSPARENT);
                        itemsSeleccionados.remove(agregarIntegrantesList.get(position));
                        emailSeleccionados.remove(agregarIntegrante.getEmail());
                    }else{
                        viewHolder.itemView.setBackgroundColor(Color.parseColor("#DADBFF"));
                        itemsSeleccionados.add(agregarIntegrantesList.get(position));
                        emailSeleccionados.add(agregarIntegrante.getEmail());

                    }
                    if (itemsSeleccionados.size() == 0)
                        itemSeleccionado = false;
                }
                else {}

            }

        });

        Globales.itemsSeleccionados = itemsSeleccionados;
        Globales.emailSeleccionados = emailSeleccionados;

    }

    @Override
    public int getItemCount() {
        return agregarIntegrantesList.size();
    }

    @Override
    public void onClick(View view) {

    }

    public void enviarContacto(MAgregarIntegrante agregarIntegrante) {

    //Aquí se realiza la lógica necesaria para poder realizar el envio
    DetalleGrupoFragment detalleGrupoFragment = new DetalleGrupoFragment();

    //Objeto bundle para transportar la información
    //Bundle bundleEnvio = new Bundle();

    //Enviar el objeto que está llegando con Serializable
    //bundleEnvio.putSerializable("objetoGrupo", detalleGrupoFragment);

    //detalleGrupoFragment.setArguments(bundleEnvio);

    //abrir fragment

        fragmentManager = ((AppCompatActivity) contexto).getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, detalleGrupoFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    private void irADetalleGrupo(){

        fragmentManager = ((AppCompatActivity) contexto).getSupportFragmentManager();
        fragmentManager.popBackStack();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.commit();

    }

    public ArrayList<MAgregarIntegrante> getItemsSeleccionados() {
        return itemsSeleccionados;
    }

    public void setItemsSeleccionados(ArrayList<MAgregarIntegrante> itemsSeleccionados) {
        this.itemsSeleccionados = itemsSeleccionados;
    }
}
