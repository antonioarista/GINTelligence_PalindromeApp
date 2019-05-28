package com.antonioarista.palindromeapp.remote;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by MSc. Antonio Arista Jalife on 28/05/2019.
 */
public class RetrofitClient {

    //Atributos globales:
    private static Retrofit retroFitConnection = null; //Aqui utilizaremos una conexión con Retrofit

    public static Retrofit getClient(String baseUrl)
    {
        if(retroFitConnection == null)
        {
            //Aqui hemos modificado la conexión HTTP para no caducar hasta los 60 segundos.
            final OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build();

            //Construimos la conexión con Retrofit aquí si no ha sido creada.
            retroFitConnection = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retroFitConnection;
    }
}
