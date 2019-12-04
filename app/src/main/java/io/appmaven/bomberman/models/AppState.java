package io.appmaven.bomberman.models;

import android.util.Log;

import com.google.gson.JsonSyntaxException;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import io.appmaven.bomberman.transactions.ApplyDamageTx;
import io.appmaven.bomberman.transactions.MovePlayerTx;
import io.appmaven.bomberman.transactions.GameTx;
import io.appmaven.bomberman.transactions.NewPlayerTx;
import io.mosaicnetworks.babble.node.BabbleState;

public class AppState implements BabbleState {

    private byte[] mStateHash = new byte[0];
    private final Map<Integer, GameTx> mState = new HashMap<>();
    private Integer mNextIndex = 0;

    private Map<String, Player> players = new HashMap<>();
    private String moniker;

    @Override
    public byte[] applyTransactions(byte[][] transactions) {

        for(byte[] transaction : transactions) {
            String rawTx = new String(transaction, StandardCharsets.UTF_8);

            GameTx gameTx;

            try {
                gameTx = GameTx.fromJson(rawTx);

                switch(gameTx.type) {
                    case APPLY_DAMAGE:
                        ApplyDamageTx tx1 = ApplyDamageTx.fromJson(rawTx);
                        Player p1 = this.players.get(tx1.data.name);
                        p1.takeHit(tx1.data.hit);
                        break;

                    case MOVE_PLAYER:
                        MovePlayerTx tx2 = MovePlayerTx.fromJson(rawTx);
                        Player p2 = this.players.get(tx2.data.name);
                        p2.moveTo(tx2.data.x, tx2.data.y);
                        break;

                    case NEW_PLAYER:
                        NewPlayerTx tx3 = NewPlayerTx.fromJson(rawTx);
                        this.players.put(tx3.data.getName(), tx3.data);
                        break;
                    default:
                        break;
                }

            } catch (JsonSyntaxException e) {
                //skip any malformed transactions
            }

        }

        return new byte[0];
    }

    @Override
    public void reset() {

    }

    public void setMoniker(String name) {
        this.moniker = name;
    }

    public String getMoniker() {
        return this.moniker;
    }

    public Map<String, Player> getPlayers() {
        return this.players;
    }
}
