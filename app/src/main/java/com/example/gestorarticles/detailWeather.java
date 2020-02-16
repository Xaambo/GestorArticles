package com.example.gestorarticles;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gestorarticles.Model.Temps;
import com.example.gestorarticles.Interfaces.JSONServiceAPI;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class detailWeather extends AppCompatActivity {

    private String baseURL = "https://api.openweathermap.org/data/2.5/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_weather);

        Bundle extras = getIntent().getExtras();
        String ciutat = extras.getString("ciutat");

        getTemps(ciutat);
    }

    private void getTemps(final String ciutat) {

        setTitle("Temps de " + ciutat.toUpperCase());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        String fullURL = "weather?q=" + ciutat + "&units=metric&APPID=541c1d0e26744897a43a4b5126b7f8c6";

        JSONServiceAPI JSONServiceAPI = retrofit.create(JSONServiceAPI.class);

        Call<Temps> temps = JSONServiceAPI.getTemps(fullURL);

        temps.enqueue(new Callback<Temps>(){

            @Override
            public void onResponse(Call<Temps> call, Response<Temps> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                Temps temps = response.body();

                if (temps != null) {
                    ImageView ivIconWeather = findViewById(R.id.ivIconWeather);
                    TextView tvCiutat = findViewById(R.id.tvCiutat);
                    TextView tvTemperatura = findViewById(R.id.tvTemperatura);
                    TextView tvHumitat = findViewById(R.id.tvHumitat);
                    TextView tvVisibilitat = findViewById(R.id.tvVisibilitat);

                    int temp = (int) Math.floor(temps.getMain().getTemp());

                    String URIcon = "https://openweathermap.org/img/wn/" + temps.getWeather().get(0).getIcon() + "@2x.png";
                    Picasso.get().load(URIcon).into(ivIconWeather);

                    tvCiutat.setText(temps.getName());
                    tvTemperatura.setText(temp + "ºC - " + (int) Math.floor(temps.getMain().getTempMin()) + "/" + (int) Math.floor(temps.getMain().getTempMax()) + "ºC");
                    tvHumitat.setText("Humitat: " + temps.getMain().getHumidity() + "%");
                    tvVisibilitat.setText("Pressió: " + temps.getMain().getPressure() + "hPa");
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
