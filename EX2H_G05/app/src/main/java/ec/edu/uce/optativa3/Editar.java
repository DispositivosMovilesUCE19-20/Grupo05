package ec.edu.uce.optativa3;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class Editar extends AppCompatActivity {
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
    private Button botonCamara;
    private ImageView foto;
    private String imagenB64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar);

        mensaje= findViewById(R.id.txtMensaje);
        botonCamara = findViewById(R.id.btnFoto);
        foto = findViewById(R.id.imgFoto);
        Servicios mm = new Servicios();
        String mms= mm.mensaje();
        mensaje.setText(mms);

        nombre = (TextView) findViewById(R.id.txtNombre);
        apellido = (TextView) findViewById(R.id.txtApellido);
        correo = (TextView) findViewById(R.id.txtEmail);
        telefono = (TextView) findViewById(R.id.txtCelular);
        fecha = (TextView) findViewById(R.id.txtFecha);
        hombre = (RadioButton) findViewById(R.id.rbHombre);
        mujer  = (RadioButton) findViewById(R.id.rbMujer);
        beca = (Switch) findViewById(R.id.swtBecado);

        lenguaje = (CheckBox) findViewById(R.id.cbcLenguaje);
        ciencias = (CheckBox) findViewById(R.id.cbcCiencias);
        progra = (CheckBox) findViewById(R.id.cbcProgra);
        analisis = (CheckBox) findViewById(R.id.cbcAnalisis);
        fisica = (CheckBox) findViewById(R.id.cbcFisica);
        ingles = (CheckBox) findViewById(R.id.cbcIngles);

        editar = findViewById(R.id.btnEditar);

        String id = getIntent().getStringExtra("id");
        cargarDatos(id);

        editar.setOnClickListener(new View.OnClickListener() {
            String id = getIntent().getStringExtra("id");
            @Override
            public void onClick(View v) {
                editarEstudiante(id);
            }
        });

        botonCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        imagenB64 = bitmapToBase64(bitmap);
        foto.setImageBitmap(bitmap);



    }
    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }


        public void cargarDatos(String id) {

        SqlLite admin = new SqlLite(this, "administracion", null, 1);
        SQLiteDatabase BaseDatos = admin.getWritableDatabase();

        Cursor fila = BaseDatos.rawQuery("select nombre,apellido,email,celular,genero,fecha,asignaturas,becado,foto " +
                "from ESTUDIANTES where id='" + id + "'", null);

        if (fila.moveToFirst()) {
            nombre.setText(fila.getString(0));
            apellido.setText(fila.getString(1));
            correo.setText(fila.getString(2));
            telefono.setText(fila.getString(3));
            String ib64=fila.getString(8);
            Bitmap bt = convert(ib64);
            foto.setImageBitmap(bt);


            if (fila.getString(4).equals("Hombre")) {
                hombre.setChecked(true);
                mujer.setChecked(false);
            } else {
                hombre.setChecked(false);
                mujer.setChecked(true);
            }
            fecha.setText(fila.getString(5));

            String matt = fila.getString(6);

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

            if(fila.getString(7).equals("Si")){
                beca.setChecked(true);
            }else{
                beca.setChecked(false);
            }

        }

    }

    public void editarEstudiante(String id){

        String sexo;
        String materias="";
        String becaR;
        String nombreT,apellidoT,emailT,celularT,fechaT;
        String fotoB64="";

        nombreT = nombre.getText().toString();
        apellidoT = apellido.getText().toString();
        emailT = correo.getText().toString();
        celularT = telefono.getText().toString();
        fechaT = fecha.getText().toString();
        fotoB64=imagenB64;



        if(beca.isChecked()==true){
            becaR = "Si";
        }else{
            becaR = "No";
        }

        if(lenguaje.isChecked()==true){
            materias=materias+lenguaje.getText().toString()+", ";
        }
        if(ciencias.isChecked()==true){
            materias=materias+ciencias.getText().toString()+", ";
        }
        if(progra.isChecked()==true){
            materias=materias+progra.getText().toString()+", ";
        }
        if(analisis.isChecked()==true){
            materias=materias+analisis.getText().toString()+", ";
        }
        if(fisica.isChecked()==true){
            materias=materias+fisica.getText().toString()+", ";
        }
        if(ingles.isChecked()==true){
            materias=materias+ingles.getText().toString()+", ";
        }

        if(hombre.isChecked()==true){
            sexo="Hombre";

        }else{
            sexo="Mujer";
        }


        SqlLite admin = new SqlLite(this,"administracion",null,1);
        SQLiteDatabase BaseDatos = admin.getWritableDatabase();


        ContentValues datosEstudiantes = new ContentValues();
        datosEstudiantes.put("nombre",nombreT);
        datosEstudiantes.put("apellido",apellidoT);
        datosEstudiantes.put("email",emailT);
        datosEstudiantes.put("celular",celularT);
        datosEstudiantes.put("genero",sexo);
        datosEstudiantes.put("fecha",fechaT);
        datosEstudiantes.put("asignaturas",materias);
        datosEstudiantes.put("becado",becaR);
        datosEstudiantes.put("foto",fotoB64);

        int cantidad = BaseDatos.update("ESTUDIANTES",datosEstudiantes,"id='"+id+"'",null);
        BaseDatos.close();

        if(cantidad!=0){
            Intent intent = new Intent (this, Lista.class );
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "Estudiante editado correctamente", Toast.LENGTH_SHORT).show();
        }
    }

    public static Bitmap convert(String base64Str) throws IllegalArgumentException
    {
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",")  + 1),
                Base64.DEFAULT
        );

        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

}
