package com.antonioarista.palindromeapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ContactMeActivity extends AppCompatActivity {

    /**********************************************************************
     * <p>onCreate</p>
     * onCreate contiene todas las órdenes necearias cuando la actividad se
     * crea, en este caso el poder contactarme por varios medios por medio
     * de intents :)
     *
     * @param savedInstanceState instancia actual.
     **********************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //Variables locales:
        Button btnMail, btnPhone, btnOther;         //Boton de mail, teléfono y otros.

        super.onCreate(savedInstanceState);

        //Vinculación Java / XML
        setContentView(R.layout.activity_contact_me);
        btnMail = findViewById(R.id.contactMe_mail);
        btnPhone = findViewById(R.id.contactMe_phonebtn);
        btnOther = findViewById(R.id.contactMe_otherbtn);

        //Creación de acciones en cada botón (con onClickListener).
        btnMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Genera un Intent con la acción "ACTION_SENDTO" para enviar un correo a mi dirección.
                Con ello puedes contactarme :D
                */
                Intent mailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","arista.antonio@gmail.com", null));
                mailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hola! Queremos contactarte!");
                mailIntent.putExtra(Intent.EXTRA_TEXT, "Hola Antonio!");
                startActivity(Intent.createChooser(mailIntent, "Envíale un correo a Antonio con..."));
            }
        });

        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Genera un Intent con la acción "ACTION_DIAL" para llamar a mi celular.
                Con ello puedes contactarme :D
                */
                String phoneToCall = "+52 1 55 3894 4903";
                String uri = "tel:" +phoneToCall ;
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
                phoneIntent.setData(Uri.parse(uri));
                startActivity(phoneIntent);
            }
        });

        btnOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Genera un Intent con la acción "ACTION_SEND" para enviar un texto plano.
                Con ello puedes contactarme via Whatsapp / LinkedIn / SMS / otro...
                */
                Intent otherIntent = new Intent(Intent.ACTION_SEND);
                otherIntent.setType("plain/text");
                otherIntent.putExtra(Intent.EXTRA_TEXT, "Hola Antonio!");
                startActivity(Intent.createChooser(otherIntent, "Contacta a Antonio vía..."));
            }
        });
    }
}
