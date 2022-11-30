package com.codinginflow.customspinnerexample;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.secureapp.Modelo.MGrupo;
import com.example.secureapp.R;

import java.util.ArrayList;


public class AdapterListaGrupos extends ArrayAdapter<MGrupo> {

    public AdapterListaGrupos(Context context, ArrayList<MGrupo> listaGrupos) {
        super(context, 0, listaGrupos);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.lista_grupos, parent, false
            );
        }

        ImageView imageViewFlag = convertView.findViewById(R.id.RV_imagen_grupo);
        TextView nombreGrupo = convertView.findViewById(R.id.txt_nombreGrupo);
        TextView descripcionGrupo = convertView.findViewById(R.id.txt_descripcionGrupo);


        MGrupo grupo = getItem(position);

        if (grupo != null) {
            imageViewFlag.setImageResource(R.drawable.escudoamerica);
            nombreGrupo.setText(grupo.getNombre());
            descripcionGrupo.setText(grupo.getDescripcion());
        }

        return convertView;
    }
}