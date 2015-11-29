package com.example.yadirafleitas.proyectomoviles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by Yadira Fleitas on 28/10/2015.
 */
public class resultado_test extends AppCompatActivity{
    TextView editnivel,editdias;
    Bundle bundle;
    Button boton;
    String nivel;
    int dias;
    int a,b,c;

    Toolbar toolbar;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resultado_test);
        bundle=this.getIntent().getExtras();
        a=bundle.getInt("a");
        b=bundle.getInt("b");
        c=bundle.getInt("c");
        Toast.makeText(getApplicationContext(), "" + bundle.getInt("a") + "," + bundle.getInt("b") + "," + bundle.getInt("c"), Toast.LENGTH_SHORT).show();
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        boton=(Button)findViewById(R.id.programardias);

        editnivel=(TextView)findViewById(R.id.nivelejercicio);
        editdias=(TextView)findViewById(R.id.diasejercicios);

        if(a>b&&a>c){
            editnivel.setText("BÁSICO");
            editdias.setText("3");
        }else if(b>a&&b>c){
            editnivel.setText("INTERMEDIO");
            editdias.setText("4");
        }else if(c>b&&c>a){
            editnivel.setText("AVANZADO");
            editdias.setText("5");
        }


        boton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(resultado_test.this, ActivityVideo.class);
                Bundle bundle=new Bundle();
                bundle.putString("nivel",editnivel.getText().toString());
                bundle.putInt("dias", Integer.parseInt(editdias.getText().toString()));


                i.putExtras(bundle);

                startActivity(i);

            }

        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
        // bloqueo el boton de atras.............
    }
}