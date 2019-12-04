package io.appmaven.bomberman.transactions;

import com.google.gson.Gson;

import io.appmaven.bomberman.models.PlayerState;
import io.mosaicnetworks.babble.node.BabbleTx;


public class NewPlayerTx extends GameTx<PlayerState> implements BabbleTx {
    private final static Gson gson = new Gson();

    public NewPlayerTx(PlayerState player) {
        // set type of transaction
        this.type = Type.NEW_PLAYER;

        // set payload data
        this.data = player;
    }

    public static NewPlayerTx fromJson(String txJson) {
        return gson.fromJson(txJson, NewPlayerTx.class);
    }

    @Override
    public byte[] toBytes() {
        return gson.toJson(this).getBytes();
    }

}

