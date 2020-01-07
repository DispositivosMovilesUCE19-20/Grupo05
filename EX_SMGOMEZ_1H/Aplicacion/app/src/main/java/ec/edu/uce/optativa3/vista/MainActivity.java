package ec.edu.uce.optativa3.vista;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tarea_05_g05.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import ec.edu.uce.optativa3.controlador.MainActivityControl;
import ec.edu.uce.optativa3.controlador.RegistroControl;

/**
 * Clase que maneja la actividad principal es decir la vista
 * que ve el usuario al iniciar la app
 */
public class MainActivity extends AppCompatActivity {

    private EditText user,clave;
    private  TextView mensaje;
    private File fileUsuarios;
    //ruta de la carpeta donde se guardará los archivos
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
        //preferencias compartidas
        SharedPreferences preferences = getSharedPreferences("datos",Context.MODE_PRIVATE);
        user.setText(preferences.getString("usuario",""));
        clave.setText(preferences.getString("clave",""));


        Button btn = (Button) findViewById(R.id.btnRegistrarse);
        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        Button btnTest = (Button) findViewById(R.id.ElBoton);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (MainActivity.this, RegistroControl.class);
                startActivity(intent);
            }
        });

        btnTest.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (MainActivity.this, MyTest.class);
                startActivity(intent);
            }
        });

        //Para el login
        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String success=null;
                MainActivityControl mainActivityControl = new MainActivityControl();
                try {
                    Boolean resp = mainActivityControl.acceder(user.getText().toString(), clave.getText().toString());
                    if(resp){
                        success=succesMessage();
                        preferencias(user.getText().toString(),clave.getText().toString());
                        Intent intent = new Intent (MainActivity.this, Lista.class);
                        startActivity(intent);

                        Toast.makeText(getApplicationContext(),success,Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"Usuario o Clave incorrecto",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });
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

    public String succesMessage(){
        String succes =null;
        String jsonMensaje = "https://api-rest-grupo05.herokuapp.com/grupo05/examen";
        //https://api-rest-grupo05.herokuapp.com/grupo05/examen
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
             succes= js.getString("mensajeExamen");

        } catch (MalformedURLException e) {

            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
return succes;

    }

    private void preferencias(String user, String pass) {

        SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor Obj_editor = preferences.edit();
        Obj_editor.putString("usuario",user);
        Obj_editor.putString("clave",pass);
        Obj_editor.commit();

    }




}