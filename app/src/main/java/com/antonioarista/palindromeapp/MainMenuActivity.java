package com.antonioarista.palindromeapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity {

    //Atributos globales.
    private Context currentContext = this;      //Contexto actual de la actividad.

    /**********************************************************************
     * <p>onCreate</p>
     * onCreate contiene todas las órdenes necearias cuando la actividad se
     * crea, en este caso la vinculación al menú principal y las funciones de
     * los dos botones: Prueba principal y acerca de.
     *
     * @param savedInstanceState instancia actual.
     **********************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //Variables locales:
        Button btnMaintest, btnContactInfo;         //Botones de la actividad.

        //onCreate de Android...
        super.onCreate(savedInstanceState);

        //Operaciones de vinculación XML / Java
        setContentView(R.layout.activity_main_menu);
        btnMaintest = findViewById(R.id.mainMenu_maintestbtn);
        btnContactInfo = findViewById(R.id.mainMenu_aboutbtn);

        //Creación de acciones en cada botón (con onClickListener).
        btnMaintest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Genera un Intent (salto entre actividades) de esta actividad a la Actividad
                PalindromeApiActivity
                */
                Intent jumpIntent = new Intent(currentContext, PalindromeAPIActivity.class);
                startActivity(jumpIntent);
            }
        });

        btnContactInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Genera un Intent (salto entre actividades) de esta actividad a la Actividad
                ContactMeActivity
                */
                Intent jumpIntent = new Intent(currentContext, ContactMeActivity.class);
                startActivity(jumpIntent);
            }
        });
    }
}
