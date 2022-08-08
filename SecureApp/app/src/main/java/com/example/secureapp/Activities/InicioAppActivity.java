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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_app);

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

}