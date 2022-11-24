package com.priscilahuentemilla.cbs_versionoriginal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.priscilahuentemilla.cbs_versionoriginal.Adaptadores.ListViewLibrosAdaptador;
import com.priscilahuentemilla.cbs_versionoriginal.Models.Libro;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.UUID;

public class TodosLosLibros extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    //Variables lista

    private ArrayList<Libro> listLibros = new ArrayList<Libro>();
    ArrayAdapter<Libro> arrayAdapterLibro;
    ListViewLibrosAdaptador listViewLibrosAdaptador;
    LinearLayout linearLayoutEditar;
    ListView listViewLibros;

    EditText inputTitulo, inputAutor, inputGenero, inputPaginas;
    Button btnCancelar;

    Libro libroSeleccionado;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    //menu
    ImageView menuIcon;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todos_los_libros);

        //variables de lista libros

        inputTitulo = findViewById(R.id.inputTitulo);
        inputAutor = findViewById(R.id.inputAutor);
        inputGenero = findViewById(R.id.inputGenero);
        inputPaginas = findViewById(R.id.inputPaginas);
        btnCancelar = findViewById(R.id.btnCancelar);

        listViewLibros = findViewById(R.id.listViewLibros);
        linearLayoutEditar = findViewById(R.id.linearLayoutEditar);

        //agregar datos a Firebase

        listViewLibros.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                libroSeleccionado = (Libro) parent.getItemAtPosition(position);
                inputTitulo.setText(libroSeleccionado.getTitulo());
                inputAutor.setText(libroSeleccionado.getAutor());
                inputGenero.setText(libroSeleccionado.getGenero());
                inputPaginas.setText(libroSeleccionado.getPaginas());

                //hacer visible el linearLayout

                linearLayoutEditar.setVisibility(View.VISIBLE);
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayoutEditar.setVisibility(View.GONE);
                libroSeleccionado = null;
            }
        });

        inicializarFirebase();
        listarLibros();

        //menu

        menuIcon = findViewById(R.id.menu_icono);
    }

    private void listarLibros() {
        databaseReference.child("Libros").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listLibros.clear();
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()){
                    Libro l = objSnaptshot.getValue(Libro.class);
                    listLibros.add(l);
                }

                //inicio de adaptador
                listViewLibrosAdaptador = new ListViewLibrosAdaptador(TodosLosLibros.this, listLibros);
                //arrayAdapterLibro = new ArrayAdapter<Libro>(TodosLosLibros.this, android.R.layout.simple_list_item_1, listLibros);

                listViewLibros.setAdapter(listViewLibrosAdaptador);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.crud_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        String titulo = inputTitulo.getText().toString();
        String autor = inputAutor.getText().toString();
        String genero = inputGenero.getText().toString();
        String paginas = inputPaginas.getText().toString();

        switch (item.getItemId()){
            case R.id.menu_agregar:
                insertar();
                break;

            case R.id.menu_guardar:
                if(libroSeleccionado != null){
                    if(validarInputs() == false){
                        Libro l = new Libro();
                        l.setIdlibro(libroSeleccionado.getIdlibro());
                        l.setTitulo(titulo);
                        l.setAutor(autor);
                        l.setGenero(genero);
                        l.setPaginas(paginas);
                        l.setFecharegistro(libroSeleccionado.getFecharegistro());
                        l.setTimestamp(libroSeleccionado.getTimestamp());
                        databaseReference.child("Libros").child(l.getIdlibro()).setValue(l);
                        Toast.makeText(this, "Actualizado Correctamente", Toast.LENGTH_SHORT).show();
                        linearLayoutEditar.setVisibility(View.GONE);
                        libroSeleccionado = null;
                    }
                } else{
                    Toast.makeText(this, "Seleccione un Libro", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.menu_eliminar:
                if(libroSeleccionado != null){
                    Libro l2 = new Libro();
                    l2.setIdlibro(libroSeleccionado.getIdlibro());
                    databaseReference.child("Libros").child(l2.getIdlibro()).removeValue();
                    libroSeleccionado = null;
                }else{
                    Toast.makeText(this, "Seleccione un Libro para eliminar", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean validarInputs() {

        String titulo = inputTitulo.getText().toString();
        String autor = inputAutor.getText().toString();
        String genero = inputGenero.getText().toString();
        String paginas = inputPaginas.getText().toString();

        if(titulo.isEmpty() || titulo.length() < 3){
            showError(inputTitulo, "Titulo inválido (Minimo 3 carácteres");
            return true;
        }else if (autor.isEmpty() || autor.length()<3){
            showError(inputAutor, "Autor inválido (Mínimo 3 carácteres)");
            return true;
        }
        else if (genero.isEmpty() || genero.length()<4){
            showError(inputGenero, "Género inválido (Mínimo 5 carácteres)");
            return true;
        }
        else if (paginas.isEmpty() || paginas.length()<1){
            showError(inputPaginas, "N° de Paginas inválido (Mínimo 1)");
            return true;
        }else {
            return false;
        }

    }

    private void insertar() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(
                TodosLosLibros.this
        );
        View mView = getLayoutInflater().inflate(R.layout.insertar_datos, null);
        Button btnInsertar = (Button) mView.findViewById(R.id.btnInsertar);
        final EditText mInputTitulo = (EditText) mView.findViewById(R.id.inputTitulo);
        final EditText mInputAutor = (EditText) mView.findViewById(R.id.inputAutor);
        final EditText mInputGenero = (EditText) mView.findViewById(R.id.inputGenero);
        final EditText mInputPaginas = (EditText) mView.findViewById(R.id.inputPaginas);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        btnInsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titulo = mInputTitulo.getText().toString();
                String autor = mInputAutor.getText().toString();
                String genero = mInputGenero.getText().toString();
                String paginas = mInputPaginas.getText().toString();
                if (titulo.isEmpty() || titulo.length()<3){
                    showError(mInputTitulo, "Titulo inválido (Mínimo 3 carácteres)");
                }
                else if (autor.isEmpty() || autor.length()<3){
                    showError(mInputAutor, "Autor inválido (Mínimo 3 carácteres)");
                }
                else if (genero.isEmpty() || genero.length()<4){
                    showError(mInputGenero, "Género inválido (Mínimo 5 carácteres)");
                }
                else if (paginas.isEmpty() || paginas.length()<1){
                    showError(mInputPaginas, "N° de Paginas inválido (Mínimo 1)");
                }else{
                    Libro l = new Libro();
                    l.setIdlibro(UUID.randomUUID().toString());
                    l.setTitulo(titulo);
                    l.setAutor(autor);
                    l.setGenero(genero);
                    l.setPaginas(paginas);
                    l.setFecharegistro(getFechaNormal(getFechaMilisegundos()));
                    l.setTimestamp(getFechaMilisegundos() * -1);
                    databaseReference.child("Libros").child(l.getIdlibro()).setValue(l);
                    Toast.makeText(TodosLosLibros.this, "Libro agregado Correctamente", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private long getFechaMilisegundos() {
        Calendar calendar = Calendar.getInstance();
        long tiempounix = calendar.getTimeInMillis();

        return tiempounix;
    }

    public String getFechaNormal(long fechamilisegundos){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
        String fecha = sdf.format(fechamilisegundos);
        return fecha;

    }

    private void showError(EditText input, String s) {
        input.requestFocus();
        input.setError(s);
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void navigationDrawer() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_Libros);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    //
    @Override
    public void onBackPressed(){

        if(drawerLayout.isDrawerVisible(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        FragmentManager fm = getSupportFragmentManager();
        if (id == R.id.nav_inicio){
            startActivity(new Intent(this, PrincipalActivity.class));
            finish();
        } else if (id == R.id.nav_Libros){
            startActivity(new Intent(this, TodosLosLibros.class));
            finish();
        } else if (id == R.id.nav_categorias){
            startActivity(new Intent(this, CategoriasActivity.class));
            finish();
        } else if (id == R.id.perfil) {
            startActivity(new Intent(this, PerfilActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}