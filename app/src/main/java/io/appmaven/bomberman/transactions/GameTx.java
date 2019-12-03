package io.appmaven.bomberman.transactions;


import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;


public class GameTx<T> {

    private final static Gson gson = new Gson();

    public enum Type {
        APPLY_DAMAGE,
        MOVE_PLAYER,
        NEW_PLAYER,
    }

    @SerializedName("type")
    public Type type = Type.MOVE_PLAYER;

    @SerializedName("data")
    public T data = null;

    public static GameTx fromJson(String txJson) {
        return gson.fromJson(txJson, GameTx.class);
    }

}
