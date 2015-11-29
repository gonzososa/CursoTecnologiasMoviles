package rutasver.transver.Actividades;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import rutasver.transver.Base_de_datos.AdaptadorBd;
import rutasver.transver.R;

public class RutaMapa extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private GoogleMap googleMap;
    Polyline linea1;
    Polyline linea2;
    FloatingActionButton fabmapa;
    AdaptadorBd dbadapter; /*Proporciona acceso a la base de datos se esta creando la instancia
    dbadapter de la clase Databaseadapter*/
    boolean apretado;
    boolean ambas_lineas=true;
    public static String busqueda;
    public Toolbar barra;//barra de herramientas
    Marker inicio_ida;
    Marker inicio_regreso;
    Marker fin_ida;
    Marker fin_regreso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruta_mapa);
        googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();
        fabmapa = (FloatingActionButton) findViewById(R.id.fab);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setTiltGesturesEnabled(false);
        barra = (Toolbar) findViewById(R.id.barra);// Asignándole el id a la barra
        setSupportActionBar(barra);//Poniendo barra cómo la action bar
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(busqueda);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if(savedInstanceState!=null){
            apretado=savedInstanceState.getBoolean("apretado");
            ambas_lineas=savedInstanceState.getBoolean("ambas_lineas");
        }
        buildGoogleApiClient();
        setupMap();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override //sobrescribe método del padre
    public void onSaveInstanceState(Bundle outState) { //método que es llamado antes de que la app sea destruida por ejemplo cuando se rota el dispositivo
        super.onSaveInstanceState(outState); //se le pasa al padre el bundle outState
        outState.putBoolean("apretado", apretado); //guardamos en el bundle una variable
        outState.putBoolean("ambas_lineas", ambas_lineas); //guardamos en el bundle una variable
    }

    public void setupMap(){

        //lista de arreglos del tipo latitud longitud
        final ArrayList<LatLng> coordenadas_ida;
        final ArrayList<LatLng> coordenadas_regreso;
        //se inicializa la instancia dbadapter con la clase DatabaseAdapter
        dbadapter = new AdaptadorBd(this);
        /*se iguala dbadapter con la llamada al metodo open que regresa la base
        de datos lista para escribir*/

        /*Cadena que se iguala con una llamada al metodo mostrar el cual recibe la cadena anterior
        y nos regresa una cadena con las coordenadas de la ruta*/
        coordenadas_ida = dbadapter.mostrar_ruta(busqueda,"ida");

        final PolylineOptions linea_azul = new PolylineOptions();//se crea una instancia de la clase PolylineOptions

        //crea la linea en las coordenadas indicadas
        linea_azul.addAll(coordenadas_ida)//con addAll se agrega la lista de arreglos coordenadas
                .width(4).color(Color.BLUE);//ancho y color de la linea

        linea1=googleMap.addPolyline(linea_azul); //agrega la linea al mapa

        final MarkerOptions markerOptions = new MarkerOptions();
        // agregando el marcador en la position tocada
        markerOptions.position(coordenadas_ida.get(0));
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marcadorinicio));
        markerOptions.title("Inicio");
        markerOptions.anchor(0,1);
        inicio_ida=googleMap.addMarker(markerOptions);
        markerOptions.position(coordenadas_ida.get(coordenadas_ida.size() - 1));
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marcadorfin));
        markerOptions.title("Fin");
        fin_ida=googleMap.addMarker(markerOptions);
        /*Cadena que se iguala con una llamada al metodo mostrar el cual recibe la cadena anterior
        y nos regresa una cadena con las coordenadas de la ruta*/
        coordenadas_regreso = dbadapter.mostrar_ruta(busqueda,"regreso");

        int latlong_inical= coordenadas_ida.size()/2;

        final PolylineOptions linea_roja = new PolylineOptions();

        //crea la linea en las coordenadas indicadas
        linea_roja.addAll(coordenadas_regreso)
                .width(4).color(Color.RED);

        //mueve la camara
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(coordenadas_ida.get(latlong_inical).latitude, coordenadas_ida.get(latlong_inical).longitude), 13.0f));

        fabmapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!apretado) {
                    fabmapa.setColorPressed(Color.BLUE); //color del fab botón presionado
                    fabmapa.setColorNormal(Color.BLUE); //color del fab botón normal
                    apretado = true;
                    linea1.remove();
                    inicio_ida.remove();
                    fin_ida.remove();
                    linea2=googleMap.addPolyline(linea_roja); //agrega la linea al mapa
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marcadorfin));
                    markerOptions.position(coordenadas_regreso.get(coordenadas_regreso.size() - 1));
                    markerOptions.title("Fin");
                    fin_regreso=googleMap.addMarker(markerOptions);
                    markerOptions.position(coordenadas_regreso.get(0));
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marcadorinicio));
                    markerOptions.title("Inicio");
                    inicio_regreso=googleMap.addMarker(markerOptions);
                    //Toast.makeText(this, "Regreso", Toast.LENGTH_SHORT).show();
                } else {
                    fabmapa.setColorPressed(Color.RED);
                    fabmapa.setColorNormal(Color.RED);
                    apretado = false;
                    linea2.remove();
                    inicio_regreso.remove();
                    fin_regreso.remove();
                    linea1=googleMap.addPolyline(linea_azul);
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marcadorfin));
                    markerOptions.position(coordenadas_ida.get(coordenadas_ida.size() - 1));
                    markerOptions.title("Fin");
                    fin_ida=googleMap.addMarker(markerOptions);
                    markerOptions.position(coordenadas_ida.get(0));
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marcadorinicio));
                    markerOptions.title("Inicio");
                    inicio_ida=googleMap.addMarker(markerOptions);
                    //Toast.makeText(this, "Ida", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        settingsrequest();
    }

    public void settingsrequest()
    {
        mGoogleApiClient.connect();
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        googleMap.setMyLocationEnabled(true);
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(RutaMapa.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // activa la capa de mi Ubicacion de Google Maps
                        googleMap.setMyLocationEnabled(true);
                        break;
                }
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
