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
import com.example.secureapp.Modelo.MAlerta;
import com.example.secureapp.Modelo.MMain;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdapterMain extends RecyclerView.Adapter<AdapterMain.ViewHolder> implements View.OnClickListener {

    private final int resource;
    private TextView txt_descripcionAlerta;
    private View.OnClickListener listener;
    private LayoutInflater inflater;
    private FirebaseFirestore firestore;
    ArrayList<MMain> mainList;

    Context contexto;

    private String codigoAlerta;
    private String descripcionAlerta;
    private String nombreUsuario;

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

        contexto = parent.getContext();

        inicializarFireStore();

        return new ViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int index) {

        MMain main = mainList.get(index);

        viewHolder.txt_descripcionAlerta.setText(main.getDescripción());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                descripcionAlerta = main.getDescripción();
                tomarNombreUsuario();

            }
        });

    }

    @Override
    public int getItemCount() {

        return mainList.size();
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

                                llamartopico(nombreUsuario, descripcionAlerta);

                            }


                        } else {
                            Toast.makeText(contexto, "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void llamartopico(String nombreUsuario, String descripcionAlerta) {

        RequestQueue myrequest= Volley.newRequestQueue(contexto);
        JSONObject json = new JSONObject();

        try {

            String url_foto="https://uploadgerencie.com/imagenes/notificacion-conducta-concluyente.png";

            // String token="cIb2ajMbQ7mtXBSV-rsHHW:APA91bEmqMrRYqHNFwWTTjrODwfkQLf4Kg0-5Pnf2A7OrLgQqn2yM7zdED2dc2Q7tSnQhhxslc0lqQOx8yDQl05QaCgy1lcuhv-kl-YOScfmmsD_0rg1j6kimDqkMSydGaBvqEval-1P";
            // "cIb2ajMbQ7mtXBSV-rsHHW:APA91bEmqMrRYqHNFwWTTjrODwfkQLf4Kg0-5Pnf2A7OrLgQqn2yM7zdED2dc2Q7tSnQhhxslc0lqQOx8yDQl05QaCgy1lcuhv-kl-YOScfmmsD_0rg1j6kimDqkMSydGaBvqEval-1P"
            json.put("to","/topics/"+"enviaratodos");
            JSONObject notificacion=new JSONObject();
            notificacion.put("titulo",nombreUsuario);
            notificacion.put("detalle",descripcionAlerta);
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
    }


    }




