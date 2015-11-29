package com.example.yadirafleitas.proyectomoviles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by Yadira Fleitas on 28/10/2015.
 */
public class activity_test2 extends AppCompatActivity{
    Button boton;
    RadioButton a1,a2,b1,b2,c1,c2;
    Toolbar toolbar;
    String opcion2 = "";
    String opcion1 = "";
    int a,b,c;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        Bundle bundle=this.getIntent().getExtras();
        a=bundle.getInt("a");b=bundle.getInt("b");c=bundle.getInt("c");
        Toast.makeText(getApplicationContext(),""+bundle.getInt("a")+","+bundle.getInt("b")+","+bundle.getInt("c"),Toast.LENGTH_SHORT).show();
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        boton = (Button)findViewById(R.id.boton1Test2);

        a1=(RadioButton)findViewById(R.id.a1);
        b1=(RadioButton)findViewById(R.id.b1);
        c1=(RadioButton)findViewById(R.id.c1);

        a2=(RadioButton)findViewById(R.id.a2);
        b2=(RadioButton)findViewById(R.id.b2);
        c2=(RadioButton)findViewById(R.id.c2);

        //Para primera pregunta
        View.OnClickListener list1 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch(view.getId()) {
                    case R.id.a1:
                        opcion1 = "a1";
                        break;
                    case R.id.b1:
                        opcion1 = "b1";
                        break;
                    case R.id.c1:
                        opcion1="c1";
                        break;

                }
                Toast.makeText(getApplicationContext(),"ID opción seleccionada: " + opcion1, Toast.LENGTH_SHORT).show();
                //Lenght_short periodo corto---- lenght_long periodo largo
            }
        };
        a1.setOnClickListener(list1);
        b1.setOnClickListener(list1);
        c1.setOnClickListener(list1);

//Para segunda pregunta
        View.OnClickListener list2 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch(view.getId()) {
                    case R.id.a2:
                        opcion2 = "a2";
                        break;
                    case R.id.b2:
                        opcion2 = "b2";
                        break;
                    case R.id.c2:
                        opcion2="c2";
                        break;

                }
                Toast.makeText(getApplicationContext(),"ID opción seleccionada: " + opcion2, Toast.LENGTH_SHORT).show();
                //Lenght_short periodo corto---- lenght_long periodo largo
            }
        };
        a2.setOnClickListener(list2);
        b2.setOnClickListener(list2);
        c2.setOnClickListener(list2);

        boton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity_test2.this, activity_test3.class);
                Bundle bundle=new Bundle();

                switch (opcion1){
                    case "a1":
                        a+=1;
                        break;
                    case "b1":
                        b+=1;
                        break;
                    case "c1":
                        c+=1;
                        break;

                }
                switch (opcion2){
                    case "a2":
                        a+=1;
                        break;
                    case "b2":
                        b+=1;
                        break;
                    case "c2":
                        c+=1;
                        break;
                }

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
