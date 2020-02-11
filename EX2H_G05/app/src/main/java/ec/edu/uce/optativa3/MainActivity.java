package ec.edu.uce.optativa3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.FaceAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import ec.edu.uce.controlador.RegistroUsuario;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Map;
import ec.edu.uce.geofencing.Constants;

public class MainActivity extends AppCompatActivity {
    private TextView usuario;
    private TextView clave;
    private TextView mensaje;
    private ImageView foto;
    private String imagenB64;
    private Button botonCamara;

    private static final String TAG = "MyFirebaseMsgService";
    private static final String TAG2 = MainActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    /**
     * Provides access to the Geofencing API.
     */
    private GeofencingClient mGeofencingClient;

    /**
     * The list of geofences used in this practice.
     */
    private ArrayList<Geofence> mGeofenceList;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

        mensaje = findViewById(R.id.txtMensaje);
        Servicios mm = new Servicios();
        String mms= mm.mensaje();
        mensaje.setText(mms);
        usuario = findViewById(R.id.txtUsuario);
        clave = findViewById(R.id.txtClave);

        //Geofencing
        // Empty list for storing geofences.
        mGeofenceList = new ArrayList<>();

        // Get the geofences used. Geofence data is hard coded in this sample.
        populateGeofenceList();

        mGeofencingClient = LocationServices.getGeofencingClient(this);

        mGeofencingClient = LocationServices.getGeofencingClient(this);


        SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
        usuario.setText(preferences.getString("usuario",""));
        clave.setText(preferences.getString("clave",""));

        Button btn = (Button) findViewById(R.id.btnRegistrarse);
        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        Button btnRecoFace = (Button) findViewById(R.id.btnFace);
        botonCamara = findViewById(R.id.btnFaceFoto);
        foto = findViewById(R.id.imgFace);

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

        btnRecoFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Reconocimiento();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        botonCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);
            }
        });

        String[] perms = { Manifest.permission.WRITE_SETTINGS, Manifest.permission.CAMERA};
        int accessS = checkSelfPermission(Manifest.permission.WRITE_SETTINGS);
        int cameraPermission = checkSelfPermission(Manifest.permission.CAMERA);
        int permsRequestCode = 100;
        Context context = getApplicationContext();
        boolean settingsCanWrite = Settings.System.canWrite(context);
        if(!settingsCanWrite) {
            // If do not have write settings permission then open the Can modify system settings panel.
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            startActivity(intent);
        }else {
            // If has permission then show an alert dialog with message.
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setMessage("You have system write settings permission now.");
            alertDialog.show();
        }

        if (cameraPermission == PackageManager.PERMISSION_GRANTED && accessS == PackageManager.PERMISSION_GRANTED)  {
            //se realiza metodo si es necesario...
        } else {
            requestPermissions(perms, permsRequestCode);

        }





      //  if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED ){
        //    ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA},0);
          //  ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.WRITE_SETTINGS},0);
       // }


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

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100:
                Toast.makeText(getApplicationContext(),"Permisos Consedidos",Toast.LENGTH_SHORT).show();
                break;
        }
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
                Toast.makeText(getApplicationContext(),"Binvenido "+usu,Toast.LENGTH_SHORT).show();
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

    public void Reconocimiento () throws IOException {

        Vision.Builder visionBuilder = new Vision.Builder(
                new NetHttpTransport(),
                new AndroidJsonFactory(),
                null);

        visionBuilder.setVisionRequestInitializer(
                new VisionRequestInitializer("AIzaSyCQZN926NF3OwezsmqN4ka-u-EzEb8kbv0"));
        final Vision vision = visionBuilder.build();


        // Create new thread
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {


                // Convert photo to byte array
                @SuppressLint("ResourceType") InputStream inputStream =
                        getResources().openRawResource(R.drawable.verito);
                @SuppressLint("ResourceType") InputStream nueva =
                        getResources().openRawResource(R.drawable.danny);
                @SuppressLint("ResourceType") InputStream nueva3 =
                        getResources().openRawResource(R.drawable.jordan);
                @SuppressLint("ResourceType") InputStream nueva4 =
                        getResources().openRawResource(R.drawable.santi);
                @SuppressLint("ResourceType") InputStream nueva5 =
                        getResources().openRawResource(R.drawable.marcos);

                try {

                    //ingreso
                    foto.buildDrawingCache();
                    Bitmap bmap = foto.getDrawingCache();

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] photoIngreso = stream.toByteArray();

                    //
                    byte[] photoData = IOUtils.toByteArray(inputStream);
                    inputStream.close();

                    byte[] photoNew = IOUtils.toByteArray(nueva);
                    nueva.close();

                    byte[] photoNew3 = IOUtils.toByteArray(nueva3);
                    nueva3.close();
                    byte[] photoNew4 = IOUtils.toByteArray(nueva4);
                    nueva4.close();
                    byte[] photoNew5 = IOUtils.toByteArray(nueva5);
                    nueva5.close();


                    //foto ingresada
                    Image inputFace = new Image();
                    inputFace.encodeContent(photoIngreso);
                    //fotos del grupo
                    Image inputImage = new Image();
                    inputImage.encodeContent(photoData);

                    Image inputNew = new Image();
                    inputNew.encodeContent(photoNew);

                    Image inputNew3 = new Image();
                    inputNew3.encodeContent(photoNew3);

                    Image inputNew4 = new Image();
                    inputNew4.encodeContent(photoNew4);

                    Image inputNew5 = new Image();
                    inputNew5.encodeContent(photoNew5);

                    Feature desiredFeature = new Feature();
                    desiredFeature.setType("FACE_DETECTION");

                    //ingreso
                    AnnotateImageRequest request0 = new AnnotateImageRequest();
                    request0.setImage(inputFace);
                    request0.setFeatures(Arrays.asList(desiredFeature));
                    //
                    AnnotateImageRequest request = new AnnotateImageRequest();
                    request.setImage(inputImage);
                    request.setFeatures(Arrays.asList(desiredFeature));

                    AnnotateImageRequest request1 = new AnnotateImageRequest();
                    request1.setImage(inputNew);
                    request1.setFeatures(Arrays.asList(desiredFeature));

                    AnnotateImageRequest request3 = new AnnotateImageRequest();
                    request3.setImage(inputNew3);
                    request3.setFeatures(Arrays.asList(desiredFeature));

                    AnnotateImageRequest request4 = new AnnotateImageRequest();
                    request4.setImage(inputNew4);
                    request4.setFeatures(Arrays.asList(desiredFeature));

                    AnnotateImageRequest request5 = new AnnotateImageRequest();
                    request5.setImage(inputNew5);
                    request5.setFeatures(Arrays.asList(desiredFeature));


                    //ingreso
                    BatchAnnotateImagesRequest batchRequest0 = new BatchAnnotateImagesRequest();
                    batchRequest0.setRequests(Arrays.asList(request0));
                    ///
                    BatchAnnotateImagesRequest batchRequest = new BatchAnnotateImagesRequest();
                    batchRequest.setRequests(Arrays.asList(request));

                    BatchAnnotateImagesRequest batchRequest1 = new BatchAnnotateImagesRequest();
                    batchRequest1.setRequests(Arrays.asList(request1));

                    BatchAnnotateImagesRequest batchRequest3 = new BatchAnnotateImagesRequest();
                    batchRequest3.setRequests(Arrays.asList(request3));

                    BatchAnnotateImagesRequest batchRequest4 = new BatchAnnotateImagesRequest();
                    batchRequest4.setRequests(Arrays.asList(request4));

                    BatchAnnotateImagesRequest batchRequest5 = new BatchAnnotateImagesRequest();
                    batchRequest5.setRequests(Arrays.asList(request5));

                    //ingreso
                    BatchAnnotateImagesResponse batchResponse0 =  vision.images().annotate(batchRequest0).execute();
                    //
                    BatchAnnotateImagesResponse batchResponse =  vision.images().annotate(batchRequest).execute();
                    BatchAnnotateImagesResponse batchResponse1 =  vision.images().annotate(batchRequest1).execute();
                    BatchAnnotateImagesResponse batchResponse3 =  vision.images().annotate(batchRequest3).execute();
                    BatchAnnotateImagesResponse batchResponse4 =  vision.images().annotate(batchRequest4).execute();
                    BatchAnnotateImagesResponse batchResponse5 =  vision.images().annotate(batchRequest5).execute();

                    //ingreso
                    final List<FaceAnnotation> facesIngreso = batchResponse0.getResponses()
                            .get(0).getFaceAnnotations();

                    //
                    final List<FaceAnnotation> faces = batchResponse.getResponses()
                            .get(0).getFaceAnnotations();

                    final List<FaceAnnotation> facesNew = batchResponse1.getResponses()
                            .get(0).getFaceAnnotations();

                    final List<FaceAnnotation> facesNew3 = batchResponse3.getResponses()
                            .get(0).getFaceAnnotations();

                    final List<FaceAnnotation> facesNew4 = batchResponse4.getResponses()
                            .get(0).getFaceAnnotations();

                    final List<FaceAnnotation> facesNew5 = batchResponse5.getResponses()
                            .get(0).getFaceAnnotations();

                    // Count faces
                    final int numberOfFaces = faces.size();
                    final int numberOfFacesNew = facesNew.size();

                    final String message =
                            "Son iguales This photo 1 has " + numberOfFaces + "This photo 2 has " + numberOfFacesNew ;

                    // Display toast on UI thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            float comp1 = facesIngreso.get(0).getLandmarkingConfidence()-facesNew.get(0).getLandmarkingConfidence();
                            float pointKey1 = Math.abs(comp1);

                            float comp2 = facesIngreso.get(0).getLandmarkingConfidence()-faces.get(0).getLandmarkingConfidence();
                            float pointKey2 = Math.abs(comp2);

                            float comp3 = facesIngreso.get(0).getLandmarkingConfidence()-facesNew3.get(0).getLandmarkingConfidence();
                            float pointKey3 = Math.abs(comp3);

                            float comp4 = facesIngreso.get(0).getLandmarkingConfidence()-facesNew4.get(0).getLandmarkingConfidence();
                            float pointKey4 = Math.abs(comp4);

                            float comp5 = facesIngreso.get(0).getLandmarkingConfidence()-facesNew5.get(0).getLandmarkingConfidence();
                            float pointKey5 = Math.abs(comp5);


                            System.out.println(facesIngreso.get(0).getLandmarkingConfidence());
                            System.out.println(faces.get(0).getLandmarkingConfidence());
                            System.out.println(facesNew.get(0).getLandmarkingConfidence());
                            if(pointKey1 < 0.15 || pointKey2 < 0.15 || pointKey3 < 0.15 || pointKey4 < 0.15 || pointKey5 < 0.15){

                                Intent intent = new Intent (MainActivity.this,Lista.class );

                                startActivity(intent);
                                Toast.makeText(getApplicationContext(),"Binvenido ",Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getApplicationContext(),"NO HAY COINCIDENCIA", Toast.LENGTH_LONG).show();
                            }

                           /* Toast.makeText(getApplicationContext(),
                                    message, Toast.LENGTH_LONG).show();*/

                        }
                    });


                } catch (IOException e) {
                    e.printStackTrace();
                }

                // More code here
            }
        });



    }

    //Geofencing
    /**
     * Builds and returns a GeofencingRequest. Specifies the list of geofences to be monitored.
     * Also specifies how the geofence notifications are initially triggered.
     */
    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
        // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
        // is already inside that geofence.
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);

        // Add the geofences to be monitored by geofencing service.
        builder.addGeofences(mGeofenceList);

        // Return a GeofencingRequest.
        return builder.build();
    }

    /**
     * This sample hard codes geofence data. A real app might dynamically create geofences based on
     * the user's location.
     */
    private void populateGeofenceList() {
        for (Map.Entry<String, LatLng> entry : Constants.AREA_LOCATION.entrySet()) {

            mGeofenceList.add(new Geofence.Builder()
                    // Set the request ID of the geofence. This is a string to identify this
                    // geofence.
                    .setRequestId(entry.getKey())

                    // Set the circular region of this geofence.
                    .setCircularRegion(
                            entry.getValue().latitude,
                            entry.getValue().longitude,
                            Constants.GEOFENCE_RADIUS_IN_METERS
                    )

                    // Set the expiration duration of the geofence. This geofence gets automatically
                    // removed after this period of time.
                    .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)

                    // Set the transition types of interest. Alerts are only generated for these
                    // transition. We track entry and exit transitions in this sample.
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)

                    // Create the geofence.
                    .build());
        }
    }


}
