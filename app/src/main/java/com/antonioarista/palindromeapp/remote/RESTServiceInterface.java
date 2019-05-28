package com.antonioarista.palindromeapp.remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by MSc. Antonio Arista Jalife on 28/05/2019.
 */
public interface RESTServiceInterface
{
    @GET("/getPalindromeJSON/{topValue}")
    Call<PalindromeData> getAnswers(@Path("topValue") long topValue);
}
