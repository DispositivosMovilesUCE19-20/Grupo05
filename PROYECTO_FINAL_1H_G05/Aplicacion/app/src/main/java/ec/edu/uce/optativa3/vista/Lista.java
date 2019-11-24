package ec.edu.uce.optativa3.vista;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tarea_05_g05.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import ec.edu.uce.optativa3.controlador.Editar;
import ec.edu.uce.optativa3.controlador.ListaControl;

public class Lista extends AppCompatActivity {

    private TextView lista,mensaje;
    private Button buscar;
    private Button editar;
    private Button eliminar;
    private TextView usuario;
    private File fileUsuarios;
    private String carpeta = "/Download/Usuarios";



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista);

        androidx.appcompat.widget.Toolbar toolbarMenu = findViewById(R.id.menu);
        setSupportActionBar(toolbarMenu);
        lista = (TextView) findViewById(R.id.txtLista);
        //Para el servicio
        mensaje();
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

        //Para eliminar usuarios
        eliminar = (Button) findViewById(R.id.btnBorrar);
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrar();
                Intent intent = new Intent (Lista.this, Lista.class);
                startActivity(intent);
            }
        });

        //Para editar usuarios
        usuario = findViewById(R.id.txtUsuarioLista);
        editar = (Button) findViewById(R.id.btnEditar);
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (Lista.this, Editar.class);
                intent.putExtra("usuario",usuario.getText().toString());
                startActivity(intent);
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
                //para guardar en el servicio
                ListaControl lc = new ListaControl();
                try {
                    lc.mensajePut();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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

    public void borrar(){
        FileWriter fichero = null;
        PrintWriter pw = null;
        boolean cont=false;
        usuario = (TextView) findViewById(R.id.txtUsuarioLista);
        String name ="usuarios.txt";
        String us = usuario.getText().toString();
        String path= (Environment.getExternalStorageDirectory()+this.carpeta);
        File directorio = new File(path);
        this.fileUsuarios = new File(directorio,name);

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
                   jrray.remove(i);
                    fichero = new FileWriter(fileUsuarios);
                    pw = new PrintWriter(fichero);
                    pw.println(jrray.toString());
                    pw.flush();
                    pw.close();
                    String estado = "eliminadoOk";
                    mensajeEstado(estado);
                    break;
                }
            }
            if(!cont){
                Toast.makeText(getApplicationContext(), "No existe ese usuario", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void mensajeEstado(String estado){
        usuario = (TextView) findViewById(R.id.txtUsuarioLista);

          //  mensaje = (TextView) findViewById(R.id.txtMensaje);
            String jsonMensaje = "https://api-rest-grupo05.herokuapp.com/grupo05/mensajes";
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
             //   mensaje.setText(js.getString("msg"));

                if(estado == "eliminadoOk"){
                    Toast.makeText(getApplicationContext(), js.getString("borrado"), Toast.LENGTH_LONG).show();

                }

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

