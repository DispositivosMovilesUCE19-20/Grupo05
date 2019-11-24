package ec.edu.uce.optativa3.controlador;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tarea_05_g05.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

import ec.edu.uce.optativa3.vista.MainActivity;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class RegistroControl extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextView date,mensaje;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private EditText usuario,clave,nombre,apellido,email,celular;
    private CheckBox lenguaje,ciencias,progra,analisis,fisica,ingles;
    private TextView txtFecha;
    private RadioButton rb1,rb2;
    private Switch beca;
    private ImageView foto;

    //para las fotos
    private String APP_DIRECTORY = "myPictureApp/";
    private String MEDIA_DIRECTORY = APP_DIRECTORY + "media";
  //  private String TEMPORAL_PICTURE_NAME = "temporal.jpg";

    private final int MY_PERMISSIONS = 100;
    private final int PHOTO_CODE = 200;
    private final int SELECT_PICTURE = 300;

    //private ImageView imageView;
    private ImageView mSetImage;
    private Button mOptionButton;
    private RelativeLayout mRlView;

    private String mPath;

    JSONObject json = new JSONObject();
    JSONArray jsonArray = new JSONArray();
    File fileUsuarios;
    String file_path;
    private String carpeta = "/Download/Usuarios";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);
        androidx.appcompat.widget.Toolbar toolbarMenu = findViewById(R.id.menu);
        Button btnGuardar = (Button) findViewById(R.id.btnGuardar);

        //Para el servicio
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

                DatePickerDialog dialog =  new DatePickerDialog(RegistroControl.this,
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

        //Para la imgen
        mSetImage =(ImageView) findViewById(R.id.imgFoto);
        mOptionButton = (Button) findViewById(R.id.btnFoto);
       // mRlView = (RelativeLayout) findViewById(R.id.rl_view);

        if(mayRequestStoragePermission())
            mOptionButton.setEnabled(true);
        else
            mOptionButton.setEnabled(false);
            mOptionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showOptions();
                }
            });

