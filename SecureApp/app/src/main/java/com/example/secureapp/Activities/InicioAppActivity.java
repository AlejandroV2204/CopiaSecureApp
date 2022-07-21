package com.example.secureapp.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.example.secureapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;
import java.util.TimerTask;

public class InicioAppActivity extends AppCompatActivity {

    int REQUEST_CODE = 200;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_app);

        verificarPermisos();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {

                Intent intent = new Intent(InicioAppActivity.this, InicioSesionActivity.class);
                startActivity(intent);
                finish();

            }
        };

        Timer tiempo = new Timer();
        tiempo.schedule(timerTask, 3000);
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
}