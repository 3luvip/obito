package com.girlkun.models.player;

public class Location {

    public int x;
    public int y;
public Location() {
        this.x = 0;
        this.y = 0;
    }
public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public long lastTimeplayerMove;
}
