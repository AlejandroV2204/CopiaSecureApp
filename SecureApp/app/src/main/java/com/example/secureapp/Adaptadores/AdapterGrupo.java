package com.example.secureapp.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secureapp.Entidades.Grupo;
import com.example.secureapp.R;

import java.util.ArrayList;

public class AdapterGrupo extends RecyclerView.Adapter<AdapterGrupo.ViewHolder> implements View.OnClickListener {

    LayoutInflater inflater;
    ArrayList<Grupo> model;

    //Listener
    private View.OnClickListener listener;

    public AdapterGrupo(Context context, ArrayList<Grupo> model){

        this.inflater = LayoutInflater.from(context);
        this.model = model;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.lista_grupos, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);

    }

    public void  setOnClickListener(View.OnClickListener listener){

        this.listener = listener;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String nombre = model.get(position).getNombre();
        String descripcion = model.get(position).getDescripcion();
        int imagen = model.get(position).getImagenid();

        holder.nombre.setText(nombre);
        holder.descripcion.setText(descripcion);
        holder.imagen.setImageResource(imagen);

    }

    @Override
    public int getItemCount() {
        return model.size();
    }


    @Override
    public void onClick(View view) {

        if (listener  != null){

            listener.onClick(view);

        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nombre, descripcion;
        ImageView imagen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nombre = itemView.findViewById(R.id.txt_nombreGrupo);
            descripcion = itemView.findViewById(R.id.txt_descripcionGrupo);
            imagen = itemView.findViewById(R.id.imagen_grupo);

        }
    }
}
