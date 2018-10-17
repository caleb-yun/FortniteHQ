package com.cogentworks.forthq;

public class ShopItem {

    String name;
    String cost;
    String imageUrl;
    String featuredImg;
    boolean featured;
    boolean isTitle;

    public ShopItem(String name, String cost, String imageUrl, String featuredImg, int featured) {
        this.name = name;
        this.cost = cost;
        this.imageUrl = imageUrl;
        this.featuredImg = featuredImg;
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
