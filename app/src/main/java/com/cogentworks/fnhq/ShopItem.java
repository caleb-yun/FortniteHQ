package com.cogentworks.fnhq;

public class ShopItem {

    String name;
    String cost;
    String imageUrl;
    boolean featured;
    boolean isTitle;

    public ShopItem(String name, String cost, String imageUrl, int featured) {
        this.name = name;
        this.cost = cost;
        this.imageUrl = imageUrl;
        this.featured = featured != 0;
        isTitle = false;
    }

    // Divider Title
    public ShopItem(String title) {
        name = title;
        featured = false;
        isTitle = true;
    }
}
