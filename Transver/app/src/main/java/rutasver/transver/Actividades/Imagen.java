package rutasver.transver.Actividades;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import rutasver.transver.Base_de_datos.AdaptadorBd;
import rutasver.transver.R;
import uk.co.senab.photoview.PhotoViewAttacher;


public class Imagen extends AppCompatActivity {

    boolean dim;
    PhotoViewAttacher mAttacher;
    public static String nombre_camion;
    Toolbar barra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagen);
        barra = (Toolbar) findViewById(R.id.barra);// Asignándole el id a la barra
        setSupportActionBar(barra);//Poniendo barra cómo la action bar
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(nombre_camion);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if(savedInstanceState!=null){
            dim =savedInstanceState.getBoolean("dim"); //asignamos a la variable correspondiente el valor guardado en savedInstanceState
            if(dim){
                getSupportActionBar().hide();
                View decorView = getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_LOW_PROFILE;
                decorView.setSystemUiVisibility(uiOptions);
                dim=true;
            }else{
                getSupportActionBar().show();
                View decorView = getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
                decorView.setSystemUiVisibility(uiOptions);
                dim=false;
            }
        }

        final ImageView img = (ImageView) findViewById(R.id.imagen);
        AdaptadorBd bd = new AdaptadorBd(this);
        String imagen_big=bd.obtener_imagen(nombre_camion);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.marker_progress);
        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

        progressBar.setVisibility(View.VISIBLE);
        Glide.with(this)
                .load(imagen_big)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(img)
        ;

        mAttacher = new PhotoViewAttacher(img);

        mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {

                if(!dim){
                    getSupportActionBar().hide();
                    View decorView = getWindow().getDecorView();
                    int uiOptions = View.SYSTEM_UI_FLAG_LOW_PROFILE;
                    decorView.setSystemUiVisibility(uiOptions);
                    dim=true;
                }else{
                    getSupportActionBar().show();
                    View decorView = getWindow().getDecorView();
                    int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
                    decorView.setSystemUiVisibility(uiOptions);
                    dim=false;
                }

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAttacher.cleanup();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override //sobrescribe método del padre
    public void onSaveInstanceState(Bundle outState) { //método que es llamado antes de que la app sea destruida por ejemplo cuando se rota el dispositivo
            super.onSaveInstanceState(outState); //se le pasa al padre el bundle outState
            outState.putBoolean("dim", dim); //guardamos en el bundle una variable
    }
}
