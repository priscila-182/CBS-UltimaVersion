package com.priscilahuentemilla.cbs_versionoriginal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class LoginActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private TextView dirigirRegistro;
    private Button iniciarSesion;
    private EditText loginCorreo, loginContraseña;

    private ProgressDialog cargando;
    private FirebaseAuth autenticacion;
    private FirebaseAuth.AuthStateListener authStateListener;

    private DatabaseReference userDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        init();

        showPDialog1();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        },1000);

        cargando = new ProgressDialog(this);

        autenticacion = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = autenticacion.getCurrentUser();
                if (user !=null ){
                    Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        dirigirRegistro = findViewById(R.id.registrarse);
        iniciarSesion = findViewById(R.id.buttonIngresar);
        loginCorreo = findViewById(R.id.correo);
        loginContraseña = findViewById(R.id.contraseña);


        dirigirRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegistroActivity.class));
            }
        });

        iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String correo = loginCorreo.getText().toString().trim();
                final String contraseña = loginContraseña.getText().toString().trim();

                if (TextUtils.isEmpty(correo)){
                    loginCorreo.setError("Debe ingresar un correo electrónico");
                    return;
                }

                if (TextUtils.isEmpty(contraseña)){
                    loginContraseña.setError("Debe ingresar contraseña");
                    return;
                }

                else{
                    cargando.setMessage("Iniciando sesión...");
                    cargando.setCanceledOnTouchOutside(false);
                    cargando.show();

                    autenticacion.signInWithEmailAndPassword(correo, contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, "Sesión iniciada", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }

                            cargando.dismiss();
                        }
                    });
                }
            }
        });
    }

    private void init() {
        this.progressDialog = new ProgressDialog(this);
    }

    private void showPDialog1(){
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Cargando");
        progressDialog.setMessage("Por favor, espere");
        progressDialog.show();
    }
}