package com.cogentworks.forthq;

public class ChallengeItem {

    String name;
    int total;
    int stars;
    String difficulty;

    boolean isSeparator = false;

    public ChallengeItem(String name, int total, int stars, String difficulty) {
        this.name = name;
        this.total = total;
        this.stars = stars;
        this.difficulty = difficulty;
    }

    public ChallengeItem(String week) {
        isSeparator = true;
        name = week;
    }
}
