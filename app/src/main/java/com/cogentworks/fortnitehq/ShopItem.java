package com.cogentworks.fortnitehq;

public class ShopItem {

    String name;
    String cost;
    String imageUrl;
    boolean featured;

    public ShopItem(String name, String cost, String imageUrl, int featured) {
        this.name = name;
        this.cost = cost;
        this.imageUrl = imageUrl;
        this.featured = featured != 0;
    }
}
