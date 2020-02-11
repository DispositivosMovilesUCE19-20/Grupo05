package ec.edu.uce.optativa3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.audiofx.BassBoost;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ec.edu.uce.controlador.RegistroEstudiante;

public class Lista extends AppCompatActivity {

    private TableLayout listaEstu;
    private TextView idEstudiante;
    private Button eliminar;
    private Button verDatos;
    private TextView mensaje;
    private ImageView imagenDetectada;
    private SeekBar barraBrillo;
    Context context;
    int cantidadBrillo;

    String usuario;
    String nombre="";
    String modelo="";
    String version="";
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista);

        usuario = getIntent().getStringExtra("usuario");
        nombre="";
        modelo="";
        version="";

        mensaje= findViewById(R.id.txtMensaje);
        barraBrillo= findViewById(R.id.barBrillo);
        context = getApplicationContext();

        //Para el brillo

        final WindowManager.LayoutParams params = getWindow().getAttributes();
        params.screenBrightness = 100;
        cantidadBrillo = Settings.System.getInt(context.getContentResolver(),Settings.System.SCREEN_BRIGHTNESS,0);
        barraBrillo.setProgress(cantidadBrillo);
        barraBrillo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String mm = String.valueOf(progress);
                Toast.makeText(getApplicationContext(),mm , Toast.LENGTH_SHORT).show();
               // Settings.System.putInt(context.getContentResolver(),Settings.System.SCREEN_BRIGHTNESS,progress);
                 params.screenBrightness = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Servicios mm = new Servicios();
        String mms= mm.mensaje();
        mensaje.setText(mms);

        listaEstu = findViewById(R.id.tableLista);
        idEstudiante =findViewById(R.id.txtId);
        eliminar = findViewById(R.id.btnEliminar);
        verDatos = findViewById(R.id.btnBuscar);

        androidx.appcompat.widget.Toolbar toolbarMenu = findViewById(R.id.menu);
        setSupportActionBar(toolbarMenu);


        Listar();

        Button crear = (Button) findViewById(R.id.btnRegistrar);
        Button editar = (Button) findViewById(R.id.btnEditar);

        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (Lista.this, RegistroEstudiante.class );
                startActivity(intent);
            }
        });

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarBeca(idEstudiante.getText().toString())){
                    Servicios mm = new Servicios();
                    String mms= mm.mensajeNoEditar();
                    Toast.makeText(getApplicationContext(), mms,Toast.LENGTH_SHORT).show();
                }else{
                    editarEstudiante();
                }


            }
        });

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrarDatos();
            }
        });

        verDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verDatos();
            }
        });
    }


    public void Listar(){
        SqlLite admin = new SqlLite(this,"administracion",null,1);
        SQLiteDatabase BaseDatos = admin.getWritableDatabase();
        Cursor fila = BaseDatos.rawQuery("select id,nombre,apellido,fecha from ESTUDIANTES Order by fecha DESC ",null);

        TextView textView;

        if(fila.moveToFirst()) {

            do{
                TableRow row = new TableRow(getBaseContext());

                    textView = new TextView(getBaseContext());
                    textView.setText(" "+fila.getString(0)+" "+fila.getString(1)+" "+fila.getString(2)+" "+fila.getString(3));
                    row.addView(textView);
                listaEstu.addView(row);

            }while(fila.moveToNext());



            }
             BaseDatos.close();

        }

        public void verDatos(){
        WebView wv = new WebView(this);

        String nn= idEstudiante.getText().toString();
            SqlLite admin = new SqlLite(this,"administracion",null,1);
            SQLiteDatabase BaseDatos = admin.getWritableDatabase();

            if(!nn.isEmpty()) {

                Cursor fila = BaseDatos.rawQuery("select nombre,apellido,email,celular,genero,fecha,asignaturas,becado,foto " +
                        "from ESTUDIANTES where id='"+nn+"'",null);
                if (fila.moveToFirst()) {

                    String mensaje = "";
                    mensaje = mensaje + "Nombre: " +fila.getString(0);
                    mensaje = mensaje + "\n";
                    mensaje = mensaje + "\n";
                    mensaje = mensaje + "Apellido: " + fila.getString(1);
                    mensaje = mensaje + "\n";
                    mensaje = mensaje + "\n";
                    mensaje = mensaje + "Correo: ";
                    mensaje = mensaje + "\n";
                    mensaje = mensaje + fila.getString(2);
                    mensaje = mensaje + "\n";
                    mensaje = mensaje + "\n";
                    mensaje = mensaje + "Celular: " + fila.getString(3);
                    mensaje = mensaje + "\n";
                    mensaje = mensaje + "\n";
                    mensaje = mensaje + "Genero: " + fila.getString(4);
                    mensaje = mensaje + "\n";
                    mensaje = mensaje + "\n";
                    mensaje = mensaje + "Fecha de Nacimiento: " + fila.getString(5);
                    mensaje = mensaje + "\n";
                    mensaje = mensaje + "\n";
                    mensaje = mensaje + "Materias: " + fila.getString(6);
                    mensaje = mensaje + "\n";
                    mensaje = mensaje + "\n";
                    mensaje = mensaje + "Beca: " + fila.getString(7);
                    mensaje = mensaje + "\n";
                    mensaje = mensaje + "\n";
                    //mensaje = mensaje + "Foto: " + fila.getString(8);
                    AlertDialog.Builder alerta = new AlertDialog.Builder(Lista.this);
                    alerta.setMessage(mensaje);
                    String ib64=fila.getString(8);
                    Bitmap bt = convert(ib64);

                    ImageView showImage = new ImageView(Lista.this);

                    showImage.setImageBitmap(bt);


                    alerta.setView(showImage);

                    AlertDialog titulo = alerta.create();
                    titulo.setTitle("Datos");
                    titulo.show();
                }else{
                    Toast.makeText(getApplicationContext(), "El estudiante no existe", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getApplicationContext(), "Ingrese un nombre", Toast.LENGTH_SHORT).show();
            }
            BaseDatos.close();

        }

    public void borrarDatos(){

        String nn= idEstudiante.getText().toString();
       // String nn="88h";
        SqlLite admin = new SqlLite(this,"administracion",null,1);
        SQLiteDatabase BaseDatos = admin.getWritableDatabase();

        if(!nn.isEmpty()) {

            int cantidad = BaseDatos.delete("estudiantes", "id ='" + nn+"'", null);

            BaseDatos.close();
            if (cantidad != 0) {
                Toast.makeText(getApplicationContext(), "Estudiante borrado correctamente", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent (Lista.this, Lista.class );
                startActivity(intent);

            } else {
                Toast.makeText(getApplicationContext(), "Estudiante no encontrado", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Ingrese un nombre", Toast.LENGTH_SHORT).show();
        }

    }

    public void editarEstudiante(){

        String nn= idEstudiante.getText().toString();
        SqlLite admin = new SqlLite(this,"administracion",null,1);
        SQLiteDatabase BaseDatos = admin.getWritableDatabase();


        if(!nn.isEmpty()) {
            Cursor fila = BaseDatos.rawQuery("select nombre from ESTUDIANTES where id ='"+nn+"'",null);



            if (fila.moveToFirst()) {

                Intent intent = new Intent(Lista.this, Editar.class);
                intent.putExtra("id", nn);
                startActivity(intent);

            } else {
                Toast.makeText(getApplicationContext(), "Estudiante no encontrado", Toast.LENGTH_SHORT).show();

            }
        }else{
            Toast.makeText(getApplicationContext(), "Ingrese un Id", Toast.LENGTH_SHORT).show();
        }
         BaseDatos.close();
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

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                break;

        }
        switch (item.getItemId()){
            case R.id.ordenarApellido:
                ordenarApellido();
                Toast.makeText(getApplicationContext(), "Se ordeno por apellido", Toast.LENGTH_SHORT).show();

                break;

        }
        switch (item.getItemId()){
            case R.id.ordenarFecha:
                ordenarFecha();
                Toast.makeText(getApplicationContext(), "Se ordeno por fecha", Toast.LENGTH_SHORT).show();

                break;

        }
        switch (item.getItemId()){
            case R.id.menuPrincipal:
                SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
                SharedPreferences.Editor Obj_editor = preferences.edit();
                Obj_editor.clear();
                Obj_editor.commit();

                //Llamanos al servicio para los logs
                try {
                    logs();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
               // Toast.makeText(getApplicationContext(), "Adios "+usuario, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent ( Lista.this, MainActivity.class);
                startActivity(intent);

                break;

        }
        return super.onOptionsItemSelected(item);
    }


    public void logs() throws JSONException {

       nombre= Build.MANUFACTURER;
       modelo= Build.MODEL;
       version = Build.VERSION.RELEASE;

       //url="https://api-rest-grupo05.herokuapp.com/saveLogs";
       //url="https://api-rest-grupo05.herokuapp.com/toXml";
       url="https://serviciogrupo05.herokuapp.com/";
        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject postparams = new JSONObject();
        postparams.put("usuario", usuario);
        postparams.put("modelo", modelo);
        postparams.put("nombre", nombre);
        postparams.put("version", version);


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                "https://api-rest-grupo05.herokuapp.com/saveLogs",postparams ,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        JSONObject mjson = new JSONObject();
                        mjson = response;
                        try {
                            String mmm= mjson.getString("message");
                            Toast.makeText(getApplicationContext(), mmm, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Problemas con el servidor", Toast.LENGTH_SHORT).show();
                       Log.e("Eroor","Problemas con el servido");
                    }
                });
// Adding the request to the queue along with a unique string tag
        queue.add(jsonObjReq);
    }

    public static Bitmap convert(String base64Str) throws IllegalArgumentException
    {
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",")  + 1),
                Base64.DEFAULT
        );

        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public void ordenarApellido(){
        listaEstu.removeAllViews();
        SqlLite admin = new SqlLite(this,"administracion",null,1);
        SQLiteDatabase BaseDatos = admin.getWritableDatabase();
        Cursor fila = BaseDatos.rawQuery("select id,nombre,apellido,fecha from ESTUDIANTES Order by apellido ASC ",null);

        TextView textView;

        if(fila.moveToFirst()) {

            do{
                TableRow row = new TableRow(getBaseContext());

                textView = new TextView(getBaseContext());
                textView.setText(" "+fila.getString(0)+" "+fila.getString(1)+" "+fila.getString(2)+fila.getString(3));
                row.addView(textView);
                listaEstu.addView(row);

            }while(fila.moveToNext());



        }
        BaseDatos.close();

    }

    public void ordenarFecha(){
        listaEstu.removeAllViews();
        SqlLite admin = new SqlLite(this,"administracion",null,1);
        SQLiteDatabase BaseDatos = admin.getWritableDatabase();
        Cursor fila = BaseDatos.rawQuery("select id,nombre,apellido,fecha from ESTUDIANTES Order by fecha ASC ",null);

        TextView textView;

        if(fila.moveToFirst()) {

            do{
                TableRow row = new TableRow(getBaseContext());

                textView = new TextView(getBaseContext());
                textView.setText(" "+fila.getString(0)+" "+fila.getString(1)+" "+fila.getString(2)+" "+fila.getString(3));
                row.addView(textView);
                listaEstu.addView(row);

            }while(fila.moveToNext());



        }
        BaseDatos.close();

    }

    public boolean validarBeca(String id){
        SqlLite admin = new SqlLite(this, "administracion", null, 1);
        SQLiteDatabase BaseDatos = admin.getWritableDatabase();

        Cursor fila = BaseDatos.rawQuery("select becado " +
                "from ESTUDIANTES where id='" + id + "'", null);

        String beca =fila.getString(0);
        Toast.makeText(getApplicationContext(), "BECA"+beca, Toast.LENGTH_SHORT).show();
        if(beca.equals("Si")){
            return true;
        }else{
            return false;
        }
    }
}
