package ec.edu.uce.optativa3.vista;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tarea_05_g05.R;

import org.json.JSONException;

import java.io.IOException;

import ec.edu.uce.optativa3.controlador.ListaControl;

public class MyTest extends AppCompatActivity {
    private Button miBtn;
    private EditText txtIn;
    private TextView texto;
    private Button regresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_layout_practica);
        androidx.appcompat.widget.Toolbar toolbarMenu = findViewById(R.id.menu);
        setSupportActionBar(toolbarMenu);
        txtIn = (EditText) findViewById(R.id.editIngreso);
        texto = (TextView) findViewById(R.id.txtResult);
        miBtn = (Button) findViewById(R.id.myButton);
        regresar = (Button) findViewById(R.id.myButton2);
        miBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = txtIn.getText().toString();
                if (!msg.isEmpty()){
                    texto.setText(msg);
                }else{
                    Toast.makeText(getApplicationContext(), "No hay info", Toast.LENGTH_LONG).show();

                }
                Log.d("MyTestClass: ","Este es mi mensajes");
                //Intent intent = new Intent (MyTest.this, MainActivity.class);
               // startActivity(intent);
            }
        });

        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("MyTestClass: ","Regresando");
                Intent intent = new Intent (MyTest.this, MainActivity.class);
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
                    String mm = lc.mensajePut();
                    Toast.makeText(getApplicationContext(), mm, Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent ( MyTest.this, MainActivity.class);
                startActivity(intent);

                break;

        }
        return super.onOptionsItemSelected(item);
    }



}
