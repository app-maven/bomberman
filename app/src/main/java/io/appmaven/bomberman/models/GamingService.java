package io.appmaven.bomberman.models;

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
        super(new AppState());
    }

    public static void submit(BabbleTx tx) {
        GamingService.getInstance().submitTx(tx);
    }
}