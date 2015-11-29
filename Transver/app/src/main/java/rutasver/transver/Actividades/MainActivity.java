package rutasver.transver.Actividades;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.ColorStateList;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
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
import rutasver.transver.Fragmentos.BuscarFragmento;
import rutasver.transver.Fragmentos.DialogMapa;
import rutasver.transver.Fragmentos.DialogCercanas;
import rutasver.transver.Fragmentos.Dialog_Origen_Destino;
import rutasver.transver.Fragmentos.RutasFragmento;
import rutasver.transver.R;


/*actividad principal extiende appcompat para
/dar compatibilidad desde api 14*/
public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        DialogMapa.ListenerdeOnclick,
        DialogCercanas.ListenerdeOnclick,
        Dialog_Origen_Destino.ListenerdeOnclick{

    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    public SearchView searchView;
    public Toolbar barra;//barra de herramientas
    public NavigationView vistaNavigation; //la vista del nav drawer
    ActionBarDrawerToggle actionBarDrawerToggle; //clase encargada de proveer métodos para el uso en conjunto del action bar y nav drawer
    private DrawerLayout drawerLayout; //contenedor donde va el nav drawer
    public boolean semuestraCamiones; //variable booleana para controlar que vista se muestra
    boolean semuestraRuta; //variable booleana para controlar que vista se muestra
    boolean semuestraMapa; //variable booleana para controlar que vista se muestra
    String titulo; //titulo para la barra
    ColorStateList iconColor; //colores para iconos del nav drawer
    ColorStateList textColor; //colores para texto del nav
    public int eleccion;

    @Override//sobrescribe del padre
    protected void onCreate(Bundle savedInstanceState) { //cuando se crea la app recibe bundle para pasar estados
        super.onCreate(savedInstanceState); /*esto le manda al padre el estado en el bundle
        , permitiendo que se corra nuestro código y el del padre*/
        setContentView(R.layout.activity_main);//asigna la vista al layout correspondiente
        barra = (Toolbar) findViewById(R.id.barra);// Asignándole el id a la barra
        setSupportActionBar(barra);//Poniendo barra cómo la action bar

        if(savedInstanceState==null){ //se checa si savedInstanceState es null lo cual se cumple cuando se abre la app por primera vez
            cambiarFragmento(0); //llama al método cambiarFragmento se la pasa 0 ya que es la posición del primer fragmento (RutasFragmento)
        }
        else { //si savedInstanceState!=null recuperamos los datos salvados
            semuestraCamiones =savedInstanceState.getBoolean("semuestraCamiones"); //asignamos a la variable correspondiente el valor guardado en savedInstanceState
            semuestraMapa=savedInstanceState.getBoolean("semuestraMapa"); //asignamos a la variable correspondiente el valor guardado en savedInstanceState
            semuestraRuta=savedInstanceState.getBoolean("semuestraRuta"); //asignamos a la variable correspondiente el valor guardado en savedInstanceState
            titulo=savedInstanceState.getString("titulo"); //asignamos a la variable correspondiente el valor guardado en savedInstanceState
            assert getSupportActionBar() != null; //cómo la barra no será nula le indicamos al compilador que getSupportActionBar no puede ser nulo
            getSupportActionBar().setTitle(titulo); //asignando a la barra el título correspondiente
        }

        vistaNavigation = (NavigationView) findViewById(R.id.vista_navegacion); //inicializando vistaNavigation
        coloresNavigation(); //método que agrega los colores respectivos al texto e iconos del nav drawer
        vistaNavigation.setItemIconTintList(iconColor);//aplica un tinte a iconos
        vistaNavigation.setItemTextColor(textColor);//aplica un tinte a texto

        if(semuestraCamiones||semuestraRuta) //if que checa si se esta mostrando el fragmento rutas
            vistaNavigation.getMenu().getItem(0).setChecked(true); //obtiene el el elemento 0 del menu del nav drawer
        else if(semuestraMapa) //if que checa si se esta mostrando el fragmento mapa
            vistaNavigation.getMenu().getItem(1).setChecked(true); //obtiene el el elemento 1 del menu del nav drawer

        //llamando al listener de Nav View para saber que elemento fue seleccionado
        vistaNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // Este método va a activar el on item Click en los elementos del navigation drawer
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Comprobando si el elemento esta en estado activo o no, si no lo esta se activa
                if (menuItem.isChecked())
                    menuItem.setChecked(false);
                else if(!menuItem.getTitle().equals("Acerca"))
                    menuItem.setChecked(true);

                //Cerrando en nav drawer después del click
                drawerLayout.closeDrawers();

                //Se comprueba a que elemento se le dio click y se realiza la acción necesaria
                switch (menuItem.getItemId()) {

                    case R.id.rutas: //cuando se selecciona rutas
                        if(!semuestraCamiones){
                            Runnable r = new Runnable() { //se crea una instancia de Runnable
                                @Override //sobrescribe del padre
                                public void run(){ //método que ejecuta código
                                    onBackPressed();
                                }
                            };

                            Handler h = new Handler(); //se crea una instancia de handler para ejecutar el runnable a futuro
                            h.postDelayed(r, 500); //se ejecuta el handler con el Runnable después de 500 milisegundos
                            getSupportActionBar().setTitle("Rutas"); //obtén la barra y asigna el titulo
                            vistaNavigation.getMenu().getItem(0).setChecked(true); //obtiene el el elemento 1 del menu del nav drawer
                            semuestraCamiones=true;
                        }
                        return true; //retornamos true para salir del listener
                    case R.id.mapa: //cuando se selecciona mapa
                        Runnable r = new Runnable() { //se crea una instancia de Runnable
                            @Override //sobrescribe del padre
                            public void run(){ //método que ejecuta código
                                buildGoogleApiClient();
                            }
                        };
                        Handler h = new Handler(); //se crea una instancia de handler para ejecutar el runnable a futuro
                        h.postDelayed(r, 500); //se ejecuta el handler con el Runnable después de 500 milisegundos
                        return true; //retornamos true para salir del listener
                    case R.id.acerca: //cuando se selecciona acerca
                        cambiarFragmento(2);
                        return true; //retornamos true para salir del listener
                    default: //si existe algún problema retornamos true para salir del listener
                        return true;

                }
            }
        });

        //inicializando Drawer Layout e ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,barra,R.string.openDrawer, R.string.closeDrawer){

            @Override //sobrescribe método del padre
            public void onDrawerClosed(View drawerView) { //cuando se cierra el drawer
                super.onDrawerClosed(drawerView); //se pasa al padre drawerView
            }

            @Override //sobrescribe método del padre
            public void onDrawerOpened(View drawerView) { //cuando se abre el drawer
                super.onDrawerOpened(drawerView); //se pasa al padre drawerView
                super.onDrawerSlide(drawerView, 0); // desactiva la animación de la flecha-hamburger
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, 0); // desactiva la animación de la flecha-hamburger
            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle); //asignando el actionbarToggle al drawer

        actionBarDrawerToggle.syncState(); //llamada a syncState es necesario para que salga el botón de la hamburguesa
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_busqueda, menu);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    @Override //sobrescribe método del padre
    protected void onResume() { //método llamado después de onCreate->onStart->onResume
        super.onResume(); //llamado a onResume del padre
        hayGooglePlayServices(); /*método encargado de revisar si google play services esta disponible
        esta app no funciona sin google play services ya que funciona con google maps el cual es parte
        de los google play services*/
    }

    @Override //sobrescribe método del padre
    public void onBackPressed() { //método que es llamado cuando el botón atrás es seleccionado en el teléfono
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){ //si el drawer esta abierto (se pone START por que comienza en la derecha)
            drawerLayout.closeDrawers(); //llamada a cerrar el drawer
        }
        else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) { //si la cuenta de fragmentos en el stack es mayor que 0
                getSupportFragmentManager().popBackStack(getSupportFragmentManager().getBackStackEntryAt(0).getId(),
                        getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);
                //hacemos pop del fragmento que esta en la posición 0 del ().setTitle("Rutas"); //obtén la barra y asigna el titulo
                vistaNavigation.getMenu().getItem(0).setChecked(true); //obtiene el el elemento 1 del menu del nav drawer
                semuestraCamiones=true;
                titulo="Rutas";
                getSupportActionBar().setTitle(titulo); //obtén la barra y asigna el titulo
                searchView.setVisibility(View.VISIBLE);
            }
            else if(semuestraCamiones&&!RutasFragmento.esta_buscando)
                super.onBackPressed();
            else if(RutasFragmento.hay_texto)
                searchView.setQuery("",true);
            else
                searchView.setIconified(true);
        }
    }

    @Override //sobrescribe método del padre
    public void onSaveInstanceState(Bundle outState) { //método que es llamado antes de que la app sea destruida por ejemplo cuando se rota el dispositivo
        super.onSaveInstanceState(outState); //se le pasa al padre el bundle outState
        outState.putBoolean("semuestraCamiones", semuestraCamiones); //guardamos en el bundle una variable
        outState.putBoolean("semuestraMapa", semuestraMapa); //guardamos en el bundle una variable
        outState.putBoolean("semuestraRuta", semuestraRuta); //guardamos en el bundle una variable
        outState.putString("titulo", titulo); //guardamos en el bundle una variable
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    private boolean hayGooglePlayServices() //método encargado de revisar si hay google play services
    {
        int services = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this); //entero que recibe si google play services esta disponible
        if (!(services == ConnectionResult.SUCCESS)){ /*si services es diferente a SUCCESS (éxito) es decir si no hay google play services o no esta actualizado
           a la última versión entra a esta condición para manejar las diferentes posibilidades*/
            if (GoogleApiAvailability.getInstance().isUserResolvableError(services)) { /*condición que checa si la inexistencia o falta de actualización
                de google play services puede ser resuelto por el usuario es decir si puede instalar o actualizar*/
                Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(this,services,0); //se crea una ventana de dialogo con el error correspondiente
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() { //listener del dialog que detecta si el usuario toca fuera de la ventana de dialog
                    @Override //sobrescribe método del padre
                    public void onCancel(DialogInterface dialogInterface) { //método llamado cuando el usuario toca fuera del dialog
                        finish(); //finalizamos la actividad principal, previniendo que el usuario use la app
                    }
                });
                dialog.show(); //mostramos el dialog creado anteriormente
            } else { //si no puede instalar o actualizar google play services
                Toast.makeText(this, "Lo sentimos este dispositivo no esta soportado", Toast.LENGTH_LONG).show(); /*mensaje que le indica al usuario que no puede
                usar la app en su dispositivo*/
                finish(); //finalizamos la actividad principal
            }
        }
        return false; //retornamos un booleano
    }

    public void cambiarFragmento(int posicion){ //método que maneja el cambio de fragmentos
        titulo = getString(R.string.app_name); //titulo a poner en la barra
        Fragment fragment = null; //instancia del fragment
        switch (posicion){ //switch para controlar los cambios de fragmentos
            case 0: //rutas
                fragment = new RutasFragmento(); //instancia de la clase RutasFragmento
                titulo = getString(R.string.rutas); //titulo a poner en la barra
                semuestraCamiones=true;
                semuestraRuta=false;
                semuestraMapa=false;
                break;
            case 1: //mapa
                fragment = new BuscarFragmento(); //instancia de la clase RutasFragmento
                titulo = getString(R.string.mapa); //titulo a poner en la barra
                semuestraCamiones = false; //cómo no se muestra rutas se asigna false
                semuestraMapa = true; //cómo se muestra mapa se asigna true
                semuestraRuta=false; //cómo no se esta mostrando ruta se asigna false
                searchView.setVisibility(View.GONE);
                break;
            case 2: //acerca
                Intent acerca = new Intent(this, Acerca.class);
                startActivity(acerca);
                if(semuestraCamiones)
                    titulo = getString(R.string.rutas); //titulo a poner en la barra
                else
                    titulo = getString(R.string.mapa); //titulo a poner en la barra
                break;
        }
        if(fragment!=null){ //si fragmento es diferente de nulo
            FragmentManager fragmentManager = getSupportFragmentManager(); //instanciando el manejador de fragmentos
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction(); //instanciando FragmentTransaction para manejar las transiciones
            if(posicion!=0) {
                fragmentTransaction.addToBackStack(null); //agregamos al stack el fragmento
                fragmentTransaction.add(R.id.frame, fragment,"fragment");
            }
            else
                fragmentTransaction.replace(R.id.frame, fragment); //reemplazando el id del layout de la main activity
            if(posicion==1)
                fragmentTransaction.commitAllowingStateLoss();
            else
                fragmentTransaction.commit();
            assert getSupportActionBar() != null; //como la barra no será nula le indicamos al compilador que getSupportActionBar no puede ser nulo
            getSupportActionBar().setTitle(titulo); //obtén la barra y asigna el titulo
        }
    }

    public void coloresNavigation(){ //método que agrega los colores respectivos al texto e iconos del nav drawer
        //lista de colores para los diferentes estados del nav drawer
        iconColor = new ColorStateList( //para los colores de iconos
                new int[][]{ //guarda los estados del nav drawer
                        new int[]{android.R.attr.state_checked},//cuando un elemento del nav esta seleccionado
                        new int[]{} //cuando el elemento no esta seleccionado
                },
                new int[] {//guarda los colores
                        getResources().getColor(R.color.colorPrimary),//obtiene recursos de values/colors
                        getResources().getColor(R.color.colorAccent)//obtiene recursos de values/colors
                }
        );

        //lista de colores para los diferentes estados del nav drawer
        textColor = new ColorStateList( //para los colores de texto
                new int[][]{ //guarda los estados del nav drawer
                        new int[]{android.R.attr.state_checked},//cuando un elemento del nav esta seleccionado
                        new int[]{} //cuando el elemento no esta seleccionado
                },
                new int[] {//guarda los colores
                        getResources().getColor(R.color.negro),//obtiene recursos de values/colors
                        getResources().getColor(R.color.colorAccent)//obtiene recursos de values/colors
                }
        );
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
                        //googleMap.setMyLocationEnabled(true);
                        BuscarFragmento.hayGPS=true;
                        cambiarFragmento(1); //llama al método cambiarFragmento se le pasa 1 ya que es la posición del segundo fragmento (MapaFragmento)
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        BuscarFragmento.hayGPS=false;
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
                        BuscarFragmento.hayGPS=true;
                        cambiarFragmento(1); //llama al método cambiarFragmento se le pasa 1 ya que es la posición del segundo fragmento (MapaFragmento)
                        break;
                    case Activity.RESULT_CANCELED:
                        BuscarFragmento.hayGPS=false;
                        cambiarFragmento(1);
                        break;
                }
                break;
        }
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

    @Override
    public void opciones(int eleccion) {
        BuscarFragmento fragment = (BuscarFragmento) getSupportFragmentManager().findFragmentByTag("fragment");
        fragment.opciones(eleccion);
    }

    @Override
    public void ruta_seleccionada(String nombre) {
        BuscarFragmento fragment = (BuscarFragmento) getSupportFragmentManager().findFragmentByTag("fragment");
        fragment.dibujar_rutas(nombre);
    }
}
