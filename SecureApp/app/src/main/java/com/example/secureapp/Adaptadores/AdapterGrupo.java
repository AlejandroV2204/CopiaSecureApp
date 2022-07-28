package com.example.secureapp.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secureapp.Interfaces.IComunicaFragments;
import com.example.secureapp.Modelo.MGrupo;
import com.example.secureapp.R;

import java.util.ArrayList;

public class AdapterGrupo extends RecyclerView.Adapter<AdapterGrupo.ViewHolder> implements View.OnClickListener {

    private final int resource;
    private View.OnClickListener listener;

    LayoutInflater inflater;
    ArrayList<MGrupo> gruposList;
    IComunicaFragments iComunicaFragments;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView txt_nombreGrupo, txt_descripcionGrupo;
        public View view;

        private View.OnClickListener listener;

        public ViewHolder(View view){
            super(view);

            this.view = view;
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

    public AdapterGrupo(ArrayList<MGrupo> gruposList, int resource, IComunicaFragments iComunicaFragments){

        this.gruposList = gruposList;
        this.resource = resource;
        this.iComunicaFragments = iComunicaFragments;

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

        MGrupo grupo = gruposList.get(index);

        viewHolder.txt_nombreGrupo.setText(grupo.getNombre());
        viewHolder.txt_descripcionGrupo.setText(grupo.getDescripcion());


    }

    @Override
    public int getItemCount() {
        return gruposList.size();
    }


    @Override
    public void onClick(View view) {



    }

}




