package com.example.secureapp.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            private TextView txt_nombreIntegrante, txt_apellidoIntegrante, txt_emailIntegrante, txt_telefonoIntegrante;
            public View view;

            private View.OnClickListener listener;

            public ViewHolder(View view){
                super(view);

                this.view = view;
                this.txt_nombreIntegrante = view.findViewById(R.id.txt_nombreIntegrante);
                this.txt_apellidoIntegrante = view.findViewById(R.id.txt_apellidoIntegrante);
                this.txt_emailIntegrante = view.findViewById(R.id.txt_emailContacto);
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

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int index) {

        MIntegrante integrante = integrantesList.get(index);

        viewHolder.txt_nombreIntegrante.setText(integrante.getNombre());
        viewHolder.txt_apellidoIntegrante.setText(integrante.getApellido());
        viewHolder.txt_emailIntegrante.setText(integrante.getEmail());
        viewHolder.txt_telefonoIntegrante.setText(integrante.getTelefono());

    }

    @Override
    public int getItemCount() {
        return integrantesList.size();
    }

    @Override
    public void onClick(View view) {

    }

}
