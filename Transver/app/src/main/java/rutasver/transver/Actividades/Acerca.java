package rutasver.transver.Actividades;

import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import rutasver.transver.R;

public class Acerca extends AppCompatActivity { //Actividad que

    public Toolbar barra;//barra de herramientas

    @Override//sobrescribe del padre
    protected void onCreate(Bundle savedInstanceState) { //cuando se crea la app recibe bundle para pasar estados
        super.onCreate(savedInstanceState); /*esto le manda al padre el estado en el bundle
        , permitiendo que se corra nuestro código y el del padre*/
        setContentView(R.layout.activity_acerca);//asigna la vista al layout correspondiente
        barra = (Toolbar) findViewById(R.id.barra);// Asignándole el id a la barra
        setSupportActionBar(barra);//Poniendo barra cómo la action bar
        getSupportActionBar().setTitle(R.string.acerca); //asignamos el titulo correspondiente
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //muestra la flecha y permite regreso a main
        ImageView imageView;
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setColorFilter(getResources().getColor(R.color.colorPrimary));
        imageView = (ImageView) findViewById(R.id.imageView2);
        imageView.setColorFilter(getResources().getColor(R.color.colorPrimary));
    }
}
