package com.example.secureapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.secureapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class OlvidarPasswordActivity extends AppCompatActivity {

    EditText et_emailRecuperarPassword;
    Button btn_recuperarPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olvidar_password);

        et_emailRecuperarPassword = findViewById(R.id.et_emailRecuperarPassword);
        btn_recuperarPassword = findViewById(R.id.btn_recuperarPassword);

        btn_recuperarPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validate();

            }
        });
    }

    private void validate() {

        String email = et_emailRecuperarPassword.getText().toString();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            et_emailRecuperarPassword.setError("Correo invalido");
            return;
        }

        sendEmail(email);

    }

    private void sendEmail(String email) {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAdress = email;
        auth.sendPasswordResetEmail(emailAdress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){

                            Toast.makeText(OlvidarPasswordActivity.this, "Correo enviado", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(OlvidarPasswordActivity.this, InicioSesionActivity.class);
                            startActivity(intent);
                        }else{

                            Toast.makeText(OlvidarPasswordActivity.this, "Correo invalido o inexistente", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(OlvidarPasswordActivity.this, InicioSesionActivity.class);
        startActivity(intent);
        finish();
    }
}