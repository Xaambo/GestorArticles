package com.example.gestorarticles.Interfaces;

import com.example.gestorarticles.Model.Temps;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface JSONServiceAPI {

    @GET
    Call<Temps> getTemps(@Url String url);
}
