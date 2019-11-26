package ec.edu.uce.optativa3.controlador;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Calendar;

import ec.edu.uce.optativa3.vista.Lista;

public class Editar extends AppCompatActivity {
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private static final String TAG = "Editar";
    private TextView usuario;
    private TextView nombre;
    private TextView apellido;
    private TextView correo;
    private TextView telefono;
    private TextView fecha,date;
    private File fileUsuarios;
    private RadioButton hombre,mujer;
    private Switch beca;
    private CheckBox lenguaje,ciencias,progra,analisis,fisica,ingles;
    private TextView mensaje;
    private Button editar;
    private String carpeta = "/Download/Usuarios";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar);
        String usuarioM = getIntent().getStringExtra("usuario");
        cargarDatos(usuarioM);
        mensaje();


        //Para la fecha
        date = (TextView) findViewById(R.id.txtFecha);
        date.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int anio = Calendar.YEAR;
                int mes = Calendar.MONTH;
                int dia = Calendar.DAY_OF_MONTH;

                DatePickerDialog dialog =  new DatePickerDialog(Editar.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        anio,mes,dia);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int anio, int mes, int dia) {
                mes = mes+1;
                Log.d(TAG,"onDateSet: date: "+dia+"/"+mes+"/"+anio);
                String dates  = dia+"/"+mes+"/"+anio;
                date.setText(dates);
            }
        };

        //Para guardar los datos
        editar = (Button) findViewById(R.id.btnEditar);
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuarioM = getIntent().getStringExtra("usuario");
                editarAcction(usuarioM);
                Intent intent = new Intent (Editar.this, Lista.class);
                startActivity(intent);
            }
        });

    }


    public void cargarDatos(String usuarioM) {

        FileWriter fichero = null;
        PrintWriter pw = null;
        //boolean cont=false;

       String name ="usuarios.txt";

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
            while (valor != -1) {
                datosDD += ((char) valor);
                valor = fr.read();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            JSONArray jrray = new JSONArray(datosDD);

            //Toast.makeText(getApplicationContext(), jrray.toString(), Toast.LENGTH_LONG).show();
            for (int i = 0; i < jrray.length(); i++) {
              JSONObject obj = jrray.getJSONObject(i);
            if (obj.getString("Usuario").equals(usuarioM.toString())) {

            nombre = (TextView) findViewById(R.id.txtNombre);
            apellido = (TextView) findViewById(R.id.txtApellido);
            correo = (TextView) findViewById(R.id.txtCorreo);
            telefono = (TextView) findViewById(R.id.txtCelular);
            fecha = (TextView) findViewById(R.id.txtFecha);
            hombre = (RadioButton) findViewById(R.id.rbHombre);
            mujer  = (RadioButton) findViewById(R.id.rbMujer);
            beca = (Switch) findViewById(R.id.swBecado);

            lenguaje = (CheckBox) findViewById(R.id.cbLenguaje);
            ciencias = (CheckBox) findViewById(R.id.cbCiencias);
            progra = (CheckBox) findViewById(R.id.cbProgra);
            analisis = (CheckBox) findViewById(R.id.cbAnalisis);
            fisica = (CheckBox) findViewById(R.id.cbFisica);
            ingles = (CheckBox) findViewById(R.id.cbIngles);

             nombre.setText(obj.getString("Nombre"));
             apellido.setText(obj.getString("Apellido"));
             correo.setText(obj.getString("Email"));
             telefono.setText(obj.getString("Celular"));
             fecha.setText(obj.getString("FechaNacimiento"));
             if(obj.getString("Sexo").equals("Hombre")){
                 hombre.setChecked(true);
                 mujer.setChecked(false);
             }else{
                 hombre.setChecked(false);
                 mujer.setChecked(true);
             }
             if(obj.getString("Beca").equals("Si")){
                 beca.setChecked(true);
             }else{
                 beca.setChecked(false);
             }

             String matt = obj.getString("Materias");

             if(matt.contains("Lenguaje")){

                 lenguaje.setChecked(true);
             }
                if(matt.contains("Ciencias")){

                    ciencias.setChecked(true);
                }
                if(matt.contains("Programación")){

                    progra.setChecked(true);
                }
                if(matt.contains("Análisis")){

                    analisis.setChecked(true);
                }
                if(matt.contains("Física")){

                    fisica.setChecked(true);
                }
                if(matt.contains("Inglés")){

                    ingles.setChecked(true);
                }

            //cont = true;
            //jrray.remove(i);
            //fichero = new FileWriter(fileUsuarios);
            //pw = new PrintWriter(fichero);
            //pw.println(jrray.toString());
            //pw.flush();
            //pw.close();
            //String estado = "eliminadoOk";
            //mensajeEstado(estado);
              break;
             }
              }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    //Para editar los cambios
    public void editarAcction(String usuarioM){

        FileWriter fichero = null;
        PrintWriter pw = null;
        //boolean cont=false;

        String name ="usuarios.txt";

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
            while (valor != -1) {
                datosDD += ((char) valor);
                valor = fr.read();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            JSONArray jrray = new JSONArray(datosDD);

            //Toast.makeText(getApplicationContext(), jrray.toString(), Toast.LENGTH_LONG).show();
            for (int i = 0; i < jrray.length(); i++) {
                JSONObject obj = jrray.getJSONObject(i);
                if (obj.getString("Usuario").equals(usuarioM.toString())) {

                    nombre = (TextView) findViewById(R.id.txtNombre);
                    apellido = (TextView) findViewById(R.id.txtApellido);
                    correo = (TextView) findViewById(R.id.txtCorreo);
                    telefono = (TextView) findViewById(R.id.txtCelular);
                    fecha = (TextView) findViewById(R.id.txtFecha);
                    hombre = (RadioButton) findViewById(R.id.rbHombre);
                    mujer  = (RadioButton) findViewById(R.id.rbMujer);
                    beca = (Switch) findViewById(R.id.swBecado);

                    lenguaje = (CheckBox) findViewById(R.id.cbLenguaje);
                    ciencias = (CheckBox) findViewById(R.id.cbCiencias);
                    progra = (CheckBox) findViewById(R.id.cbProgra);
                    analisis = (CheckBox) findViewById(R.id.cbAnalisis);
                    fisica = (CheckBox) findViewById(R.id.cbFisica);
                    ingles = (CheckBox) findViewById(R.id.cbIngles);

                    obj.put("Nombre",nombre.getText().toString());
                    obj.put("Apellido",apellido.getText().toString());
                    obj.put("Email",correo.getText().toString());
                    obj.put("Celular",telefono.getText().toString());
                    obj.put("FechaNacimiento",fecha.getText().toString());

                    if(hombre.isChecked()){
                        obj.put("Sexo","Hombre");
                    }else{
                        obj.put("Sexo","Mujer");
                    }
                    if(beca.isChecked()){
                        obj.put("Beca","Si");
                    }else{
                        obj.put("Beca","No");
                    }

                    String matt = "";

                    if(lenguaje.isChecked()){

                        matt=matt+"Lenguaje, ";
                    }
                    if(ciencias.isChecked()){

                        matt=matt+"Ciencias, ";
                    }
                    if(progra.isChecked()){

                        matt=matt+"Programación, ";
                    }
                    if(analisis.isChecked()){

                        matt=matt+"Análisis, ";
                    }
                    if(fisica.isChecked()){

                        matt=matt+"Física, ";
                    }
                    if(ingles.isChecked()){

                        matt=matt+"Inglés, ";
                    }
                    obj.put("Materias",matt);

                    //cont = true;
                    //jrray.remove(i);
                    fichero = new FileWriter(fileUsuarios);
                    pw = new PrintWriter(fichero);
                    pw.println(jrray.toString());
                    pw.flush();
                    pw.close();
                    String estado = "editadoOk";
                    mensajeEstado(estado);
                    break;
                }
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
            if(estado == "editadoOk"){
                Toast.makeText(getApplicationContext(), js.getString("editado"), Toast.LENGTH_LONG).show();

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
