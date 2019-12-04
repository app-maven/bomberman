package io.appmaven.bomberman.models;

import io.appmaven.bomberman.transactions.ApplyDamageTx;
import io.appmaven.bomberman.transactions.MovePlayerTx;
import io.appmaven.bomberman.transactions.NewPlayerTx;
import io.mosaicnetworks.babble.node.BabbleConfig;
import io.mosaicnetworks.babble.node.BabbleService;
import io.mosaicnetworks.babble.node.BabbleTx;


public class GamingService extends BabbleService<AppState> {
    private static GamingService INSTANCE;

    public static GamingService getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new GamingService();
        }

        return INSTANCE;
    }

    private GamingService() {
        super(new AppState(), new BabbleConfig.Builder().heartbeat(1).logLevel(BabbleConfig.LogLevel.DEBUG).build());
    }

    public static void addNewPlayer(Player p) {
        NewPlayerTx tx = new NewPlayerTx(p);
        GamingService.getInstance().submitTx(tx);
    }

    public static void movePlayer(String name, int x, int y) {
        MovePlayerTx tx = new MovePlayerTx(name, x, y);
        GamingService.getInstance().submitTx(tx);
    }

    public static void applyDamage(String name, int hit) {
        ApplyDamageTx tx = new ApplyDamageTx(name, hit);
        GamingService.getInstance().submitTx(tx);
    }
}