package com.priscilahuentemilla.cbs_versionoriginal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegistroActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    Button registrar;

    private TextInputEditText registrarNombreUsuario, registrarCorreoElectronico, registrarContraseña;

    private ProgressDialog cargando;

    private FirebaseAuth autenticacion;
    private DatabaseReference userDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        init();

        showPDialog1();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        },1000);


        registrar = findViewById(R.id.btnRegistrar);
        registrarNombreUsuario = findViewById(R.id.registrarUsuario);
        registrarCorreoElectronico = findViewById(R.id.registrarCorreo);
        registrarContraseña = findViewById(R.id.registrarContraseña);
        cargando = new ProgressDialog(this);

        autenticacion = FirebaseAuth.getInstance();

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String nombreUsuario = registrarNombreUsuario.getText().toString().trim();
                final String correo = registrarCorreoElectronico.getText().toString().trim();
                final String contraseña = registrarContraseña.getText().toString().trim();

                if (TextUtils.isEmpty(nombreUsuario)){
                    registrarNombreUsuario.setError("Debe ingresar un nombre de usuario");
                    return;
                }

                if (TextUtils.isEmpty(correo)){
                    registrarCorreoElectronico.setError("Debe ingresar un correo electrónico");
                    return;
                }

                if (TextUtils.isEmpty(contraseña)){
                    registrarContraseña.setError("Debe ingresar contraseña");
                    return;
                }
                else{
                    cargando.setMessage("Registrando...");
                    cargando.setCanceledOnTouchOutside(false);
                    cargando.show();

                    autenticacion.createUserWithEmailAndPassword(correo, contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (!task.isSuccessful()){
                                String error = task.getException().toString();
                                Toast.makeText(RegistroActivity.this, "Error" + error, Toast.LENGTH_SHORT).show();
                            }
                            else {
                                String currentUserId = autenticacion.getCurrentUser().getUid();
                                userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(currentUserId);
                                HashMap userInfo = new HashMap();
                                userInfo.put("id", currentUserId);
                                userInfo.put("Nombre", nombreUsuario);
                                userInfo.put("Correo", correo);

                                userDatabaseReference.updateChildren(userInfo).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(RegistroActivity.this, "Datos guardados correctamente", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(RegistroActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                        Intent intent = new Intent(RegistroActivity.this, PrincipalActivity.class);
                                        startActivity(intent);
                                        finish();
                                        cargando.dismiss();

                                    }
                                });


                            }
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