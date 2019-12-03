package io.appmaven.bomberman.transactions;

import com.google.gson.Gson;

import io.mosaicnetworks.babble.node.BabbleTx;

public class MovePlayerTx extends GameTx<MovePlayer> implements BabbleTx {

    private final static Gson gson = new Gson();

    public MovePlayerTx(String name, int x, int y) {
        // set type of transaction
        this.type = Type.MOVE_PLAYER;

        // set payload data
        this.data = new MovePlayer(name, x, y);
    }

    public static MovePlayerTx fromJson(String txJson) {
        return gson.fromJson(txJson, MovePlayerTx.class);
    }

    @Override
    public byte[] toBytes() {
        return gson.toJson(this).getBytes();
    }

}

