package io.appmaven.bomberman.models;

import android.util.Log;

import com.google.gson.JsonSyntaxException;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.appmaven.bomberman.transactions.ApplyDamageTx;
import io.appmaven.bomberman.transactions.MovePlayerTx;
import io.appmaven.bomberman.transactions.GameTx;
import io.appmaven.bomberman.transactions.NewPlayerTx;
import io.mosaicnetworks.babble.node.BabbleState;

public class AppState implements BabbleState {
    private Map<String, PlayerState> globalPlayers = new HashMap<>();
    private Map<String, Player> localPlayers = new HashMap<>();
    private String moniker;
    private ArrayList<String> notifications = new ArrayList<>();

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
                        PlayerState ps1 = this.globalPlayers.get(tx1.data.name);
                        if(ps1.getHp() > tx1.data.hit) {
                            ps1.setHp(ps1.getHp()-tx1.data.hit);
                        } else {
                            ps1.setHp(0);
                            // this.removeGlobalPlayer(ps1);
                        }
                        break;

                    case MOVE_PLAYER:
                        MovePlayerTx tx2 = MovePlayerTx.fromJson(rawTx);
                        PlayerState ps2 = this.globalPlayers.get(tx2.data.name);
                        ps2.setX(tx2.data.x);
                        ps2.setY(tx2.data.y);
                        break;

                    case NEW_PLAYER:
                        NewPlayerTx tx3 = NewPlayerTx.fromJson(rawTx);
                        this.addGlobalPlayer(tx3.data);
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

    public Map<String, PlayerState> getGlobalPlayers() {
        return this.globalPlayers;
    }
    public Map<String, Player> getLocalPlayers() {
        return this.localPlayers;
    }

    public void addGlobalPlayer(PlayerState state) {
        this.globalPlayers.put(state.getName(), state);
    }

    public void removeGlobalPlayer(PlayerState state) {
        this.globalPlayers.remove(state.getName());
    }

    public void addLocalPlayer(Player p) {
        this.localPlayers.put(p.getName(), p);
    }

    public void removeLocalPlayer(Player p) {
        this.localPlayers.remove(p.getName());
    }

    public ArrayList<String> getNotifications() {
        return this.notifications;
    }
    public void addNotification(String message) {
        this.notifications.add(message);
    }

    public void removeNotification(String message) {
        this.notifications.remove(message);
    }

}
