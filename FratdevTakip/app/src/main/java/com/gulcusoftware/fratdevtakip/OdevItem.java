package com.gulcusoftware.fratdevtakip;

public class OdevItem {
    private String title;
    private String href;

    public OdevItem(String title, String href) {
        this.title = title;
        this.href = href;
    }
    @Override
    public String toString() {
        return title; // ListView'de görüntülenecek metni döndürün
    }
    public String getTitle() {
        return title;
    }

    public String getHref() {
        return href;
    }
}
