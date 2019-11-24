package ec.edu.uce.optativa3.controlador;

import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ListaControl {
    private File fileUsuarios;
    private String carpeta = "/Download/Usuarios";

    public void mensajePut() throws IOException, JSONException {
        String jsonMensaje = "https://api-rest-grupo05.herokuapp.com/toXml";

        String path= (Environment.getExternalStorageDirectory()+this.carpeta);
        File directorio = new File(path);
        String name = "usuarios.txt";
        this.fileUsuarios = new File(directorio,name);

        FileWriter fichero = null;
        PrintWriter pw = null;

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
            valor=fr.read();
        }


        JSONArray jrray = new JSONArray(datosDD);
        String estudiantes=jrray.toString();



        URL url = null;
        HttpURLConnection conn;

        try {
            url = new URL(jsonMensaje);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.getOutputStream().write(estudiantes.getBytes());
            conn.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
