package com.example.secureapp.Activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.secureapp.Fragments.ContactoFragment;
import com.example.secureapp.Fragments.DetalleContactoFragment;
import com.example.secureapp.Fragments.NuevoContactoFragment;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;

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
    private String token;

    int REQUEST_CODE = 200;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializarFireStore();
        verificarPermisos();
        extraerToken();

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

    private void extraerToken(){

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        token = task.getResult();

                        insertarTokenUsuario(token);

                    }
                });
    }

    private void insertarTokenUsuario(String token){

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        this.token = token;

        HashMap<String, Object> tokenAlerta = new HashMap<>();
        tokenAlerta.put("tokenAlerta", token);

        firestore.collection("usuario").document(email).collection("alertas").document("tokenAlerta")
                .set(tokenAlerta)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        //Toast.makeText(getContext(), "Grupo agregado exitosamente al usuario", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(MainActivity.this, "Error en la inserción del token al usuario", Toast.LENGTH_SHORT).show();

                    }
                });

    }

}

