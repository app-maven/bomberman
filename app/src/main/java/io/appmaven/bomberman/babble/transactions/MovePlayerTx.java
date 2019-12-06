package io.appmaven.bomberman.babble.transactions;

import com.google.gson.Gson;

import io.mosaicnetworks.babble.node.BabbleTx;

import io.appmaven.bomberman.models.MovePlayer;

public class MovePlayerTx extends Transaction<MovePlayer> implements BabbleTx {

    private final static Gson gson = new Gson();

    public MovePlayerTx(String name, int x, int y) {
        // set type of transaction
        this.type = Transaction.Type.MOVE_PLAYER;

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
