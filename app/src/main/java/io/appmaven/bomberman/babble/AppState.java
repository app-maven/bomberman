package io.appmaven.bomberman.babble;

import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import io.mosaicnetworks.babble.node.BabbleState;

import io.appmaven.bomberman.models.Player;
import io.appmaven.bomberman.babble.transactions.Transaction;
import io.appmaven.bomberman.babble.transactions.ApplyDamageTx;
import io.appmaven.bomberman.babble.transactions.MovePlayerTx;
import io.appmaven.bomberman.babble.transactions.NewPlayerTx;

public class AppState implements BabbleState {
    private Map<String, Player> players = new HashMap<>();

    @Override
    public byte[] applyTransactions(byte[][] transactions) {

        for(byte[] transaction : transactions) {
            String rawTx = new String(transaction, StandardCharsets.UTF_8);

            Transaction tx = Transaction.fromJson(rawTx);;

            switch(tx.type) {
                case APPLY_DAMAGE:
                    ApplyDamageTx dmgTx = ApplyDamageTx.fromJson(rawTx);
                    Player p = this.players.get(dmgTx.data.name);

                    Log.i("ApplyDmg Received", String.format("%s@(%d)", dmgTx.data.name, dmgTx.data.hit));

                    if (p == null) {
                        Log.e("PlayerNotFound", "Cannot find player");
                        break;
                    }

                    p.takeHit(dmgTx.data.hit);

                    break;

                case MOVE_PLAYER:
                    MovePlayerTx moveTx = MovePlayerTx.fromJson(rawTx);
                    Player p2 = this.players.get(moveTx.data.name);

                    Log.i("MovePlayerTx Received", String.format("%s@(%d, %d)", moveTx.data.name, moveTx.data.x, moveTx.data.y));

                    if (p2 == null) {
                        Log.e("PlayerNotFound", "Cannot find player");
                        break;
                    }

                    p2.setNewPosition(moveTx.data.x, moveTx.data.y);

                    break;

                case NEW_PLAYER:
                    NewPlayerTx newPlayerTx = NewPlayerTx.fromJson(rawTx);

                    Log.i("NewPlayerTx Received", String.format("%s@(%d, %d)", newPlayerTx.data.getName(), newPlayerTx.data.x, newPlayerTx.data.y));

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

    public Map<String, Player> getPlayers() {
        return this.players;
    }

    private void addPlayer(Player p) {
        this.players.put(p.getName(), p);
    }

    private void removePlayer(Player p) {
        this.players.remove(p.getName());
    }

    // TEMP till localStorage
    private String moniker = "Player 1";

    public void setMoniker(String name) {
        this.moniker = name;
    }
    public String getMoniker() {
        return this.moniker;
    }
}
