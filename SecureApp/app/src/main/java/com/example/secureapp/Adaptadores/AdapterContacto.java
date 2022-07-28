package com.example.secureapp.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secureapp.Modelo.MContacto;
import com.example.secureapp.R;

import java.util.ArrayList;

public class AdapterContacto extends RecyclerView.Adapter<AdapterContacto.ViewHolder> implements View.OnClickListener  {

    private final int resource;
    LayoutInflater inflater;
    ArrayList<MContacto> contactosList;

    //Listener
    private View.OnClickListener listener;


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            private TextView txt_nombreContacto, txt_apellidoContacto, txt_emailContacto, txt_telefonoContacto;
            public View view;

            private View.OnClickListener listener;

            public ViewHolder(View view){
                super(view);

                this.view = view;
                this.txt_nombreContacto = view.findViewById(R.id.txt_nombreIntegrante);
                this.txt_apellidoContacto = view.findViewById(R.id.txt_apellidoIntegrante);
                this.txt_emailContacto = view.findViewById(R.id.txt_emailContacto);
                this.txt_telefonoContacto = view.findViewById(R.id.txt_telefonoContacto);

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

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int index) {

        MContacto contacto = contactosList.get(index);

        viewHolder.txt_nombreContacto.setText(contacto.getNombre());
        viewHolder.txt_apellidoContacto.setText(contacto.getApellido());
        viewHolder.txt_emailContacto.setText(contacto.getEmail());
        viewHolder.txt_telefonoContacto.setText(contacto.getTelefono());

    }

    @Override
    public int getItemCount() {
        return contactosList.size();
    }

    @Override
    public void onClick(View view) {

    }

}
