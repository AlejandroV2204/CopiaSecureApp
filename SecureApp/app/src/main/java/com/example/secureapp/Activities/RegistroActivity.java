package com.example.secureapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.secureapp.Modelo.MUsuario;
import com.example.secureapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistroActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText et_nombre, et_apellido, et_telefono, et_email, et_password, et_identificacion;
    String nombre, apellido, identificacion, telefono, email, password, departamento, ciudad;
    Spinner spinner_departamentos, spinner_ciudades;
    Button btn_registrar;

    FirebaseAuth firebaseAuth;
    AwesomeValidation awesomeValidation;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //inicializarFirebase();
        inicializarFireStore();
        validarCampos();

        et_nombre = findViewById(R.id.et_nombreRegistro);
        et_apellido = findViewById(R.id.et_apellidoRegistro);
        et_telefono = findViewById(R.id.et_telefonoRegistro);
        et_email = findViewById(R.id.et_emailRegistro);
        et_password = findViewById(R.id.et_passwordRegistro);
        et_identificacion = findViewById(R.id.et_identificacionRegistro);
        spinner_departamentos = (Spinner) findViewById(R.id.spinner_departamentos);
        spinner_ciudades = (Spinner) findViewById(R.id.spinner_ciudades);
        btn_registrar = findViewById(R.id.btn_registrar);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.R_listaDepartamentos, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_departamentos.setAdapter(adapter);
        spinner_departamentos.setOnItemSelectedListener(this);

        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validarDatos();

            }
        });


    }

    private void inicializarFirebase(){

        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

    }

    private void inicializarFireStore(){

        FirebaseApp.initializeApp(this);
        firestore = FirebaseFirestore.getInstance();

    }

    private void validarCampos(){

        firebaseAuth = FirebaseAuth.getInstance();
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.et_emailRegistro, Patterns.EMAIL_ADDRESS, R.string.invalid_email);
        awesomeValidation.addValidation(this, R.id.et_passwordRegistro, ".{6,}", R.string.invalid_password);

        awesomeValidation.addValidation(this, R.id.et_nombreRegistro, ".{1,}", R.string.invalid_campo);
        awesomeValidation.addValidation(this, R.id.et_apellidoRegistro, ".{1,}", R.string.invalid_campo);
        awesomeValidation.addValidation(this, R.id.et_identificacionRegistro, ".{7,}", R.string.invalid_identificacion);
        awesomeValidation.addValidation(this, R.id.et_telefonoRegistro, ".{10,}", R.string.invalid_telefono);

    }

    private void validarDatos(){

        nombre = et_nombre.getText().toString();
        apellido = et_apellido.getText().toString();
        identificacion = et_identificacion.getText().toString();
        telefono = et_telefono.getText().toString();
        email = et_email.getText().toString();
        password = et_password.getText().toString();
        departamento = spinner_departamentos.getSelectedItem().toString();
        ciudad = spinner_ciudades.getSelectedItem().toString();

        if (awesomeValidation.validate()){

            registrarUsuario();

        }else {

            Toast.makeText(RegistroActivity.this, "Completa todos lo datos", Toast.LENGTH_SHORT).show();
        }

    }

    private void registrarUsuario() {

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    //registrarUsuarioRealTime();
                    registrarUsuarioFireStore();
                    //verificarEmail();
                    finish();

                }else {

                    String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                    dameToastdeerror(errorCode);

                }

            }
        });

    }

    private void registrarUsuarioRealTime(){

        MUsuario usuario = new MUsuario();
        usuario.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setIdentificacion(identificacion);
        usuario.setTelefono(telefono);
        usuario.setEmail(email);
        usuario.setPassword(password);
        usuario.setDepartamento(departamento);
        usuario.setCiudad(ciudad);

        databaseReference.child("usuario").child(usuario.getUid()).setValue(usuario);
    }

    private void registrarUsuarioFireStore() {

        MUsuario usuario = new MUsuario();
        usuario.setUid(email);
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setIdentificacion(identificacion);
        usuario.setTelefono(telefono);
        usuario.setEmail(email);
        usuario.setPassword(password);
        usuario.setDepartamento(departamento);
        usuario.setCiudad(ciudad);

        firestore.collection("usuario").document(email)
                .set(usuario)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(RegistroActivity.this, "Usuario creado exitosamente", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(RegistroActivity.this, "Error en el registro", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void verificarEmail(){

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        firebaseUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                Toast.makeText(RegistroActivity.this, "Correo de verificación de cuenta enviado, por favor revise su bandeja de entrada", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(RegistroActivity.this, "Error, no ha sido posible enviar email de verificación", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

        int [] ciudades = {R.array.R_listaAntioquia,R.array.R_listaValleDelCauca};

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, ciudades[position], android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_ciudades.setAdapter(adapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void dameToastdeerror(String error) {

        switch (error) {

            case "ERROR_INVALID_CUSTOM_TOKEN":
                Toast.makeText(RegistroActivity.this, "El formato del token personalizado es incorrecto. Por favor revise la documentación", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                Toast.makeText(RegistroActivity.this, "El token personalizado corresponde a una audiencia diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_CREDENTIAL":
                Toast.makeText(RegistroActivity.this, "La credencial de autenticación proporcionada tiene un formato incorrecto o ha caducado.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_EMAIL":
                Toast.makeText(RegistroActivity.this, "La dirección de correo electrónico está mal formateada.", Toast.LENGTH_LONG).show();
                et_email.setError("La dirección de correo electrónico está mal formateada.");
                et_email.requestFocus();
                break;

            case "ERROR_WRONG_PASSWORD":
                Toast.makeText(RegistroActivity.this, "La contraseña no es válida o el usuario no tiene contraseña.", Toast.LENGTH_LONG).show();
                et_password.setError("la contraseña es incorrecta ");
                et_password.requestFocus();
                et_password.setText("");
                break;

            case "ERROR_USER_MISMATCH":
                Toast.makeText(RegistroActivity.this, "Las credenciales proporcionadas no corresponden al usuario que inició sesión anteriormente..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_REQUIRES_RECENT_LOGIN":
                Toast.makeText(RegistroActivity.this,"Esta operación es sensible y requiere autenticación reciente. Inicie sesión nuevamente antes de volver a intentar esta solicitud.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                Toast.makeText(RegistroActivity.this, "Ya existe una cuenta con la misma dirección de correo electrónico pero diferentes credenciales de inicio de sesión. Inicie sesión con un proveedor asociado a esta dirección de correo electrónico.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_EMAIL_ALREADY_IN_USE":
                Toast.makeText(RegistroActivity.this, "La dirección de correo electrónico ya está siendo utilizada por otra cuenta..   ", Toast.LENGTH_LONG).show();
                et_email.setError("La dirección de correo electrónico ya está siendo utilizada por otra cuenta.");
                et_email.requestFocus();
                break;

            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                Toast.makeText(RegistroActivity.this, "Esta credencial ya está asociada con una cuenta de usuario diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_DISABLED":
                Toast.makeText(RegistroActivity.this, "La cuenta de usuario ha sido inhabilitada por un administrador..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_TOKEN_EXPIRED":
                Toast.makeText(RegistroActivity.this, "La credencial del usuario ya no es válida. El usuario debe iniciar sesión nuevamente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_NOT_FOUND":
                Toast.makeText(RegistroActivity.this, "No hay ningún registro de usuario que corresponda a este identificador. Es posible que se haya eliminado al usuario.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_USER_TOKEN":
                Toast.makeText(RegistroActivity.this, "La credencial del usuario ya no es válida. El usuario debe iniciar sesión nuevamente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_OPERATION_NOT_ALLOWED":
                Toast.makeText(RegistroActivity.this, "Esta operación no está permitida. Debes habilitar este servicio en la consola.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_WEAK_PASSWORD":
                Toast.makeText(RegistroActivity.this, "La contraseña proporcionada no es válida..", Toast.LENGTH_LONG).show();
                et_password.setError("La contraseña no es válida, debe tener al menos 6 caracteres");
                et_password.requestFocus();
                break;

        }


}}