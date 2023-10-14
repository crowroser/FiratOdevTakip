package com.gulcusoftware.fratdevtakip;

import androidx.appcompat.app.AppCompatActivity;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
        baslik = findViewById(R.id.baslik);
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
            String url = selectedHref;
            Log.d("hrefdegetr", url);
            try {
                Document document = Jsoup.connect(url).get();

                Elements links = document.select("div[class=post-content]");

                for (Element link : links) {
                    String text = link.text();
                    textContent += text + "\n";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return textContent;
        }

        @Override
        protected void onPostExecute(String result) {
            // Veriyi TextView'e ekleyin ve içindeki bağlantıları tıklanabilir hale getirin
            aciklama.setText(result);
            makeLinksClickable(aciklama, result);
        }
    }

    private void makeLinksClickable(TextView textView, String text) {
        SpannableString spannableString = new SpannableString(text);
        String[] words = text.split("\\s+"); // Boşluklara göre metni böler

        for (final String word : words) {
            if (word.startsWith("http://") || word.startsWith("https://")) {
                int start = text.indexOf(word);
                int end = start + word.length();
                spannableString.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        // Bağlantıya tıklandığında tarayıcıyı açın
                        openLink(word);
                    }
                }, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        // TextView içindeki bağlantıları tıklanabilir hale getirin
        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void openLink(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "Tarayıcı bulunamadı.", Toast.LENGTH_SHORT).show();
        }
    }
}
