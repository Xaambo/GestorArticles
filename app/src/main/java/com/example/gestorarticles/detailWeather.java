package com.example.gestorarticles;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gestorarticles.interfaces.JSONServiceAPI;

public class detailWeather extends AppCompatActivity {

    private String baseURL = "api.openweathermap.org/data/2.5/weather?q=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_weather);

        Bundle extras = getIntent().getExtras();
        String ciutat = extras.getString("ciutat");

        getTemps(ciutat);
    }

    private void getTemps(String ciutat) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL + ciutat + "&APPID=541c1d0e26744897a43a4b5126b7f8c6")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JSONServiceAPI JSONServiceAPI = retrofit.create(JSONServiceAPI.class);

        Call<Temps> temps = JSONServiceAPI.getTemps();

        temps.enqueue(new Callback<Temps>(){

            @Override
            public void onResponse(Call<Temps> call, Response<Temps> response) {

            }

            @Override
            public void onFailure(Call<Temps> call, Throwable t) {

            }
        });
    }
}
