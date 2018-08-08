package com.cogentworks.fortnitehq;

public class NewsItem {

    public String title;
    public String body;
    public String image;
    public long time;

    public NewsItem(String title, String body, String image, long time) {
        this.title = title;
        this.body = body;
        this.image = image;
        this.time = time;
    }
}
