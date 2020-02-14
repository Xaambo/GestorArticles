package com.example.gestorarticles.interfaces;

import com.example.gestorarticles.Temps;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JSONServiceAPI {

    @GET("temps")
    Call<Temps> getTemps();
}
