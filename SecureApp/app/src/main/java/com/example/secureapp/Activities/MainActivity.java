package com.example.secureapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.secureapp.Adaptadores.AdapterAlerta;
import com.example.secureapp.Adaptadores.AdapterGrupo;

import com.example.secureapp.Fragments.ContactoFragment;
import com.example.secureapp.Fragments.DetalleContactoFragment;
import com.example.secureapp.Fragments.NuevoContactoFragment;

import com.example.secureapp.Modelo.MAlerta;
import com.example.secureapp.Modelo.MGrupo;
import com.example.secureapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import com.example.secureapp.Fragments.AlertaFragment;
import com.example.secureapp.Fragments.DetalleGrupoFragment;
import com.example.secureapp.Fragments.GrupoFragment;
import com.example.secureapp.Fragments.MainFragment;
import com.example.secureapp.Fragments.NuevoGrupoFragment;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessagingService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;

    //variables para cargar el fragment principal
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    //variable del fragment detalle grupo
    DetalleGrupoFragment detalleGrupoFragment;

    //variable del fragment detalle contacto
    DetalleContactoFragment detalleContactoFragment;

    ArrayList listaGrupos;
    RecyclerView recyclerGrupos;

    private FirebaseFirestore firestore;

    private String identificadorGrupo;

    private ArrayList<String> codigoAlerta = new ArrayList<String>();

    int REQUEST_CODE = 200;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializarFireStore();
        verificarPermisos();
        verificarGruposIntegrados();


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);

        //establecer evento onClick al navigationView
        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        //cargar fragment principal
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, new MainFragment());
        fragmentTransaction.commit();

    }

    private void inicializarFireStore(){

        FirebaseApp.initializeApp(this);
        firestore = FirebaseFirestore.getInstance();

    }


    //Se controla la pulsación del botón atrás.
    @Override
    public void onBackPressed(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("¿Deseas salir de SecureApp?")
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void verificarPermisos() {

        int permisoUbicacion = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permisoUbicacionExacta = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int permisoInternet = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);

        if (permisoUbicacion == PackageManager.PERMISSION_GRANTED && permisoUbicacionExacta == PackageManager.PERMISSION_GRANTED && permisoInternet == PackageManager.PERMISSION_GRANTED){

            Toast.makeText(this, "Permiso de ubicación concecido", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Permiso de conexión a internet concecido", Toast.LENGTH_SHORT).show();


        }else{

            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, REQUEST_CODE);

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        drawerLayout.closeDrawer(GravityCompat.START);

        if (item.getItemId() == R.id.inicio){

            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new MainFragment());
            fragmentTransaction.commit();

        }

        if (item.getItemId() == R.id.contactos){

            fragmentManager = getSupportFragmentManager();

            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new ContactoFragment());
            fragmentTransaction.commit();

        }

        if (item.getItemId() == R.id.nuevoContacto){

            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new NuevoContactoFragment());
            fragmentTransaction.commit();

        }

        if (item.getItemId() == R.id.grupo){

            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new GrupoFragment());
            fragmentTransaction.commit();

        }

        if (item.getItemId() == R.id.nuevoGrupo){

            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new NuevoGrupoFragment());
            fragmentTransaction.commit();

        }

        if (item.getItemId() == R.id.alerta){

            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new AlertaFragment());
            fragmentTransaction.commit();

        }

        if (item.getItemId() == R.id.cerrarSesion){

            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
            irAinicioSesion();

        }

        return false;
    }

    private void irAinicioSesion() {

        Intent intent = new Intent(MainActivity.this, InicioSesionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    private void verificarGruposIntegrados(){

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        firestore.collection("grupo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                identificadorGrupo = document.getId();

                                firestore.collection("grupo").document(identificadorGrupo).collection("integrantes")
                                        .whereEqualTo("email", email)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                                        String identificadorGrupoDos = document.getString("identificadorGrupo");
                                                        //Toast.makeText(getContext(), "" + identificadorGrupo, Toast.LENGTH_SHORT).show();

                                                        DocumentReference grupoRef = firestore.collection("grupo").document(identificadorGrupoDos);
                                                        grupoRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                if (task.isSuccessful()) {
                                                                    DocumentSnapshot document = task.getResult();
                                                                    if (document.exists()) {

                                                                        String identificadorGrupoConsulta = document.getString("identificador");
                                                                        String nombreGrupo = document.getString("nombre");
                                                                        String descripcionGrupo = document.getString("descripcion");
                                                                        String administradorGrupo = document.getString("administrador");
                                                                        String emailAdministrador = document.getString("emailAdministrador");
                                                                        String fechaCreacion = document.getString("fechaCreacion");
                                                                        GeoPoint localizacion = document.getGeoPoint("localizacion");

                                                                        HashMap<String, Object> grupo = new HashMap<>();
                                                                        grupo.put("identificador", identificadorGrupoConsulta);
                                                                        grupo.put("nombre", nombreGrupo);
                                                                        grupo.put("descripcion", descripcionGrupo);
                                                                        grupo.put("administrador", administradorGrupo);
                                                                        grupo.put("emailAdministrador", emailAdministrador);
                                                                        grupo.put("fechaCreacion", fechaCreacion);
                                                                        grupo.put("localizacion", localizacion);

                                                                        firestore.collection("usuario").document(email).collection("grupos").document(identificadorGrupoConsulta)
                                                                                .set(grupo)
                                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void aVoid) {

                                                                                        //Toast.makeText(getContext(), "Grupo agregado exitosamente al usuario", Toast.LENGTH_SHORT).show();

                                                                                    }
                                                                                })
                                                                                .addOnFailureListener(new OnFailureListener() {
                                                                                    @Override
                                                                                    public void onFailure(@NonNull Exception e) {

                                                                                        Toast.makeText(getApplicationContext(), "Error en la integración del grupo al usuario", Toast.LENGTH_SHORT).show();

                                                                                    }
                                                                                });

                                                                        firestore.collection("grupo").document(identificadorGrupo).collection("integrantes")
                                                                                .get()
                                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                        if (task.isSuccessful()) {
                                                                                            for (QueryDocumentSnapshot document : task.getResult()) {

                                                                                                String nombreIntegrante = document.getString("nombre");
                                                                                                String apellidoIntegrante = document.getString("apellido");
                                                                                                String emailIntegrante = document.getString("email");
                                                                                                String telefonoIntegrante = document.getString("telefono");
                                                                                                String documentoIntegrante = document.getString("identificadorGrupo");

                                                                                                HashMap<String, Object> integrante = new HashMap<>();
                                                                                                integrante.put("nombre", nombreIntegrante);
                                                                                                integrante.put("apellido", apellidoIntegrante);
                                                                                                integrante.put("email", emailIntegrante);
                                                                                                integrante.put("telefono", telefonoIntegrante);
                                                                                                integrante.put("identificadorGrupo", documentoIntegrante);

                                                                                                firestore.collection("usuario").document(email).collection("grupos").document(identificadorGrupo).collection("integrantes").document(email)
                                                                                                        .set(integrante)
                                                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                            @Override
                                                                                                            public void onSuccess(Void aVoid) {

                                                                                                                //Toast.makeText(getActivity(), "Usuario creado exitosamente", Toast.LENGTH_SHORT).show();

                                                                                                            }
                                                                                                        })
                                                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                                                            @Override
                                                                                                            public void onFailure(@NonNull Exception e) {

                                                                                                                Toast.makeText(getApplicationContext(), "Error en el registro", Toast.LENGTH_SHORT).show();

                                                                                                            }
                                                                                                        });

                                                                                            }

                                                                                        } else {
                                                                                            Toast.makeText(getApplicationContext(), "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    }
                                                                                });

                                                                    } else {

                                                                        Toast.makeText(getApplicationContext(), "No such document", Toast.LENGTH_SHORT).show();

                                                                    }
                                                                } else {

                                                                    Toast.makeText(getApplicationContext(), "get failed with " + task.getException(), Toast.LENGTH_SHORT).show();

                                                                }
                                                            }
                                                        });

                                                    }

                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    private void verificarAlertasPropias(){

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        firestore.collection("usuario").document(email).collection("alertas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                codigoAlerta.add(document.getString("codigo"));

                                firestore.collection("alerta")
                                        .whereNotIn("codigo", codigoAlerta)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                                        String identificadorAlerta = document.getId();
                                                        String nombreAlerta = document.getString("nombre");
                                                        String codigoAlerta = document.getString("codigo");
                                                        String descripcionAlerta = document.getString("descripcion");
                                                        boolean alertaFavorita = false;

                                                        HashMap<String, Object> alerta = new HashMap<>();
                                                        alerta.put("identificador", identificadorAlerta);
                                                        alerta.put("nombre", nombreAlerta);
                                                        alerta.put("codigo", codigoAlerta);
                                                        alerta.put("descripcion", descripcionAlerta);
                                                        alerta.put("favorita", alertaFavorita);

                                                        firestore.collection("usuario").document(email).collection("alertas").document(identificadorAlerta)
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

                                                                        Toast.makeText(getApplicationContext(), "Error en la integración del grupo al usuario", Toast.LENGTH_SHORT).show();

                                                                    }
                                                                });

                                                    }

                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

}

