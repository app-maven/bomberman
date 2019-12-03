package io.appmaven.bomberman.transactions;

import com.google.gson.annotations.SerializedName;

public class MovePlayer {
    @SerializedName("name")
    public String name;
    @SerializedName("x")
    public int x;
    @SerializedName("y")
    public int y;

    public MovePlayer(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }
}