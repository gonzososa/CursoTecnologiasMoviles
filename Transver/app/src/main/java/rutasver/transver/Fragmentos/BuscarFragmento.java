package rutasver.transver.Fragmentos;

import android.content.Context;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import rutasver.transver.Base_de_datos.AdaptadorBd;
import rutasver.transver.R;

public class BuscarFragmento extends Fragment implements LocationListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapClickListener{

    public static final String KEY_MAP_FRAGMENT_STATE = "rutasver.transver.Fragmentos.BuscarFragmento";
    Context contexto;
    MapView mMapView;
    private GoogleMap googleMap;
    public static boolean hayGPS;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 100;
    LocationManager locationManager;
    DialogMapa dialogMapa;
    LatLng latlang_cercanas;
    AdaptadorBd base_de_datos;
    Polyline linea1;
    Polyline linea2;
    boolean haylinea;
    boolean haycercanas;
    boolean hayorigen;
    boolean haydestino;
    Marker marcador_cerca;
    Marker marcador_origen;
    Marker marcador_destino;
    Marker inicio_ida;
    Marker inicio_regreso;
    Marker fin_ida;
    Marker fin_regreso;
    FloatingActionButton fabmapa;
    boolean apretado;
    int rebotes;
    String lista_cercanas_origen[];
    String lista_cercanas_destino[];
    String resultado_origen_destino[];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(getMapFragmentState(savedInstanceState));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate and return the layout
        View v = inflater.inflate(R.layout.fragment_buscar, container,
                false);
        mMapView = (MapView) v.findViewById(R.id.mapView);

        mMapView.onCreate(getMapFragmentState(savedInstanceState));

        mMapView.onResume();// needed to get the map to display immediately
        contexto= getActivity();

