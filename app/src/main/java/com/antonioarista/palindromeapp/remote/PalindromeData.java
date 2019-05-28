package com.antonioarista.palindromeapp.remote;

/**
 * Created by MSc. Antonio Arista Jalife on 28/05/2019.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PalindromeData
{
    //Atributos globales con las etiquetas SerializedName(para el JSON) y Expose.
    @SerializedName("All")
    @Expose
    private List<Integer> allPalindromes = null;

    @SerializedName("Bin")
    @Expose
    private List<String> allBinaryValues = null;

    @SerializedName("Sum")
    @Expose
    private Integer sumOfValues;

    /******************************************************************
     * Getters & Setters
    ******************************************************************/
    public List<Integer> getAllPalindromes() {
        return allPalindromes;
    }

    public void setAllPalindromes(List<Integer> allPalindromes) { this.allPalindromes = allPalindromes; }

    public List<String> getAllBinaryValues() {
        return allBinaryValues;
    }

    public void setAllBinaryValues(List<String> allBinaryValues) { this.allBinaryValues = allBinaryValues; }

    public Integer getSumOfValues(){
        return sumOfValues;
    }

    public void setSumOfValues(Integer sumOfValues) {
        this.sumOfValues = sumOfValues;
    }
}
