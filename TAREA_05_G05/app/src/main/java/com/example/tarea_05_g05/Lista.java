package com.example.tarea_05_g05;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

public class Lista extends AppCompatActivity {

    private TextView lista;
    private Button buscar;
    private TextView usuario;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista);

        androidx.appcompat.widget.Toolbar toolbarMenu = findViewById(R.id.menu);
        setSupportActionBar(toolbarMenu);
        lista = (TextView) findViewById(R.id.txtLista);
        String archivos[] = fileList();
        lista.setText("");
        for(int i =0; i<archivos.length;i++){
            lista.append("- ");
            lista.append(archivos[i]);
            lista.append("\n");


        }

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
                finish();
                moveTaskToBack(true);
                break;

        }
        switch (item.getItemId()){
            case R.id.menuPrincipal:
                Intent intent = new Intent ( Lista.this, MainActivity.class);
                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void cargarDatos(){
        usuario = (TextView) findViewById(R.id.txtUsuarioLista);
        Boolean cont=false;
        String datos="";
        String archivos[] = fileList();
        for(int i=0; i<archivos.length;i++){
            if(archivos[i].equals(usuario.getText().toString())){
                cont = true;
                try {
                    FileInputStream fileInputStream = getApplicationContext().openFileInput(archivos[i]);
                    InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;

                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    datos=datos+(stringBuilder.toString());
                    // Toast.makeText(getApplicationContext(),datos,Toast.LENGTH_LONG).show();
                    JSONObject obj = new JSONObject(datos);
                    String clave2 = obj.getString("Clave");
                    String mensaje = "";
                    mensaje = mensaje + "Usuario: " + obj.getString("Usuario");
                    mensaje = mensaje+"\n";
                    mensaje = mensaje+"\n";
                    mensaje = mensaje + "Nombre: " + obj.getString("Nombre");
                    mensaje = mensaje+"\n";
                    mensaje = mensaje+"\n";
                    mensaje = mensaje + "Apellido: " + obj.getString("Apellido");
                    mensaje = mensaje+"\n";
                    mensaje = mensaje+"\n";
                    mensaje = mensaje + "Correo: ";
                    mensaje = mensaje+"\n";
                    mensaje = mensaje + obj.getString("Email");
                    mensaje = mensaje+"\n";
                    mensaje = mensaje+"\n";
                    mensaje = mensaje + "Celular: " + obj.getString("Celular");
                    mensaje = mensaje+"\n";
                    mensaje = mensaje+"\n";
                    mensaje = mensaje + "Genero: " + obj.getString("Sexo");
                    mensaje = mensaje+"\n";
                    mensaje = mensaje+"\n";
                    mensaje = mensaje + "Fecha de Nacimiento: " + obj.getString("FechaNacimiento");
                    mensaje = mensaje+"\n";
                    mensaje = mensaje+"\n";
                    mensaje = mensaje + "Materias: " + obj.getString("Materias");
                    mensaje = mensaje+"\n";
                    mensaje = mensaje+"\n";
                    mensaje = mensaje + "Beca: " + obj.getString("Beca");
                    mensaje = mensaje+"\n";
                    mensaje = mensaje+"\n";
                    mensaje = mensaje + "Foto: " ;
                    mensaje = mensaje+"\n";


                    //String mensaje = obj.toString();
                    AlertDialog.Builder alerta = new AlertDialog.Builder(Lista.this);
                    String lugar = obj.getString("Foto");
                    WebView wv = new WebView(this);
                    wv.loadDataWithBaseURL("file:///android_res/drawable/", "<img src='"+lugar+"' />", "text/html", "utf-8", null);

                    //wv.setScaleX(100);
                    //wv.setScaleX(100);
                    wv.setInitialScale(30);
                    alerta.setView(wv);
                    alerta.setMessage(mensaje);

                    AlertDialog titulo = alerta.create();
                    titulo.setTitle("Datos");
                    titulo.show();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }
        if(cont==false){
            Toast.makeText(getApplicationContext(),"No existe ese usuario",Toast.LENGTH_LONG).show();
        }
    }

}

