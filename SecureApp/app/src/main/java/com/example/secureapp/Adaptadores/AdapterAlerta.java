package com.example.secureapp.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.secureapp.Modelo.MAlerta;
import com.example.secureapp.Modelo.MContacto;
import com.example.secureapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AdapterAlerta extends RecyclerView.Adapter<AdapterAlerta.ViewHolder> implements View.OnClickListener {

    private final int resource;
    private View.OnClickListener listener;
    private LayoutInflater inflater;
    private FirebaseFirestore firestore;
    ArrayList<MAlerta> alertaList;


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView txt_descripcionAlerta;
        public View view;

        private View.OnClickListener listener;


        public ViewHolder(View view){
            super(view);

            this.view = view;
            this.txt_descripcionAlerta = view.findViewById(R.id.txt_descripcionAlerta);

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




    //Listener
    //private View.OnClickListener listener;

    public AdapterAlerta(ArrayList<MAlerta> alertaList, int resource){

        this.alertaList = alertaList;
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

        MAlerta alerta = alertaList.get(index);

        viewHolder.txt_descripcionAlerta.setText(alerta.getDescripción());

    }

    @Override
    public int getItemCount() {

        return alertaList.size();
    }


    @Override
    public void onClick(View view) {



        }


    }




