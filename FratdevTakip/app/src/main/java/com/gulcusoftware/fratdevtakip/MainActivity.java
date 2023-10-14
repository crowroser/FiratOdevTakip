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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button button1;
    Button button3;
    ListView odevliste;
    ArrayAdapter<OdevItem> adapter;
    List<OdevItem> odevler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = findViewById(R.id.button1);
        button3 = findViewById(R.id.button3);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // MainActivity'yi başlatan bir Intent oluşturun
                Intent intent = new Intent(v.getContext(), YemekTakip.class);
                startActivity(intent);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // MainActivity'yi başlatan bir Intent oluşturun
                Intent intent = new Intent(v.getContext(), DuyuruMainActivity.class);
                startActivity(intent);
            }
        });
        odevliste = findViewById(R.id.odevliste);

        odevler = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, odevler);
        odevliste.setAdapter(adapter);
        odevliste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OdevItem selectedOdev = getSelectedOdev(position);
                if (selectedOdev != null) {
                    // Başka bir sınıfa Intent ile "title" ve "href" değerlerini aktarın
                    Intent intent = new Intent(MainActivity.this, OdevAciklama.class);
                    intent.putExtra("title", selectedOdev.getTitle());
                    intent.putExtra("href", selectedOdev.getHref());
                    startActivity(intent);
                }
            }
        });
        // Web sayfasından verileri çekmek için AsyncTask'i başlat
        new JSoupTask().execute();
    }

    private class JSoupTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            String url = "https://muhammetbaykara.com/"; // HTML belgesini çekeceğiniz URL'yi buraya ekleyin

            try {
                Document document = Jsoup.connect(url).get(); // URL'den HTML belgesini çekin

                Elements links = document.select("a[href*=odev][class=more-link]"); // "href" içinde "odev" kelimesini içeren bağlantıları seçin
                String text2;
                for (Element link : links) {
                    String href = link.attr("href"); // Bağlantının href özelliğini alın
                    String text = link.text(); // Bağlantının metnini alın
                    text2 = text.replace("Devamını okuyun", "");




                    // Eğer text ve href boş değilse, listeye ekleyin
                    if (!text.isEmpty() && !href.isEmpty()) {
                        // Çekilen verileri liste içine ekleyin
                        odevler.add(new OdevItem(text2, href));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Verileri ListView'e ekleyin
            adapter.notifyDataSetChanged();
        }
    }

    private OdevItem getSelectedOdev(int position) {
        if (position >= 0 && position < odevler.size()) {
            return odevler.get(position);
        }
        return null;
    }
}
