package ec.edu.uce.optativa3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import ec.edu.uce.controlador.RegistroUsuario;

public class MainActivity extends AppCompatActivity {
    private TextView usuario;
    private TextView clave;
    private TextView mensaje;

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mensaje = findViewById(R.id.txtMensaje);
        Servicios mm = new Servicios();
        String mms= mm.mensaje();
        mensaje.setText(mms);
        usuario = findViewById(R.id.txtUsuario);
        clave = findViewById(R.id.txtClave);

        SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
        usuario.setText(preferences.getString("usuario",""));
        clave.setText(preferences.getString("clave",""));

        Button btn = (Button) findViewById(R.id.btnRegistrarse);
        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Buscar();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (MainActivity.this, RegistroUsuario.class );
                startActivity(intent);

                FirebaseInstanceId.getInstance().getInstanceId()
                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                if (!task.isSuccessful()) {
                                    Log.w(TAG, "getInstanceId failed", task.getException());
                                    return;
                                }

                                // Get new Instance ID token
                                String token = task.getResult().getToken();

                                // Log and toast
                                String msg = getString(R.string.msg_token_fmt, token);
                                Log.d(TAG, msg);
                                //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    public void Buscar(){
        SqlLite admin = new SqlLite(this,"administracion",null,1);
        SQLiteDatabase BaseDatos = admin.getWritableDatabase();

        String usu=usuario.getText().toString();
        String pss= clave.getText().toString();
        String pss1;

        Cursor fila = BaseDatos.rawQuery("select password from USUARIOS where usuario='"+usu+"'",null);

        if(fila.moveToFirst()){
            pss1 = fila.getString(0);

            if(pss1.equals(pss)){
                preferencias(usuario.getText().toString(), clave.getText().toString());
                Intent intent = new Intent (MainActivity.this,Lista.class );
                intent.putExtra("usuario", usu);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"Bienvenido "+usu,Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(),"Clave incorrecta",Toast.LENGTH_SHORT).show();
            }
        }else{

            Toast.makeText(getApplicationContext(),"No existe el usuario",Toast.LENGTH_SHORT).show();

        }

        BaseDatos.close();

    }

    private void preferencias(String user, String pass) {

        SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor Obj_editor = preferences.edit();
        Obj_editor.putString("usuario",user);
        Obj_editor.putString("clave",pass);
        Obj_editor.commit();

    }



}
