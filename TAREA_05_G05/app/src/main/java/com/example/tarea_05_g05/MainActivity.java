package com.example.tarea_05_g05;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    private EditText user,clave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        androidx.appcompat.widget.Toolbar toolbarMenu = findViewById(R.id.menu);
        setSupportActionBar(toolbarMenu);

        Button btn = (Button) findViewById(R.id.btnRegistrarse);
        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (MainActivity.this, Registro.class);
                startActivity(intent);
            }
        });

        //Para el login
        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    acceder();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                Intent intent = new Intent ( MainActivity.this, MainActivity.class);
                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

        public void acceder() throws JSONException {

        user = (EditText) findViewById(R.id.txtUsuario);
        clave = (EditText) findViewById(R.id.txtClave);

            String archivos[] = fileList();
            String num = Integer.toString(archivos.length);
            String datos ="";

            for(int i=0; i<archivos.length;i++){
                if(archivos[i].equals(user.getText().toString())){
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

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                   // Toast.makeText(getApplicationContext(),datos,Toast.LENGTH_LONG).show();
                    JSONObject obj = new JSONObject(datos);
                    String clave2 = obj.getString("Clave");

                    if(clave2.equals(clave.getText().toString()) ){
                        Intent intent = new Intent (MainActivity.this, Lista.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),"Usuario Aceptado",Toast.LENGTH_LONG).show();
                        break;
                    }else{
                        Toast.makeText(getApplicationContext(),"Usuario o Clave invalido",Toast.LENGTH_LONG).show();
                        break;
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Usuario o Clave invalido",Toast.LENGTH_LONG).show();
                }

            }
            // Toast.makeText(getApplicationContext(),"entro "+num,Toast.LENGTH_LONG).show();

        }

}