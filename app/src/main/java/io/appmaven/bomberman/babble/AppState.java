package io.appmaven.bomberman.babble;

import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import io.appmaven.bomberman.models.PlayerState;
import io.appmaven.bomberman.transactions.ApplyDamageTx;
import io.appmaven.bomberman.transactions.MovePlayerTx;
import io.appmaven.bomberman.transactions.GameTx;
import io.appmaven.bomberman.transactions.NewPlayerTx;
import io.mosaicnetworks.babble.node.BabbleState;

public class State implements BabbleState {
    private Map<String, PlayerState> players = new HashMap<>();

    @Override
    public byte[] applyTransactions(byte[][] transactions) {

        for(byte[] transaction : transactions) {
            String rawTx = new String(transaction, StandardCharsets.UTF_8);
            GameTx gameTx;

            // .data attribute is null
            gameTx = GameTx.fromJson(rawTx);

            switch(gameTx.type) {
                case APPLY_DAMAGE:
                    ApplyDamageTx dmgTx = ApplyDamageTx.fromJson(rawTx);
                    PlayerState p = this.players.get(dmgTx.data.name);

                    if (p == null) {
                        Log.e("PlayerNotFound", "Cannot find player");
                        break;
                    }

                    if(p.getHp() > dmgTx.data.hit) {
                        p.setHp(p.getHp() - dmgTx.data.hit);
                    } else {
                        p.setHp(0);
                    }

                    break;

                case MOVE_PLAYER:
                    MovePlayerTx moveTx = MovePlayerTx.fromJson(rawTx);
                    PlayerState p2 = this.players.get(moveTx.data.name);

                    if (p2 == null) {
                        Log.e("PlayerNotFound", "Cannot find player");
                        break;
                    }

                    p2.setX(moveTx.data.x);
                    p2.setY(moveTx.data.y);

                    break;

                case NEW_PLAYER:
                    NewPlayerTx newPlayerTx = NewPlayerTx.fromJson(rawTx);

                    this.addPlayer(newPlayerTx.data);

                    break;
                default:
                    break;
            }
        }

        return new byte[0];
    }

    @Override
    public void reset() {
        // Do reset of state here
    }

    public Map<String, PlayerState> getPlayers() {
        return this.players;
    }

    private void addPlayer(PlayerState p) {
        this.players.put(p.getName(), p);
    }

    private void removePlayer(PlayerState p) {
        this.players.remove(p.getName());
    }
}
