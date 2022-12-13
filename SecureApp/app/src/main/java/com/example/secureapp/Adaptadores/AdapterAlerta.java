package com.example.secureapp.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.secureapp.Activities.Globales;
import com.example.secureapp.Fragments.AlertaFragment;
import com.example.secureapp.Modelo.MAlerta;
import com.example.secureapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdapterAlerta extends RecyclerView.Adapter<AdapterAlerta.ViewHolder> implements View.OnClickListener {

    private final int resource;
    private View.OnClickListener listener;
    private LayoutInflater inflater;
    private FirebaseFirestore firestore;
    private ArrayList<MAlerta> alertaList;
    private ArrayList<String> tokenUsuarios;

    Context contexto;

    private String codigoAlerta;
    private String descripcionAlerta;
    private String nombreUsuario;
    private boolean alertaFavorita, favoritaAlerta;
    private Switch switchAlerta;

    public AdapterAlerta(int resource) {

        this.resource = resource;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        //private TextView txt_nombreAlerta;
        private TextView txt_descripcionAlerta;
        private String codigoAlerta;
        private Switch switchAlerta;
        public View view;


        private View.OnClickListener listener;


        public ViewHolder(View view){
            super(view);

            this.view = view;
            this.txt_descripcionAlerta = view.findViewById(R.id.txt_descripcionAlerta);
            this.switchAlerta = view.findViewById(R.id.RV_switchAlerta);


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

        contexto = parent.getContext();

        inicializarFireStore();

        FirebaseMessaging.getInstance().subscribeToTopic("enviaratodos").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //Toast.makeText(contexto,"Registrado",Toast.LENGTH_SHORT).show();
            }
        });

        return new ViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int index) {

        MAlerta alerta = alertaList.get(index);

        //viewHolder.txt_nombreAlerta.setText(alerta.getNombre());
        viewHolder.txt_descripcionAlerta.setText(alerta.getDescripción());
        viewHolder.switchAlerta.setChecked(alerta.getFavorita());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                descripcionAlerta = alerta.getDescripción();
                AlertaFragment alertaFragment = new AlertaFragment();
                tokenUsuarios = Globales.tokenUsuarios;
                tomarNombreUsuario();

            }
        });

        viewHolder.switchAlerta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                descripcionAlerta = alerta.getDescripción();
                alertaFavorita = alerta.getFavorita();

                if (viewHolder.switchAlerta.isChecked()){
                    alertaFavorita = true;
                    alerta.setFavorita(alertaFavorita);
                }else {
                    alertaFavorita = false;
                    alerta.setFavorita(alertaFavorita);
                }

                alertaFavorita = alerta.getFavorita();
                alertaFavorito(descripcionAlerta, alertaFavorita);

            }
        });


    }

    @Override
    public int getItemCount() {

        return alertaList.size();
    }


    @Override
    public void onClick(View view) {


    }

    private void inicializarFireStore() {

        FirebaseApp.initializeApp(contexto);
        firestore = FirebaseFirestore.getInstance();

    }

    private void tomarNombreUsuario(){

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();

        DocumentReference docRef = firestore.collection("usuario").document(email);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        nombreUsuario = (String) document.get("nombre") + " " + document.getString("apellido");
                        consulta(nombreUsuario, descripcionAlerta);

                    } else {
                        Toast.makeText(contexto, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(contexto, "Error obteniendo los datos de " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void consulta(String nombreUsuario, String descripcionAlerta){

        this.descripcionAlerta = descripcionAlerta;
        this.nombreUsuario = nombreUsuario;

        firestore.collection("alerta")
                .whereEqualTo("descripcion", this.descripcionAlerta)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String nombreAlerta = document.getString("nombre");
                                String descripcionAlerta = document.getString("descripcion");

                                llamartopico(nombreUsuario, descripcionAlerta, tokenUsuarios);

                            }


                        } else {
                            Toast.makeText(contexto, "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void alertaFavorito(String descripcionAlerta, boolean alertaFavorita){

        this.descripcionAlerta = descripcionAlerta;
        this.alertaFavorita = alertaFavorita;

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        firestore.collection("usuario").document(email).collection("alertas")
                .whereEqualTo("descripcion", this.descripcionAlerta)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String codigoAlerta = document.getString("codigo");
                                String nombreAlerta = document.getString("nombre");
                                String descripcionAlerta = document.getString("descripcion");
                                String identificadorAlerta = document.getString("identificador");

                                HashMap<String, Object> alerta = new HashMap<>();
                                alerta.put("codigo", codigoAlerta);
                                alerta.put("nombre", nombreAlerta);
                                alerta.put("descripcion", descripcionAlerta);
                                alerta.put("identificador", identificadorAlerta);
                                alerta.put("favorita", alertaFavorita);

                                firestore.collection("usuario").document(email).collection("alertas").document(codigoAlerta)
                                        .set(alerta)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                //Toast.makeText(getContext(), "Grupo agregado exitosamente al usuario", Toast.LENGTH_SHORT).show();

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                                Toast.makeText(contexto, "Error en la integración del grupo al usuario", Toast.LENGTH_SHORT).show();

                                            }
                                        });

                            }


                        } else {
                            Toast.makeText(contexto, "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void llamartopico(String nombreUsuario, String descripcionAlerta, ArrayList<String>tokenUsuarios) {

        RequestQueue myrequest= Volley.newRequestQueue(contexto);

        JSONObject json = new JSONObject();

        try {

            String url_foto="https://uploadgerencie.com/imagenes/notificacion-conducta-concluyente.png";
            //String tokenUsuario = "c089vvpjRL-I37udknnT9m:APA91bFxeOxS271tVZcTWXI45U2SRzhTuX_3aAmk-fPkInTlaCYw2EIIvbhkVEheudZVoYOMUKmHb4A3T98SIhv07POQ19Fo7w3oPDQnI5RnfkGgRzdpdVh2P0UZWyc0szoPjbEksKyl";

            // String token="cIb2ajMbQ7mtXBSV-rsHHW:APA91bEmqMrRYqHNFwWTTjrODwfkQLf4Kg0-5Pnf2A7OrLgQqn2yM7zdED2dc2Q7tSnQhhxslc0lqQOx8yDQl05QaCgy1lcuhv-kl-YOScfmmsD_0rg1j6kimDqkMSydGaBvqEval-1P";
            // "cIb2ajMbQ7mtXBSV-rsHHW:APA91bEmqMrRYqHNFwWTTjrODwfkQLf4Kg0-5Pnf2A7OrLgQqn2yM7zdED2dc2Q7tSnQhhxslc0lqQOx8yDQl05QaCgy1lcuhv-kl-YOScfmmsD_0rg1j6kimDqkMSydGaBvqEval-1P"
            json.put("to",tokenUsuarios);
            JSONObject notificacion=new JSONObject();
            notificacion.put("titulo",nombreUsuario);
            notificacion.put("detalle",descripcionAlerta);
            notificacion.put("foto",url_foto);

            json.put("data",notificacion);
            String URL="https://fcm.googleapis.com/fcm/send";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,URL,json,null,null){
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
    }

    public void setTokenUsuarios(ArrayList<String> tokenUsuarios) {
        this.tokenUsuarios = tokenUsuarios;
    }
}




