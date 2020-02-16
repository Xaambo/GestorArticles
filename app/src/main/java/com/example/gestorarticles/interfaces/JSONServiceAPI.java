package com.example.gestorarticles.interfaces;

import com.example.gestorarticles.Model.Temps;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JSONServiceAPI {

    @GET("weather?q=Madrid&APPID=541c1d0e26744897a43a4b5126b7f8c6")
    Call<Temps> getTemps();
}
