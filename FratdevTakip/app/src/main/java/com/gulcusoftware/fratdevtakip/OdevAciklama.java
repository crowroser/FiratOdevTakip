package com.gulcusoftware.fratdevtakip;

import androidx.appcompat.app.AppCompatActivity;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;

public class OdevAciklama extends AppCompatActivity {
    TextView aciklama;
    TextView baslik;
    private String selectedHref;
private String baslikmetni;
    String textContent = "";
    String baslikContent = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aciklama_layout);
        baslik=findViewById(R.id.baslik);
        aciklama = findViewById(R.id.aciklama);
        Intent intent = getIntent();

            selectedHref = intent.getStringExtra("href");
            baslikmetni = intent.getStringExtra("title");
baslik.setText(baslikmetni);

        // Web sayfasından verileri çekmek için AsyncTask'i başlat
        new JSoupTask().execute();
    }

    private class JSoupTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String url = selectedHref; // HTML belgesini çekeceğiniz URL'yi buraya ekleyin
            Log.d("hrefdegetr",url);
            try {
                Document document = Jsoup.connect(url).get(); // URL'den HTML belgesini çekin

                Elements links = document.select("div[class=post-content]"); // "href" içinde "odev" kelimesini içeren bağlantıları seçin

                for (Element link : links) {
                    String text = link.text(); // Bağlantının metnini alın
                    textContent += text + "\n";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return textContent;
        }

        @Override
        protected void onPostExecute(String result) {
            // Veriyi TextView'e ekleyin
            aciklama.setText(result);
        }
    }
}
