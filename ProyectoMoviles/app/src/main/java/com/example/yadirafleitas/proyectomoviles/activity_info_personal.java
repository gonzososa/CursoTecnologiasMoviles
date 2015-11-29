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
import android.widget.Toast;


/**
 * Created by Yadira Fleitas on 28/10/2015.
 */
public class activity_info_personal extends AppCompatActivity{
    EditText editNombre,editEdad,editAltura,editPeso;
    Bundle bundle;
    Button boton;
    int a,b,c;

    Toolbar toolbar;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_informacion_personal);
        bundle=this.getIntent().getExtras();
        a=bundle.getInt("a");b=bundle.getInt("b");c=bundle.getInt("c");

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       boton=(Button)findViewById(R.id.botonIP);
        editNombre=(EditText)findViewById(R.id.IP1);
        editEdad=(EditText)findViewById(R.id.IP2);
        editAltura=(EditText)findViewById(R.id.IP3);
        editPeso=(EditText)findViewById(R.id.IP4);


        boton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity_info_personal.this, resultado_test.class);
                Bundle bundle=new Bundle();
                bundle.putString("nombre",editNombre.getText().toString());
               bundle.putInt("edad", Integer.parseInt(editEdad.getText().toString()));
                bundle.putDouble("peso", Double.parseDouble(editPeso.getText().toString()));
                bundle.putDouble("altura", Double.parseDouble(editAltura.getText().toString()));
                bundle.putInt("a",a);
                bundle.putInt("b",b);
                bundle.putInt("c",c);

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
