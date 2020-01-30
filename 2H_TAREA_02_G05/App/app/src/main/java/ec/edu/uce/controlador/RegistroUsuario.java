package ec.edu.uce.controlador;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ec.edu.uce.optativa3.MainActivity;
import ec.edu.uce.optativa3.Servicios;
import ec.edu.uce.optativa3.R;
import ec.edu.uce.optativa3.SqlLite;

public class RegistroUsuario extends AppCompatActivity {

    private TextView usuario;
    private TextView clave;
    private Button registrar;
    private TextView mensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_usuario);

        mensaje= findViewById(R.id.txtMensaje);
        Servicios mm = new Servicios();
        String mms= mm.mensaje();
        mensaje.setText(mms);
        usuario = findViewById(R.id.txtUsuario);
        clave = findViewById(R.id.txtClave);
        registrar = findViewById(R.id.btnGuardar);
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registroUsu();
            }
        });
    }

    public void registroUsu(){

        SqlLite admin = new SqlLite(this,"administracion",null,1);
        SQLiteDatabase BaseDatos = admin.getWritableDatabase();
        String usuarioT = usuario.getText().toString();
        String claveT = clave.getText().toString();


        if(!usuarioT.isEmpty() && !claveT.isEmpty()){


            ContentValues datosUsuarios = new ContentValues();
            datosUsuarios.put("usuario",usuarioT);
            datosUsuarios.put("password",claveT);


            BaseDatos.insert("USUARIOS",null,datosUsuarios);
            BaseDatos.close();
            usuario.setText("");
            clave.setText("");
            Intent intent = new Intent (this, MainActivity.class );
            startActivity(intent);
            Toast.makeText(getApplicationContext(),"Usuaeio: "+usuarioT+" registrado exitosamente",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(),"Existen campos vacios",Toast.LENGTH_SHORT).show();
        }
    }
}
