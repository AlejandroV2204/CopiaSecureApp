package com.example.secureapp.Activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.secureapp.Adaptadores.AdapterContacto;
import com.example.secureapp.Fragments.AjustesFragment;
import com.example.secureapp.Fragments.ContactoFragment;
import com.example.secureapp.Fragments.DetalleContactoFragment;
import com.example.secureapp.Fragments.NuevoContactoFragment;

import com.example.secureapp.Modelo.MContacto;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    SwipeRefreshLayout swipeRefreshLayout;
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

    private ImageView img_fotoPersona;
    private TextView txt_nombrePersona;

    Context context;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializarFireStore();
        traerNombreUsuario();
        verificarPermisos();
        extraerToken();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer);
        swipeRefreshLayout = findViewById(R.id.swipe);
        navigationView = findViewById(R.id.navigationView);

        //establecer evento onClick al navigationView
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        txt_nombrePersona = (TextView) headerView.findViewById(R.id.txt_headerTexto);
        img_fotoPersona = (ImageView) headerView.findViewById(R.id.imagen_fotoPersona);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        //cargar fragment principal
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, new MainFragment());
        fragmentTransaction.commit();

        context = this.getApplicationContext();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                reiniciarActivity(MainActivity.this);

                Toast.makeText(MainActivity.this, "Refreshed.", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);

            }
        });

        img_fotoPersona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                irAAjustes();

            }
        });

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

    //reinicia una Activity
    public static void reiniciarActivity(Activity actividad){
        Intent intent = new Intent();
        intent.setClass(actividad, actividad.getClass());
        //llamamos a la actividad
        actividad.startActivity(intent);
        //finalizamos la actividad actual
        actividad.finish();

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

        firestore.collection("usuario").document(email)
                .update(tokenAlerta)
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

    private void traerNombreUsuario(){

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();

        DocumentReference usuarioRef = firestore.collection("usuario").document(email);
        usuarioRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        //Toast.makeText(getContext(), "DocumentSnapshot data:\n " + document.getData(), Toast.LENGTH_SHORT).show();
                        String nombrePropio = document.getString("nombre");
                        String apellidoPropio = document.getString("apellido");

                        txt_nombrePersona.setText(nombrePropio + " " + apellidoPropio);

                    } else {

                        Toast.makeText(getApplicationContext(), "get failed with " + task.getException(), Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
    }

    private void irAAjustes(){

        drawerLayout.closeDrawer(GravityCompat.START);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, new AjustesFragment());
        fragmentTransaction.commit();

    }

}