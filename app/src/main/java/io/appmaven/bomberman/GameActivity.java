package io.appmaven.bomberman;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Random;

import io.appmaven.bomberman.models.BabbleNodeListener;
import io.mosaicnetworks.babble.discovery.Peer;
import io.mosaicnetworks.babble.node.BabbleConfig;
import io.mosaicnetworks.babble.node.BabbleNode;
import io.mosaicnetworks.babble.node.KeyPair;
import io.mosaicnetworks.babble.utils.Utils;

public class GameActivity extends Activity {

    private String publicKey;
    private String privateKey;
    private String moniker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        KeyPair keyPair = new KeyPair();
        this.publicKey = keyPair.publicKey;
        this.privateKey = keyPair.privateKey;
        Random rnd = new Random();
        this.moniker = "Player" + rnd.nextInt(1000);

        Intent intent = getIntent();
        int type = intent.getIntExtra(MainActivity.EXTRA_MESSAGE, 0);
        Log.i("TYPE:", "" + type);
        switch(type) {
            case 0:
                startNew(this.moniker);
                break;
            case 1:
                join(this.moniker);
                break;
            default:
        }

        setContentView(new GameView(this));
    }

    public void startNew(String moniker) {
        ArrayList<Peer> peers = new ArrayList<>();
        String ip = Utils.getIPAddr(this);
        peers.add(new Peer(this.publicKey, ip, moniker));

        String peersJSON = new Gson().toJson(peers);

        BabbleConfig.Builder builder = new BabbleConfig.Builder();
        builder.logLevel(BabbleConfig.LogLevel.DEBUG);

        BabbleConfig config = builder.build();

        BabbleNode babbleNode = BabbleNode.createWithConfig(peers, peers, this.privateKey, ip, 1337, moniker, new BabbleNodeListener(), config);

        babbleNode.run();
    }

    public void join(String moniker) {

    }
}
