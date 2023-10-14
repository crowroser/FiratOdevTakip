package com.gulcusoftware.fratdevtakip;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class YemekTakip extends AppCompatActivity {
    ListView yemekListe;
    ArrayAdapter<YemekItem> adapter2;
    List<YemekItem> yemekler;


    private static final String CHANNEL_ID = "yeni_yemek_kanal_id";
    private int notificationId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yemek_liste);

        yemekListe = findViewById(R.id.yemekListe);

        yemekler = new ArrayList<>();
        adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, yemekler);
        yemekListe.setAdapter(adapter2);

        // Bildirim kanalını oluşturun (API Seviyesi 26+)
        createNotificationChannel();

        // Web sayfasından verileri çekmek için AsyncTask'i başlat
        new JSoupTask().execute();

        startPeriodicCheck();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Yeni Yemek Bildirimleri";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private class JSoupTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            String url = "https://unievi.firat.edu.tr/"; // HTML belgesini çekeceğiniz URL'yi buraya ekleyin

            try {
                Document document = Jsoup.connect(url).get(); // URL'den HTML belgesini çekin

                Elements links = document.select("div[class=box__content]"); // "href" içinde "odev" kelimesini içeren bağlantıları seçin
                String text2;
                for (Element link : links) {
                    String href = link.attr("href"); // Bağlantının href özelliğini alın
                    String text = link.text(); // Bağlantının metnini alın
                    Log.d("yemekler", text);

                    // Eğer text ve href boş değilse, listeye ekleyin
                    if (!text.isEmpty() && !href.isEmpty()) {
                        // Çekilen verileri liste içine ekleyin
                        yemekler.add(new YemekItem(text, href));
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
            adapter2.notifyDataSetChanged();
        }
    }

    private YemekItem getSelectedOdev(int position) {
        if (position >= 0 && position < yemekler.size()) {
            return yemekler.get(position);
        }
        return null;
    }

    private void startPeriodicCheck() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // Web sitesinden verileri çekme işlemi burada yapılır
                handler.postDelayed(this, 360000); //Check Süresi  (ayarlayabilirsiniz)
            List<YemekItem> yeniYemekler = new ArrayList<>();

                // Web sitesinden yeni ödevleri çekme işlemi burada yapılır


                for (YemekItem yeniYemek : yeniYemekler) {
                    if (!yemekler.contains(yeniYemek)) {
                        // Bildirim gönderme işlemi burada gerçekleştirilir
                        sendNotification(yeniYemek);
                    }
                }


                yemekler.addAll(yeniYemekler);
                adapter2.notifyDataSetChanged();
            }
        };
        // İlk sorgulamayı başlatın
        handler.post(runnable);
    }

    @SuppressLint("MissingPermission")
    private void sendNotification(YemekItem yeniYemek) {
        String title = yeniYemek.getTitle();
        String href = yeniYemek.getHref();

        // Bildirim oluşturma işlemi
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Yeni Ödev")
                .setContentText(title)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent intent = new Intent(this, OdevAciklama.class);
        intent.putExtra("title", title);
        intent.putExtra("href", href);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        builder.setContentIntent(pendingIntent);

        // Bildirimi gönderme işlemi
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationId, builder.build());
        notificationId++;
    }
}
