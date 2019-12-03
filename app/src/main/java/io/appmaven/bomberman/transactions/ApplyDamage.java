package io.appmaven.bomberman.transactions;

import com.google.gson.annotations.SerializedName;

public class ApplyDamage {
    @SerializedName("name")
    public String name;
    @SerializedName("hit")
    public int hit;

    public ApplyDamage(String name, int hit) {
        this.name = name;
        this.hit = hit;
    }
}