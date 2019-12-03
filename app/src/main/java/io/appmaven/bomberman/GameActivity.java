package io.appmaven.bomberman;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import io.appmaven.bomberman.models.GamingService;
import io.mosaicnetworks.babble.node.BabbleService;
import io.mosaicnetworks.babble.node.ServiceObserver;
import io.mosaicnetworks.babble.utils.Utils;

public class GameActivity extends Activity implements ServiceObserver {

    private String moniker;
    private int type;
    private final GamingService gamingService = GamingService.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);


        Intent intent = getIntent();
        this.type = intent.getIntExtra(MainActivity.EXTRA_TYPE, 0);
        this.moniker = intent.getStringExtra(MainActivity.EXTRA_MONIKER);


        switch(type) {
            case 0:
                this.startNew(this.moniker);
                break;
            case 1:
                this.join(this.moniker);
                break;
            default:
        }

        setContentView(new GameView(this));
    }

    public void startNew(String moniker) {
        this.gamingService.configureNew(moniker, Utils.getIPAddr(this));
        this.gamingService.state.setMoniker(moniker);
        this.gamingService.registerObserver(this);
        this.gamingService.start();

        if (gamingService.getState()!= BabbleService.State.RUNNING_WITH_DISCOVERY) {
            Toast.makeText(this, "Unable to advertise peers", Toast.LENGTH_LONG).show();
        }
    }

    public void join(String moniker) {
//        this.gamingService.configureJoin(moniker, Utils.getIPAddr(this));
//        this.gamingService.state.setMoniker(moniker);
//        this.gamingService.registerObserver(this);
//        this.gamingService.start();
//
//        if (gamingService.getState()!= BabbleService.State.RUNNING_WITH_DISCOVERY) {
//            Toast.makeText(this, "Unable to advertise peers", Toast.LENGTH_LONG).show();
//        }
    }

    @Override
    public void stateUpdated() {
//        final List<Player> newPlayers = gamingService.state.getMessagesFromIndex(mMessageIndex);
//
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                for (Player player : newPlayers ) {
//                    mAdapter.addToStart(message, true);
//                }
//            }
//        });
//
//        mMessageIndex = mMessageIndex + newMessages.size();
    }

    @Override
    public void onBackPressed() {
        gamingService.leave(null);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        gamingService.removeObserver(this);
        super.onDestroy();
    }
}