        fabmapa = (FloatingActionButton) v.findViewById(R.id.fab);
        fabmapa.hide(false);

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(savedInstanceState!=null){
            latlang_cercanas= new LatLng(savedInstanceState.getDouble("latitude"),savedInstanceState.getDouble("longitude"));
        }
        //se inicializa la instancia dbadapter con la clase DatabaseAdapter
        base_de_datos = new AdaptadorBd(getActivity());
        googleMap = mMapView.getMap(); //obtiene la vista del mapa
        googleMap.setOnMapLongClickListener(this); //habilitamos el listener del click prolongado del mapa
        googleMap.setOnMapClickListener(this); //habilitamos el listener del click del mapa
        googleMap.setOnMarkerClickListener(this); //habilitamos el listener del click de marcadores
        googleMap.getUiSettings().setMapToolbarEnabled(false); //deshabilita la barra del mapa que sale cuando das click a un marcador
        googleMap.getUiSettings().setTiltGesturesEnabled(false); //deshabilita el tilt del mapa
        ubicacion();
        return v;
    }

    public void ubicacion(){

        if(!hayGPS){
            LatLng latLng = new LatLng(19.177021, -96.154955);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 12);
            googleMap.moveCamera(cameraUpdate);
        }else{
            // Get LocationManager object from System Service LOCATION_SERVICE
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            // Create a criteria object to retrieve provider
            Criteria criteria = new Criteria();

            // Get the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);

            Location mylocation = locationManager.getLastKnownLocation(provider);
            if(mylocation==null) {
                locationManager.requestLocationUpdates(provider, MIN_TIME, MIN_DISTANCE, this); //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER
                LatLng latLng = new LatLng(19.177021, -96.154955);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 12);
                googleMap.moveCamera(cameraUpdate);
            }
            else{
                googleMap.setMyLocationEnabled(true);
                LatLng latLng = new LatLng(mylocation.getLatitude(), mylocation.getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
                googleMap.moveCamera(cameraUpdate);
            }

        }
    }

    @Override
    public void onStop(){
        super.onStop();
        googleMap.setMyLocationEnabled(false);
    }


    @Override
    public void onLocationChanged(Location location) {
        googleMap.setMyLocationEnabled(true);
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
        googleMap.animateCamera(cameraUpdate);
        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        FragmentManager fm = getChildFragmentManager();
        dialogMapa = new DialogMapa();
        dialogMapa.show(fm, "dialog_mapa");
        latlang_cercanas=latLng;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(marker.equals(marcador_cerca)){
            getActionBar().setTitle(R.string.mapa);
            final FragmentManager fm = getChildFragmentManager();
            final DialogCercanas dialogCercanas = new DialogCercanas();
            dialogCercanas.latlang_cercanas=latlang_cercanas;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    dialogCercanas.show(fm, "dialog_mapa");
                }
            };

            Handler handler = new Handler();

            handler.postDelayed(runnable, 300);

            if(haylinea){
                linea1.remove();
                inicio_ida.remove();
                fin_ida.remove();
            }
            else {
                linea2.remove();
                inicio_regreso.remove();
                fin_regreso.remove();
            }
        }
        else if(marker.equals(marcador_origen)||marker.equals(marcador_destino)){
            getActionBar().setTitle(R.string.mapa);
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    dialog_origen_destino();
                }
            };
            Handler h = new Handler();
            h.postDelayed(r,300);
            if(haylinea){
                linea1.remove();
                inicio_ida.remove();
                fin_ida.remove();
            }
            else {
                linea2.remove();
                inicio_regreso.remove();
                fin_regreso.remove();
            }
        }
        return false;
    }

    public void opciones(int eleccion){
        switch (eleccion){
            case 0:
            case 1:
            case 2:
                marcadores(latlang_cercanas, eleccion);
                break;
        }
    }

    public void dialog_cercanas(int eleccion){
        FragmentManager fm = getChildFragmentManager();
        DialogCercanas dialogCercanas = new DialogCercanas();
        dialogCercanas.latlang_cercanas=latlang_cercanas;
        dialogCercanas.show(fm, "dialog_mapa");
        haycercanas=true;
    }

    public void dialog_origen_destino(){
        FragmentManager fm = getChildFragmentManager();
        Dialog_Origen_Destino dialogOrigenDestino = new Dialog_Origen_Destino();
        dialogOrigenDestino.resultado_origen_destino=resultado_origen_destino;
        dialogOrigenDestino.show(fm, "dialog_mapa");
        haycercanas=true;
    }

    public void marcadores(LatLng latLng,int eleccion){

        // creando el marcador
        MarkerOptions markerOptions = new MarkerOptions();
        // agregando el marcador en la position tocada
        markerOptions.position(latLng);

        switch(eleccion){
            case 0:
                if(hayorigen) //si ya existe un marcador de origen se borra para agregar el nuevo
                    marcador_origen.remove(); //quitamos el marcador de origen
                else if(haycercanas) {//si existe un marcador de buscar cercanas
                    googleMap.clear(); //limpiamos el mapa
                    fabmapa.hide(true); //escondemos el fab button con animación
                }
                haycercanas=false;
                // titulo del marcador
                // sale arriba del marcador
                markerOptions.title("Origen");
                hayorigen=true; //hay un punto de origen
                // Agregando el marcador a la posicion tocada
                marcador_origen=googleMap.addMarker(markerOptions);
                lista_cercanas_origen = base_de_datos.rutas_cercanas(latLng);
                if(haydestino){
                    if(lista_cercanas_origen.length>0){
                        int k=0;
                        for(int i =0;i<lista_cercanas_origen.length;i++){
                            for(int j=0;j<lista_cercanas_destino.length;j++){
                                if(lista_cercanas_origen[i].equals(lista_cercanas_destino[j])){
                                    k++;
                                }
                            }
                        }
                        resultado_origen_destino = new String[k];
                        k=0;
                        for(int i =0;i<lista_cercanas_origen.length;i++){
                            for(int j=0;j<lista_cercanas_destino.length;j++){
                                if(lista_cercanas_origen[i].equals(lista_cercanas_destino[j])){
                                    resultado_origen_destino[k]=lista_cercanas_origen[i];
                                    k++;
                                }
                            }
                        }
                        if(resultado_origen_destino.length>0){
                            dialog_origen_destino();
                        }
                        else {
                            Snackbar.make(getView(),"Ninguna ruta cercana",Snackbar.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Snackbar.make(getView(),"Ninguna ruta cercana",Snackbar.LENGTH_LONG).show();
                    }
                }
                else{
                    Snackbar.make(getView(),"Agregue destino",Snackbar.LENGTH_LONG).show();
                }
                break;
            case 1:
                if(haydestino) //si ya existe un marcador de destino se borra para agregar el nuevo
                    marcador_destino.remove(); //quitamos el marcador de destino
                else if(haycercanas) {//si existe un marcador de buscar cercanas
                    googleMap.clear(); //limpiamos el mapa
                    fabmapa.hide(true); //escondemos el fab button con animación
                }
                haycercanas=false;
                markerOptions.title("Destino");
                haydestino=true;
                marcador_destino=googleMap.addMarker(markerOptions);
                lista_cercanas_destino = base_de_datos.rutas_cercanas(latLng);
                if(hayorigen){
                    if(lista_cercanas_destino.length>0){
                        int k=0;
                        for(int i =0;i<lista_cercanas_origen.length;i++){
                            for(int j=0;j<lista_cercanas_destino.length;j++){
                                if(lista_cercanas_origen[i].equals(lista_cercanas_destino[j])){
                                    k++;
                                }
                            }
                        }
                        resultado_origen_destino = new String[k];
                        k=0;
                        for(int i =0;i<lista_cercanas_origen.length;i++){
                            for(int j=0;j<lista_cercanas_destino.length;j++){
                                if(lista_cercanas_origen[i].equals(lista_cercanas_destino[j])){
                                    resultado_origen_destino[k]=lista_cercanas_origen[i];
                                    k++;
                                }
                            }
                        }
                        if(resultado_origen_destino.length>0){
                            dialog_origen_destino();
                        }
                        else {
                            Snackbar.make(getView(),"Ninguna ruta cercana",Snackbar.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Snackbar.make(getView(),"Ninguna ruta cercana",Snackbar.LENGTH_LONG).show();
                    }
                }
                else{
                    Snackbar.make(getView(),"Agregue origen",Snackbar.LENGTH_LONG).show();
                }
                break;
            case 2:
                if(hayorigen||haycercanas||haydestino)
                    googleMap.clear();
                hayorigen=false;
                haydestino=false;
                haycercanas=true;
                // titulo del marcador
                // sale arriba del marcador
                markerOptions.title("Rutas Cercanas");
                // Agregando el marcador a la posicion tocada
                marcador_cerca=googleMap.addMarker(markerOptions);
                dialog_cercanas(eleccion);
                break;
        }
    }

    public void dibujar_rutas(String nombre){
        rebotes=0;
        if(marcador_cerca!=null)
            setMarkerBounce(marcador_cerca);
        if(marcador_destino!=null){
            setMarkerBounce(marcador_destino);
        }

        fabmapa.show(true);
        getActionBar().setTitle(nombre);
        haylinea = true;
        //lista de arreglos del tipo latitud longitud
        final ArrayList<LatLng> coordenadas_ida;
        final ArrayList<LatLng> coordenadas_regreso;
        /*se iguala dbadapter con la llamada al metodo open que regresa la base
        de datos lista para escribir*/

        /*Cadena que se iguala con una llamada al metodo mostrar el cual recibe la cadena anterior
        y nos regresa una cadena con las coordenadas de la ruta*/
        coordenadas_ida = base_de_datos.mostrar_ruta(nombre,"ida");

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
        coordenadas_regreso = base_de_datos.mostrar_ruta(nombre,"regreso");

        int latlong_inical= coordenadas_ida.size()/2;

        final PolylineOptions linea_roja = new PolylineOptions();

        //crea la linea en las coordenadas indicadas
        linea_roja.addAll(coordenadas_regreso)
                .width(4).color(Color.RED);

        //mueve la camara
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(coordenadas_ida.get(latlong_inical).latitude, coordenadas_ida.get(latlong_inical).longitude), 13.0f));

        fabmapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!apretado) {
                    fabmapa.setColorPressed(Color.BLUE); //color del fab botón presionado
                    fabmapa.setColorNormal(Color.BLUE); //color del fab botón normal
                    apretado = true;
                    haylinea=false;
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
                } else {
                    fabmapa.setColorPressed(Color.RED);
                    fabmapa.setColorNormal(Color.RED);
                    apretado = false;
                    haylinea=true;
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
                }
            }
        });
    }

    @Override //sobrescribe método del padre
    public void onSaveInstanceState(Bundle outState) { //método que es llamado antes de que la app sea destruida por ejemplo cuando se rota el dispositivo
        super.onSaveInstanceState(outState); //se le pasa al padre el bundle outState
        if(latlang_cercanas!=null){
            outState.putDouble("latitude", latlang_cercanas.latitude); //guardamos en el bundle una variable
            outState.putDouble("longitude", latlang_cercanas.longitude); //guardamos en el bundle una variable
        }
        Bundle mapFragmentState = new Bundle();
        super.onSaveInstanceState(mapFragmentState);
        outState.putBundle(KEY_MAP_FRAGMENT_STATE, mapFragmentState);
    }

    private Bundle getMapFragmentState(Bundle savedInstanceState) {
        return savedInstanceState != null ? savedInstanceState.getBundle(KEY_MAP_FRAGMENT_STATE) : null;
    }

    private void setMarkerBounce(final Marker marker) {
        final Handler handler = new Handler();
        final long startTime = SystemClock.uptimeMillis();
        final long duration = 2000;
        final Interpolator interpolator = new BounceInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - startTime;
                float t = Math.max(1 - interpolator.getInterpolation((float) elapsed / duration), 0);
                marker.setAnchor(0.5f, 1f + t);

                if (t > 0.0) {
                    handler.postDelayed(this, 16);
                } else if(rebotes<1){
                    rebotes++;
                    setMarkerBounce(marker);
                    marker.showInfoWindow();
                }
            }
        });
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Snackbar
                .make(getView(),"Mantenga presionado en el mapa", Snackbar.LENGTH_LONG)
                .show();
    }

    private ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }
}
