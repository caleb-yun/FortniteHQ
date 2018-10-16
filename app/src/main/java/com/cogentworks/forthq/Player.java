package com.cogentworks.forthq;

public class Player {

    String kills;
    String placeTop1;
    String placeTop2;
    String placeTop3;
    String matchesPlayed;
    String kd;
    String winRate;
    String score;
    String minutesPlayed;

    // Solo, Duo, Squad
    public Player(String kills, String placeTop1, String placeTop2, String placeTop3, String matchesPlayed, String kd, String winRate, String score, String minutesPlayed) {
        this.kills = kills;
        this.placeTop1 = placeTop1;
        this.placeTop2 = placeTop2;
        this.placeTop3 = placeTop3;
        this.matchesPlayed = matchesPlayed;
        this.kd = kd;
        this.winRate = winRate;
        this.score = score;
        this.minutesPlayed = minutesPlayed;
    }

    // Totals
    public Player(String kills, String wins, String matchesPlayed, String minutesPlayed, String score, String winRate, String kd) {
        this.kills = kills;
        this.placeTop1 = wins;
        this.matchesPlayed = matchesPlayed;
        this.minutesPlayed = minutesPlayed;
        this.score = score;
        this.winRate = winRate;
        this.kd = kd;
    }

}
