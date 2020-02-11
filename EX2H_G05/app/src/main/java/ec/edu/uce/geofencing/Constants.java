package ec.edu.uce.geofencing;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

public final class Constants {

    private Constants() {
    }

    private static final String PACKAGE_NAME = "ec.edu.uce.geofencing";

    public static final String GEOFENCES_ADDED_KEY = PACKAGE_NAME + ".GEOFENCES_ADDED_KEY";

    /**
     * Se coloca un tiempo de vencimiento para el geofence
     * despues de eso dejar de utilizar los servicios de geofence.
     */
    private static final long GEOFENCE_EXPIRATION_IN_HOURS = 4;


    public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS =
            GEOFENCE_EXPIRATION_IN_HOURS * 60 * 60 * 1000;
    /*Se coloca el radio en metros*/
    public static final float GEOFENCE_RADIUS_IN_METERS = 30;

    /**
     * Colocamos los lugares que vamos a utilizar en nuestra aplicacion.
     */
    public static final HashMap<String, LatLng> AREA_LOCATION = new HashMap<>();

    static {
        // Edificio de Aulas.
        AREA_LOCATION.put("EDFA", new LatLng(-0.198290, -78.504031));

        // Edificio de laboratorios.
        AREA_LOCATION.put("EDFB", new LatLng(-0.198695, -78.502937));
    }
}
