package com.antonioarista.palindromeapp.remote;

/**
 * Created by MSc. Antonio Arista Jalife on 28/05/2019.
 */
public class ApiUtilities
{
    //La URL base de donde nos conectaremos al servicio REST:
    public static final String BASE_URL = "http://antonioarista-gindemo2.us-west-1.elasticbeanstalk.com/";

    /**********************************************************************
     * getRestServiceInterface.
     * genera solamente un valor para conectarnos al cliente de Retrofit.
     * para este ejemplo es una url estática Hard-coded, pero podemos
     * agregar otras funciones para hacer una conexión mas dinámica.
     * @return un valor RESTServiceInterface para conectarnos al servicio REST.
     **********************************************************************/
    public static RESTServiceInterface getRestServiceInterface()
    {
        return RetrofitClient.getClient(BASE_URL).create(RESTServiceInterface.class);
    }
}
