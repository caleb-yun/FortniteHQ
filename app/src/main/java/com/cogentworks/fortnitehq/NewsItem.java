package com.cogentworks.fortnitehq;

public class NewsItem {

    public String title;
    public String body;
    public String image;
    public String url;
    public String date;
    public long time;

    public NewsItem(String title, String body, String image, long time) {
        this.title = title;
        this.body = body;
        this.image = image;
        this.time = time;
    }

    public NewsItem(String title, String body, String image, String date, String url) {
        this.title = title;
        this.body = body;
        this.image = image;
        this.date = date;
        this.url = url;
    }
}
