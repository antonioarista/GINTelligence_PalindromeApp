package com.antonioarista.palindromeapp;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.antonioarista.palindromeapp.remote.ApiUtilities;
import com.antonioarista.palindromeapp.remote.PalindromeData;
import com.antonioarista.palindromeapp.remote.RESTServiceInterface;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PalindromeAPIActivity extends AppCompatActivity {

    //Variables globales internas:
    private RESTServiceInterface restService;       //Sistema de servicio REST (en paquete remote)
    private Context currentContext = this;          //Contexto de la Actividad actual.

    //Variables globales de control de interfaz.
    private ScrollView resultScroll;                //ScrollView que contiene los resultados.
    private EditText editPalindrome;                //EditText para obtener el tope del palíndromo.
    private Button btnLaunch;                       //Un botón de enviar (opcional su uso).
    private TextView txtResult_sum, txtResult_binary, txtResult_decimal; //textos de resultados.
    private GifImageView loadingScreen;             //Imagen GIF para mostrar el status de la App
                                                    //Pude haber usado un SVG, pero es mas divertido
                                                    // Un .GIF (y mas difícil ;) )

    /**********************************************************************
     * <p>onCreate</p>
     * onCreate en este caso servirá para generar la funcionalidad tanto
     * del botón de enviar, como del editText cuando se presiona el botón
     * de "done" o enviar.
     *
     * @param savedInstanceState instancia actual.
     **********************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Operaciones de vinculación Java / XML
        setContentView(R.layout.activity_palindromeapi);
        editPalindrome = findViewById(R.id.mainActivity_EditText);
        btnLaunch = findViewById(R.id.mainActivity_Button);
        txtResult_sum = findViewById(R.id.mainActivity_result_sum);
        txtResult_binary = findViewById(R.id.mainActivity_result_binary);
        txtResult_decimal = findViewById(R.id.mainActivity_result_decimal);
        loadingScreen = findViewById(R.id.mainActivity_loadingScreen);
        resultScroll = findViewById(R.id.mainActivity_ScrollResult);

        //Conexión de la interfaz REST (revisar paquete remote para mas datos).
        restService = ApiUtilities.getRestServiceInterface();

        //Generación de Listeners: para cuando presionas el botón de enviar.
        btnLaunch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //este método corre todas las operaciones necesarias.
                runTheClickButton();
            }
        });
        //Generación de listeners: Para cuando presionas en el teclado el botón de Done.
        editPalindrome.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //este método corre todas las operaciones necesarias.
                    runTheClickButton();
                    return true;
                }
                return false;
            }
        });
    }


    /**********************************************************************
     * <p>runTheClickButton</p>
     * Este método originalmente podría haber estado en uno de los Listener
     * sin embargo como dos listener hacen la misma operación, se separó.
     * Este método hace un rápido "Sanity Check" de los datos, luego minimiza
     * el teclado, extrae el valor del EditText, y lo envía al método que carga
     * el palíndromo de la operación REST.
     **********************************************************************/
    private void runTheClickButton()
    {
        //Revisando que el valor del EditText no esté vacío ("")...
        //El InputType del EditText está colocado para recibir numeros enteros.
        if (editPalindrome.getText().toString().contentEquals("")) {
            editPalindrome.setHintTextColor(Color.RED);
            return;
        }

        //Minimizamos el teclado con un Try/Catch (porque Android nos dice que puede
        //ocurrir un NullPointerException
        try {
            InputMethodManager imm = (InputMethodManager) currentContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editPalindrome.getWindowToken(), 0);
        }
        catch (NullPointerException ex) {
            Log.d("PalindromeAPIActivity", "Got a NPException on the keyboard. No one is going to die for this...");
        }

        //En caso de haber tenido que cambiar el color del hint, lo retornamos a su estado original.
        editPalindrome.setHintTextColor(Color.GRAY);

        //Hacemos las transiciones a Loading...
        transitionToLoading();

        //Obtenemos el valor tope, y lo mandamos a "loadPalindrome".
        long topValueToCheck = Long.parseLong(editPalindrome.getText().toString());
        loadPalindrome(topValueToCheck);
    }


    /**********************************************************************
     * <p>loadPalindrome</p> Este método sirve para cargar el palíndromo
     * Del servicio REST, y maneja los posibles errores que pueden ocurrir.
     *
     * @param topValue el valor tope que vamos a calcular (entero mayor o igual a 0)
     **********************************************************************/
    public void loadPalindrome(long topValue)
    {
        //Solicita la respuesta del servicio REST, de manera asíncrona.
        restService.getAnswers(topValue).enqueue(new Callback<PalindromeData>()
        {
            //Una vez obtenida una respuesta, se hace lo siguiente:
            @Override
            public void onResponse(Call<PalindromeData> call, Response<PalindromeData> response)
            {
                //Si la respuesta fue exitosa...
                if(response.isSuccessful())
                {
                    //Enviamos un Log para que veamos que las cosas van bien...
                    Log.d("Main Activity","Loaded!");

                    //Editamos el texto de la suma.
                    txtResult_sum.setText(Html.fromHtml("<b>Suma: </b>"+response.body().getSumOfValues()));

                    //Extraemos de la respuesta todos los valores decimales y binarios.
                    List<Integer> decimalValues = response.body().getAllPalindromes();
                    List<String> binaryValues = response.body().getAllBinaryValues();

                    //Extraemos los valores decimales y binarios...
                    String resultingText_decimal, resultingText_binary;
                    resultingText_decimal = "<b>Decimal: </b><br>";
                    resultingText_binary = "<b>Binario: </b><br>";

                    //Y los concatenamos uno por uno cíclicamente en su respectivo string.
                    for(int counter = 0; counter < decimalValues.size(); counter++)
                    {
                        resultingText_binary += binaryValues.get(counter)+ "<br>";
                        resultingText_decimal += decimalValues.get(counter) +"<br>";
                    }

                    //Finalmente, tomamos el string y lo usamos para llenar el resultado.
                    txtResult_binary.setText(Html.fromHtml(resultingText_binary));
                    txtResult_decimal.setText(Html.fromHtml(resultingText_decimal));

                    //Hacemos la transición para mostrar el resultado.
                    transitionToResult();
                }
                else
                {
                    //Obtenemos lo que falló.
                    int statusCode = response.code();
                    //Mandamos el código de error al Log para ver que pasó.
                    Log.d("Main Activity", "Whoops.... Status code:"+statusCode);

                    //Hacemos una transición hacia la pantalla de error...
                    transitionToError();

                    //Y mandamos un toast para saber que pasó.
                    Toast.makeText(currentContext,"Whoops, algo salió mal... Error: "+statusCode,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<PalindromeData> call, Throwable t)
            {
                //Mandamos el código de error al Log para ver que pasó.
                Log.d("Main Activity", "Error loading from API :c Reason:"+t.toString());

                //Hacemos una transición hacia la pantalla de error...
                transitionToError();

                //Y mandamos un toast para saber que pasó.
                Toast.makeText(currentContext,"Whoops, algo salió mal... Error: "+t.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }


    /***********************************************************************
     *          Métodos de EyeCandy / Transiciones.
     *
     * Estos métodos sirven para mostrar pantallas de Loading / Error.
     * No son "mision critical" pero sirven para hacer la operación mas fluída.
     **********************************************************************/

    /**********************************************************************
     * transitionToLoading muestra la ventana de Loading y retira la visibilidad
     * del resto de los controles. Explícitamente carga el GIF de "loading".
     **********************************************************************/
    private void transitionToLoading()
    {
        TransitionManager.beginDelayedTransition(resultScroll, new Fade());
        loadingScreen.setVisibility(View.VISIBLE);
        loadingScreen.setImageResource(R.drawable.loadinggif);
        resultScroll.setVisibility(View.GONE);
        editPalindrome.setVisibility(View.GONE);
        btnLaunch.setVisibility(View.GONE);
    }

    /**********************************************************************
     * transitionToResult retira la ventana de Loading y permite ver el resto
     * de los conroles, para que pueda intentar nuevamente el usuario el generar
     * un conjunto de palíndromos.
     **********************************************************************/
    private void transitionToResult()
    {
        TransitionManager.beginDelayedTransition(resultScroll, new Fade());
        loadingScreen.setVisibility(View.GONE);
        resultScroll.setVisibility(View.VISIBLE);
        editPalindrome.setVisibility(View.VISIBLE);
        btnLaunch.setVisibility(View.VISIBLE);
    }

    /**********************************************************************
     * transitionToError muestra al simpático dinosaurio en forma animada.
     * sin embargo regresa los controles para intentarlo de nuevo.
     * Si algo salió mal (Por ejemplo un error 404 o no hay internet) este
     * método se llama.
     *
     * Curiosidad: El GIF del dinosaurio lo edité para tener la paleta de
     * colores del resto de la App con GIMP (GNU image manipulation program)
     **********************************************************************/
    private void transitionToError()
    {
        TransitionManager.beginDelayedTransition(resultScroll, new Fade());
        loadingScreen.setVisibility(View.VISIBLE);
        loadingScreen.setImageResource(R.drawable.dino_cyan_large);
        resultScroll.setVisibility(View.GONE);
        editPalindrome.setVisibility(View.VISIBLE);
        btnLaunch.setVisibility(View.VISIBLE);
    }
}
