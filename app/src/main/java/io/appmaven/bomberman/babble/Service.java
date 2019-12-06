package io.appmaven.bomberman.babble;

import android.annotation.SuppressLint;
import android.util.Log;

import io.mosaicnetworks.babble.node.BabbleConfig;
import io.mosaicnetworks.babble.node.BabbleService;

import io.appmaven.bomberman.models.Player;
import io.appmaven.bomberman.babble.transactions.NewPlayerTx;
import io.appmaven.bomberman.babble.transactions.MovePlayerTx;
import io.appmaven.bomberman.babble.transactions.ApplyDamageTx;

public class Service extends BabbleService<AppState> {
    private static Service INSTANCE;

    public static Service getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Service();
        }

        return INSTANCE;
    }

    private Service() {
        super(new AppState(), new BabbleConfig.Builder().heartbeat(1).logLevel(BabbleConfig.LogLevel.DEBUG).build());
    }

    public static Player getMyPlayer() {
        Player p = Service.getInstance().state.getPlayers().get( Service.getInstance().state.getMoniker());

        if (p == null) {
            throw new Error("Cannot find my player!");
        }

        return p;
    }

    public static void addPlayer(Player p) {
        NewPlayerTx tx = new NewPlayerTx(p);

        @SuppressLint("DefaultLocale")
        String message = String.format("%s@(%d, %d) - (%d, %d)", p.getName(), p.getX(), p.getY(), p.newX, p.newY);
        Log.i("Submitting NewPlayerTx:", message);

        Service.getInstance().submitTx(tx);
    }

    public static void movePlayer(String name, int x, int y) {
        MovePlayerTx tx = new MovePlayerTx(name, x, y);

        @SuppressLint("DefaultLocale")
        String message = String.format("%s@(%d, %d)",name, x, y);
        Log.i("Submitting MovePlayerTx", message);

        Service.getInstance().submitTx(tx);
    }

    public static void applyDamage(String name, int hit) {
        ApplyDamageTx tx = new ApplyDamageTx(name, hit);

        @SuppressLint("DefaultLocale")
        String message = String.format("%s - (%d)", name, hit);
        Log.i("Submitting ApplyDmgTx", message);

        Service.getInstance().submitTx(tx);
    }
}