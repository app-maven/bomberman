package io.appmaven.bomberman.models;

import android.graphics.Bitmap;

public class PlayerState {
    private String name;
    private Bitmap avatar;
    private int x;
    private int y;
    private int hp;
    private int max;

    public PlayerState(String name, int x, int y, int hp) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.hp = hp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        if(hp < 0) {
            this.hp = 0;
        } else {
            this.hp = hp;
        }
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
}
