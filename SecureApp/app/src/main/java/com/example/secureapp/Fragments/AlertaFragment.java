package com.example.secureapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.secureapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AlertaFragment extends Fragment{

    String tituloAlerta ="NECESITO UN MECANICO \uD83D\uDD27";

    Button btn_emitirAlerta;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alerta,container, false);

        inicializarFirebase();
        firebaseAuth = FirebaseAuth.getInstance();

        btn_emitirAlerta = view.findViewById(R.id.btn_emitirAlerta);

        String id = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

        databaseReference.child("usuario").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    String nombre = snapshot.child("nombre").getValue().toString();
                    tituloAlerta = nombre + tituloAlerta;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //imagen = (String) view.findViewById(R.drawable.secureapplogo);

        FirebaseMessaging.getInstance().subscribeToTopic("enviaratodos").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //Toast.makeText(getContext(),"Registrado",Toast.LENGTH_SHORT).show();
            }
        });


        btn_emitirAlerta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamartopico();
            }
        });

        return view;
    }

    private void inicializarFirebase(){

        FirebaseApp.initializeApp(getContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

    }

    private void llamartopico() {

        RequestQueue myrequest= Volley.newRequestQueue(getContext());
        JSONObject json = new JSONObject();

        try {

            String url_foto = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRUZWSYpZzrv8X3PWYAJJ5Ib_82G4zN4p_gQA&usqp=CAU";

            // String token="cIb2ajMbQ7mtXBSV-rsHHW:APA91bEmqMrRYqHNFwWTTjrODwfkQLf4Kg0-5Pnf2A7OrLgQqn2yM7zdED2dc2Q7tSnQhhxslc0lqQOx8yDQl05QaCgy1lcuhv-kl-YOScfmmsD_0rg1j6kimDqkMSydGaBvqEval-1P";
            // "cIb2ajMbQ7mtXBSV-rsHHW:APA91bEmqMrRYqHNFwWTTjrODwfkQLf4Kg0-5Pnf2A7OrLgQqn2yM7zdED2dc2Q7tSnQhhxslc0lqQOx8yDQl05QaCgy1lcuhv-kl-YOScfmmsD_0rg1j6kimDqkMSydGaBvqEval-1P"
            json.put("to","/topics/"+"enviaratodos");
            JSONObject notificacion = new JSONObject();
            notificacion.put("titulo", tituloAlerta);
            notificacion.put("foto",url_foto);

            json.put("data",notificacion);
            String URL="https://fcm.googleapis.com/fcm/send";
            JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST,URL,json,null,null){
                @Override
                public Map<String, String> getHeaders() {
                    Map<String,String>header=new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AAAA3B6j5bE:APA91bE7jmUJzdJAJU4xX9QJgC83hlbkMag_OM5eFq4JSmevJCQmttyRwjUSxKPiYWFZ1qmCWycuzUNnYsUOiQbKc6pRg8zzgbwwaqzKf48K_iBvrwO4a9QJ8jCqVTlXa5vVuHxtQh50");
                    return  header;

                }

            };
            myrequest.add(request);


        }catch (JSONException e){
            e.printStackTrace();
        }


        Toast.makeText(getActivity(), "Alerta enviada", Toast.LENGTH_SHORT).show();
        limpiarCampos();


    }

    private void limpiarCampos(){


    }



}


