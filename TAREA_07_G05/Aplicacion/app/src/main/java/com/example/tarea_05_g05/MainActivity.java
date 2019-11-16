package com.example.tarea_05_g05;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    private EditText user,clave;
    private  TextView mensaje;
    private File fileUsuarios;
    private String carpeta = "/Download/Usuarios";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        androidx.appcompat.widget.Toolbar toolbarMenu = findViewById(R.id.menu);
        setSupportActionBar(toolbarMenu);

        //Para cargar el servicio
        mensaje();
        //Para cargar el SharePreferences
        user = (EditText) findViewById(R.id.txtUsuario);
        clave = (EditText) findViewById(R.id.txtClave);
        SharedPreferences preferences = getSharedPreferences("datos",Context.MODE_PRIVATE);
        user.setText(preferences.getString("usuario",""));
        clave.setText(preferences.getString("clave",""));


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
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


        public void acceder() throws JSONException, IOException {
            String path= (Environment.getExternalStorageDirectory()+this.carpeta);
            File directorio = new File(path);
            String name = "usuarios.txt";
            this.fileUsuarios = new File(directorio,name);
        user = (EditText) findViewById(R.id.txtUsuario);
        clave = (EditText) findViewById(R.id.txtClave);

        String us= user.getText().toString();
        String cla = clave.getText().toString();



            FileWriter fichero = null;
            PrintWriter pw = null;

            FileReader fr = new FileReader(fileUsuarios.toString());


            String datosDD ="";

            int valor=fr.read();
            while(valor!=-1){
                datosDD+=((char)valor);
                valor=fr.read();
            }

            JSONArray jrray = new JSONArray(datosDD);

            for(int i =0; i<jrray.length();i++){
                JSONObject obj = jrray.getJSONObject(i);
                //Toast.makeText(getApplicationContext(),obj.toString(),Toast.LENGTH_LONG).show();
                if((obj.getString("Usuario").equals(us))&&(obj.getString("Clave").equals(cla))){
                    Intent intent = new Intent (MainActivity.this, Lista.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),"Ingreso Exitoso",Toast.LENGTH_LONG).show();
                    //Llamar al metodo preferencias
                    preferencias();
                    break;
                }else{
                    Toast.makeText(getApplicationContext(),"Usuario o Password incorrecto",Toast.LENGTH_LONG).show();
                }
            }



        }

    private void preferencias() {

        user = (EditText) findViewById(R.id.txtUsuario);
        clave = (EditText) findViewById(R.id.txtClave);
        SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor Obj_editor = preferences.edit();
        Obj_editor.putString("usuario",user.getText().toString());
        Obj_editor.putString("clave",clave.getText().toString());
        Obj_editor.commit();

    }

    public void mensaje(){
        mensaje = (TextView) findViewById(R.id.txtMensaje);
        String jsonMensaje = "https://serviciogrupo05.herokuapp.com/";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        URL url = null;
        HttpURLConnection conn;
        try {
            url = new URL(jsonMensaje);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String inputLine;
            StringBuffer response = new StringBuffer();
            String json = "";

            while((inputLine= in.readLine())!=null){
                response.append(inputLine);
            }
                json= response.toString();

            JSONObject js = new JSONObject(json);
            mensaje.setText(js.getString("msg"));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
               catch (IOException e) {
            e.printStackTrace();
        }
                    catch (JSONException e) {
            e.printStackTrace();
              }


    }

}