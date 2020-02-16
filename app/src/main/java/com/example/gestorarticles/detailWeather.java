package com.example.gestorarticles;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gestorarticles.Model.Temps;
import com.example.gestorarticles.interfaces.JSONServiceAPI;

public class detailWeather extends AppCompatActivity {

    private String baseURL = "https://api.openweathermap.org/data/2.5/";
    private Temps temps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_weather);

        Bundle extras = getIntent().getExtras();
        String ciutat = "weather?q=" + extras.getString("ciutat");

        getTemps(ciutat);
    }

    private void getTemps(final String ciutat) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JSONServiceAPI JSONServiceAPI = retrofit.create(JSONServiceAPI.class);

        Call<Temps> temps = JSONServiceAPI.getTemps();

        temps.enqueue(new Callback<Temps>(){

            @Override
            public void onResponse(Call<Temps> call, Response<Temps> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                Temps temps = response.body();

                if (temps != null) {
                    TextView tvCiutat = findViewById(R.id.tvPais);
                    tvCiutat.setText(response.body().toString());
                }
             }

            @Override
            public void onFailure(Call<Temps> call, Throwable t) {
                String str = t.getMessage();
                String valor = "No se ha podido recuperar los datos desde el servidor. " + str;

                Dialogs.showToast(getApplicationContext(),valor);
            }
        });
    }
}
