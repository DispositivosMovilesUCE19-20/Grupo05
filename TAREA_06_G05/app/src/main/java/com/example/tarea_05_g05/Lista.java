package com.example.tarea_05_g05;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

public class Lista extends AppCompatActivity {

    private TextView lista;
    private Button buscar;
    private TextView usuario;
    private File fileUsuarios;
    private String carpeta = "/Download/Usuarios";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista);

        androidx.appcompat.widget.Toolbar toolbarMenu = findViewById(R.id.menu);
        setSupportActionBar(toolbarMenu);
        lista = (TextView) findViewById(R.id.txtLista);

        //Listar Usuarios
        String path= (Environment.getExternalStorageDirectory()+this.carpeta);
        File directorio = new File(path);
        String name = "usuarios.txt";
        this.fileUsuarios = new File(directorio,name);
        FileReader fr = null;
        try {
            fr = new FileReader(fileUsuarios.toString());


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        String datosDD ="";

        int valor= 0;
        try {
            valor = fr.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(valor!=-1){
            datosDD+=((char)valor);
            try {
                valor=fr.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
       // Toast.makeText(getApplicationContext(),datosDD,Toast.LENGTH_LONG).show();
        try {
            JSONArray jrray = new JSONArray(datosDD);
            lista.setText("");
            for(int i =0; i<jrray.length();i++){
                JSONObject objecto = jrray.getJSONObject(i);
                lista.append("- ");
                lista.append(objecto.getString("Usuario"));
                lista.append("\n");
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        String archivos[] = fileList();


        buscar = (Button) findViewById(R.id.btnBuscar);
        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarDatos();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater  = getMenuInflater();
        inflater.inflate(R.menu.opciones,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.salir:
               // finish();
                //moveTaskToBack(true);
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                break;

        }
        switch (item.getItemId()){
            case R.id.menuPrincipal:
                SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
                SharedPreferences.Editor Obj_editor = preferences.edit();
                Obj_editor.clear();
                Obj_editor.commit();
                Intent intent = new Intent ( Lista.this, MainActivity.class);
                startActivity(intent);

                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void cargarDatos() {
        boolean cont=false;
        usuario = (TextView) findViewById(R.id.txtUsuarioLista);
        String datos = "";
        //Cargar los usuarios
        String path = (Environment.getExternalStorageDirectory() + this.carpeta);
        File directorio = new File(path);
        String name = "usuarios.txt";
        this.fileUsuarios = new File(directorio, name);
        FileReader fr = null;
        try {
            fr = new FileReader(fileUsuarios.toString());


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        String datosDD = "";

        int valor = 0;
        try {
            valor = fr.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (valor != -1) {
            datosDD += ((char) valor);
            try {
                valor = fr.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            JSONArray jrray = new JSONArray(datosDD);
            for (int i = 0; i < jrray.length(); i++) {
                JSONObject obj = jrray.getJSONObject(i);
                if (obj.getString("Usuario").equals(usuario.getText().toString())) {
                    cont = true;
                    String mensaje = "";
                    mensaje = mensaje + "Usuario: " + obj.getString("Usuario");
                    mensaje = mensaje + "\n";
                    mensaje = mensaje + "\n";
                    mensaje = mensaje + "Nombre: " + obj.getString("Nombre");
                    mensaje = mensaje + "\n";
                    mensaje = mensaje + "\n";
                    mensaje = mensaje + "Apellido: " + obj.getString("Apellido");
                    mensaje = mensaje + "\n";
                    mensaje = mensaje + "\n";
                    mensaje = mensaje + "Correo: ";
                    mensaje = mensaje + "\n";
                    mensaje = mensaje + obj.getString("Email");
                    mensaje = mensaje + "\n";
                    mensaje = mensaje + "\n";
                    mensaje = mensaje + "Celular: " + obj.getString("Celular");
                    mensaje = mensaje + "\n";
                    mensaje = mensaje + "\n";
                    mensaje = mensaje + "Genero: " + obj.getString("Sexo");
                    mensaje = mensaje + "\n";
                    mensaje = mensaje + "\n";
                    mensaje = mensaje + "Fecha de Nacimiento: " + obj.getString("FechaNacimiento");
                    mensaje = mensaje + "\n";
                    mensaje = mensaje + "\n";
                    mensaje = mensaje + "Materias: " + obj.getString("Materias");
                    mensaje = mensaje + "\n";
                    mensaje = mensaje + "\n";
                    mensaje = mensaje + "Beca: " + obj.getString("Beca");
                    mensaje = mensaje + "\n";
                    mensaje = mensaje + "\n";
                    AlertDialog.Builder alerta = new AlertDialog.Builder(Lista.this);
                    alerta.setMessage(mensaje);

                    AlertDialog titulo = alerta.create();
                    titulo.setTitle("Datos");
                    titulo.show();
                    break;
                }
            }
            if(!cont){
                Toast.makeText(getApplicationContext(), "No existe ese usuario", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}

