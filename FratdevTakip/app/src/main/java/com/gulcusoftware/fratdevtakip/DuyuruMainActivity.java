package com.gulcusoftware.fratdevtakip;

import android.os.Bundle;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DuyuruMainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DuyuruAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.duyuru_layout);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://raw.githubusercontent.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DuyuruService service = retrofit.create(DuyuruService.class);

        Call<List<Duyuru>> call = service.getDuyurular();
        call.enqueue(new Callback<List<Duyuru>>() {
            @Override
            public void onResponse(Call<List<Duyuru>> call, Response<List<Duyuru>> response) {
                if (response.isSuccessful()) {
                    List<Duyuru> duyurular = response.body();
                    adapter = new DuyuruAdapter(duyurular);
                    recyclerView.setAdapter(adapter);
                } else {
                    // Hata durumlarını işleyin
                }
            }

            @Override
            public void onFailure(Call<List<Duyuru>> call, Throwable t) {
                // Bağlantı hatası durumlarını işleyin
            }
        });



    }
}