//Para guardar
            btnGuardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    guardar();
                   // Toast.makeText(getApplicationContext(),"entro",Toast.LENGTH_LONG).show();
                }
            });

            //Para entrar

    }

    private void guardar() {
        String name ="usuarios.txt";
        String nameCarpeta ="prueba";
        String path= (Environment.getExternalStorageDirectory()+this.carpeta);
        File directorio = new File(path);


        if(!directorio.exists()){
            directorio.mkdirs();
        }
        int num = Comprobar();
        if(num>=3){


        String sexo;
        String materias="";
        String becaR;
        usuario = (EditText) findViewById(R.id.txtUsuario);
        clave = (EditText) findViewById(R.id.txtClave);
        nombre = (EditText) findViewById(R.id.txtNombre);
        apellido = (EditText) findViewById(R.id.txtApellido);
        email = (EditText) findViewById(R.id.txtEmail);
        celular = (EditText) findViewById(R.id.txtCelular);
        rb1= (RadioButton) findViewById(R.id.rb1);
        rb2 = (RadioButton) findViewById(R.id.rb2);
        txtFecha = (TextView) findViewById(R.id.txtFecha);
        lenguaje = (CheckBox) findViewById(R.id.cbcLenguaje);
        ciencias = (CheckBox) findViewById(R.id.cbcCiencias);
        progra = (CheckBox) findViewById(R.id.cbcProgra);
        analisis = (CheckBox) findViewById(R.id.cbcAnalisis);
        fisica = (CheckBox) findViewById(R.id.cbcFisica);
        ingles = (CheckBox) findViewById(R.id.cbcIngles);
        beca = (Switch) findViewById(R.id.swtBecado);
        foto = (ImageView) findViewById(R.id.imgFoto);

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

        if(rb1.isChecked()==true){
            sexo="Hombre";

        }else{
            sexo="Mujer";
        }



        String fileName= usuario.getText().toString();

        try {
            json.put("Usuario", usuario.getText());
            json.put("Clave", clave.getText());
            json.put("Nombre", nombre.getText());
            json.put("Apellido", apellido.getText());
            json.put("Email", email.getText());
            json.put("Celular", celular.getText());
            json.put("Sexo", sexo);
            json.put("FechaNacimiento", txtFecha.getText());
            json.put("Materias", materias);
            json.put("Beca", becaR);

        }catch (JSONException ERROR){}
        String content =json.toString();

            //Inicio parara escribir en el archivo

            this.fileUsuarios = new File(directorio,name);
            if(this.fileUsuarios.exists()){
                try {
                    escribir(content);
                }catch(IOException e){
                    e.printStackTrace();
                }

            }
            else if(!this.fileUsuarios.exists()) {
                try {
                    this.fileUsuarios.createNewFile();

                    escribir(content);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            //Fin para escribir en el archivo


     //   FileOutputStream fileOutputStream = null;
       // try {
      //      fileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE);

     //       fileOutputStream.write(content.getBytes());
      //      fileOutputStream.close();
           // Toast.makeText(getApplicationContext(),content, Toast.LENGTH_LONG).show();
     //   }catch(IOException e){
     //       e.printStackTrace();
      //  }

        Toast.makeText(getApplicationContext(),"Usuario "+usuario.getText().toString()+" creado satisfactoriamente",Toast.LENGTH_LONG).show();

        Intent intent = new Intent (RegistroControl.this, MainActivity.class);
        startActivity(intent);
    }else{
            Toast.makeText(getApplicationContext(),"Tienes que seleccionar al menos 3 materias",Toast.LENGTH_LONG).show();
        }
    }

    private boolean mayRequestStoragePermission(){

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;
        if((checkSelfPermission(WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)&&
                (checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED))
            return true;
        if((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) || (shouldShowRequestPermissionRationale(CAMERA))){
            Snackbar.make(null,"Los persmisos son necesarios",Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    requestPermissions(new String [] {WRITE_EXTERNAL_STORAGE,CAMERA},MY_PERMISSIONS);
                }
            }).show();
        }else{
            requestPermissions(new String [] {WRITE_EXTERNAL_STORAGE,CAMERA},MY_PERMISSIONS);
        }
        return false;
    }

    private void showOptions(){
        final CharSequence[] option = { "Elegir de galeria","Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(RegistroControl.this);
        builder.setTitle("Elige una opcion");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (option[which]=="Tomar foto") {
                    openCamera();

                }else if (option[which]=="Elegir de galeria"){
                    Intent intent = new Intent (Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent,"Selecciona la imgen"),SELECT_PICTURE);

                }else{
                    dialog.dismiss();
                }

            }
        });
        builder.show();
    }

    private void openCamera() {
        File file = new File(Environment.getExternalStorageDirectory(),MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if(!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();
        if(isDirectoryCreated){
            Long timestamp = System.currentTimeMillis()/100;
            String imageName = timestamp.toString()+".jpg";

            mPath = Environment.getExternalStorageState()+File.separator + MEDIA_DIRECTORY + File.separator+ imageName;

            File newFile = new File(mPath);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(newFile));
            startActivityForResult(intent, PHOTO_CODE);
        }

    }

private int Comprobar(){
        int contar=0;
    lenguaje = (CheckBox) findViewById(R.id.cbcLenguaje);
    ciencias = (CheckBox) findViewById(R.id.cbcCiencias);
    progra = (CheckBox) findViewById(R.id.cbcProgra);
    analisis = (CheckBox) findViewById(R.id.cbcAnalisis);
    fisica = (CheckBox) findViewById(R.id.cbcFisica);
    ingles = (CheckBox) findViewById(R.id.cbcIngles);


    if(lenguaje.isChecked()==true){
       contar=contar+1;
    }
    if(ciencias.isChecked()==true){
        contar=contar+1;
    }
    if(progra.isChecked()==true){
        contar=contar+1;
    }
    if(analisis.isChecked()==true){
        contar=contar+1;
    }
    if(fisica.isChecked()==true){
        contar=contar+1;
    }
    if(ingles.isChecked()==true){
        contar=contar+1;
    }
        return contar;
}



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("file_path",mPath);
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState);

        mPath = savedInstanceState.getString("file_path");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            switch (requestCode){
                case PHOTO_CODE:
                    MediaScannerConnection.scanFile(this, new String[]{mPath}, null, new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned" + path + ":");
                            Log.i("ExternalStorage", "-> Uri=: "+uri);
                        }
                    });
                    Bitmap bitmap = BitmapFactory.decodeFile(mPath);
                    mSetImage.setImageBitmap(bitmap);
                    break;


                case SELECT_PICTURE:
                    Uri path = data.getData();
                    mSetImage.setImageURI(path);
                   // try {
                    //    if(path!=null) {
                          //  json.put("Foto", path);
                     //   }else{
                           // json.put("Foto", "");
                     //   }
               // } catch (JSONException e) {
                 //       e.printStackTrace();
                   // }
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == MY_PERMISSIONS){
            if(grantResults.length==2 && grantResults[0]== PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){
                mOptionButton.setEnabled(true);
            }
        }
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
                Intent intent = new Intent ( RegistroControl.this, MainActivity.class);
                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

public void escribir(String datosN) throws IOException {
    FileWriter fichero = null;
    PrintWriter pw = null;

    FileReader fr = new FileReader(fileUsuarios.toString());

    String hola ="";

        int valor=fr.read();
        while(valor!=-1){
            hola+=((char)valor);
            valor=fr.read();
        }
        if(hola==""){
            Toast.makeText(getApplicationContext(),"Esta vacio",Toast.LENGTH_LONG).show();
            try {
                JSONObject nn2 = new JSONObject(datosN);
                JSONArray pp = new JSONArray();

                pp.put(nn2);
                fichero = new FileWriter(fileUsuarios);
                pw = new PrintWriter(fichero);
                pw.println(pp.toString());
                pw.flush();
                pw.close();


            }catch(JSONException e){
                e.printStackTrace();
            }



        }else if(!(hola =="")) {

            try {

                JSONArray prueba = new JSONArray(hola);
                JSONObject jn = new JSONObject(datosN);
                prueba.put(jn);
                String h = prueba.toString();
                fichero = new FileWriter(fileUsuarios);
                pw = new PrintWriter(fichero);
                pw.println(prueba.toString());
                pw.flush();
                pw.close();
               // Toast.makeText(getApplicationContext(),fileUsuarios.toString(), Toast.LENGTH_LONG).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }

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

