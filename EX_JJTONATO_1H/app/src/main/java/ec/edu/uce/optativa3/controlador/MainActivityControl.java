package ec.edu.uce.optativa3.controlador;

import android.content.Context;

import android.content.SharedPreferences;
import android.os.Environment;
import android.os.StrictMode;


import androidx.appcompat.app.AppCompatActivity;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivityControl extends AppCompatActivity {
    private String carpeta = "/Download/Usuarios";
    private File fileUsuarios;
    //Para acceder
    public boolean acceder(String user, String clave) throws JSONException, IOException {

        boolean bandera=false;
        String path= (Environment.getExternalStorageDirectory()+this.carpeta);
        File directorio = new File(path);
        String name = "usuarios.txt";
        this.fileUsuarios = new File(directorio,name);

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
            if((obj.getString("Usuario").equals(user))&&(obj.getString("Clave").equals(clave))){

               bandera=true;
               break;
            }else{

                bandera=false;
            }
        }
        return bandera;

    }


}
