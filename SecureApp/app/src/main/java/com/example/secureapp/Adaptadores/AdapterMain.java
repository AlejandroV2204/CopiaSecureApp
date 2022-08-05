package com.example.secureapp.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secureapp.Modelo.MAlerta;
import com.example.secureapp.Modelo.MMain;
import com.example.secureapp.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AdapterMain extends RecyclerView.Adapter<AdapterMain.ViewHolder> implements View.OnClickListener {

    private final int resource;
    private View.OnClickListener listener;
    private LayoutInflater inflater;
    private FirebaseFirestore firestore;
    ArrayList<MMain> mainList;


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

    public AdapterMain(ArrayList<MMain> mainList, int resource){

        this.mainList = mainList;
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

        MMain main = mainList.get(index);

        viewHolder.txt_descripcionAlerta.setText(main.getDescripción());

    }

    @Override
    public int getItemCount() {

        return mainList.size();
    }


    @Override
    public void onClick(View view) {



        }


    }




