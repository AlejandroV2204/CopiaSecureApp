package com.example.secureapp.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secureapp.Entidades.Contacto;
import com.example.secureapp.Entidades.Grupo;
import com.example.secureapp.R;

import java.util.ArrayList;

public class AdapterContacto extends RecyclerView.Adapter<AdapterContacto.ViewHolder> implements View.OnClickListener {

    LayoutInflater inflater;
    ArrayList<Contacto> model;

    //Listener
    private View.OnClickListener listener;

    public AdapterContacto(Context context, ArrayList<Contacto> model){

        this.inflater = LayoutInflater.from(context);
        this.model = model;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.lista_contactos, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);

    }

    public void  setOnClickListener(View.OnClickListener listener){

        this.listener = listener;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String nombre = model.get(position).getNombre();
        String telefono = model.get(position).getTelefono();
        int imagen = model.get(position).getImagenid();

        holder.nombre.setText(nombre);
        holder.telefono.setText(telefono);
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

        TextView nombre, telefono;
        ImageView imagen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nombre = itemView.findViewById(R.id.txt_nombreContacto);
            telefono = itemView.findViewById(R.id.txt_telefonoContacto);
            imagen = itemView.findViewById(R.id.imagen_contacto);

        }
    }
}
