package ec.edu.uce.controlador;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ec.edu.uce.optativa3.Lista;
import ec.edu.uce.optativa3.Servicios;
import ec.edu.uce.optativa3.R;
import ec.edu.uce.optativa3.SqlLite;

public class RegistroEstudiante extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextView date,mensaje;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TextView txtFecha;
    private TextView nombre;
    private TextView apellido;
    private TextView celular;
    private TextView email;
    private RadioButton hombre;
    private RadioButton mujer;
    private CheckBox lenguaje,ciencias,progra,analisis,fisica,ingles;
    private Switch beca;
    private Button registroOk;
    private Button botonCamara;
    private ImageView foto;
    private String imagenB64;
    private String mensajeValidacion;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);

        mensaje= findViewById(R.id.txtMensaje);
        Servicios mm = new Servicios();
        String mms= mm.mensaje();
        mensaje.setText(mms);

        botonCamara = findViewById(R.id.btnFoto);
        foto = findViewById(R.id.imgFoto);
        nombre = (EditText) findViewById(R.id.txtNombre);
        apellido = (EditText) findViewById(R.id.txtApellido);
        email = (EditText) findViewById(R.id.txtEmail);
        celular = (EditText) findViewById(R.id.txtCelular);
        hombre= (RadioButton) findViewById(R.id.rbHombre);
        mujer = (RadioButton) findViewById(R.id.rbMujer);
        txtFecha = (TextView) findViewById(R.id.txtFecha);
        lenguaje = (CheckBox) findViewById(R.id.cbcLenguaje);
        ciencias = (CheckBox) findViewById(R.id.cbcCiencias);
        progra = (CheckBox) findViewById(R.id.cbcProgra);
        analisis = (CheckBox) findViewById(R.id.cbcAnalisis);
        fisica = (CheckBox) findViewById(R.id.cbcFisica);
        ingles = (CheckBox) findViewById(R.id.cbcIngles);
        beca = (Switch) findViewById(R.id.swtBecado);
        registroOk = (Button) findViewById(R.id.btnGuardar);



        //Para la fecha
        date = (TextView) findViewById(R.id.txtFecha);
        date.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int anio = Calendar.YEAR;
                int mes = Calendar.MONTH;
                int dia = Calendar.DAY_OF_MONTH;

                DatePickerDialog dialog =  new DatePickerDialog(RegistroEstudiante.this,
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
                String dates  = anio+"/"+mes+"/"+dia;
                date.setText(dates);
            }
        };

        registroOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cedula=celular.getText().toString();
                String correo=email.getText().toString();
                String respCelular="";
                String respCorreo="";
                respCelular=validarCedula(cedula);
                respCorreo=validarCorreo(correo);
                if(respCelular=="Si" && respCorreo=="Si"){
                    registroEstu();
                }else{
                        mensajeValidacion="Datos mal ingresados";


                        Toast.makeText(getApplicationContext(),mensajeValidacion,Toast.LENGTH_SHORT).show();

                }
                if(respCelular=="No"){
                    celular.setTextColor((Color.rgb(200,0,0)));
                }
                if(respCorreo=="No"){
                    email.setTextColor((Color.rgb(200,0,0)));
                }

            }
        });

        celular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                celular.setTextColor(Color.rgb(0,0,0));
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email.setTextColor(Color.rgb(0,0,0));
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

    public void registroEstu(){
        String sexo;
        String materias="";
        String becaR;
        String nombreT,apellidoT,emailT,celularT,fechaT;

        nombreT = nombre.getText().toString();
        apellidoT = apellido.getText().toString();
        emailT = email.getText().toString();
        celularT = celular.getText().toString();
        fechaT = txtFecha.getText().toString();




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

        String foto="asdfasdfasdf";
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
            datosEstudiantes.put("foto",imagenB64);


            BaseDatos.insert("ESTUDIANTES",null,datosEstudiantes);
            BaseDatos.close();

            Intent intent = new Intent (this, Lista.class );
            startActivity(intent);
            Toast.makeText(getApplicationContext(),"Estudiante registrado exitosamente",Toast.LENGTH_SHORT).show();

    }

    public String validarCedula(String cadena){
        Pattern pat = Pattern.compile("^(09)?[8|9][0-9]{7}$");
        Matcher mat = pat.matcher(cadena);
        if (mat.matches()) {
            return "Si";

        } else {
            return "No";
        }
    }



    public String validarCorreo(String cadena){
        Pattern pat = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher mat = pat.matcher(cadena);
        if (mat.matches()) {
            return "Si";

        } else {
            return "No";
        }
    }

}
