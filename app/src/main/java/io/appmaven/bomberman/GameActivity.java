package io.appmaven.bomberman;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.appmaven.bomberman.models.GamingService;
import io.mosaicnetworks.babble.discovery.Peer;
import io.mosaicnetworks.babble.discovery.ResponseListener;
import io.mosaicnetworks.babble.node.BabbleService;
import io.mosaicnetworks.babble.node.ServiceObserver;
import io.mosaicnetworks.babble.utils.Utils;
import io.mosaicnetworks.babble.discovery.HttpPeerDiscoveryRequest;

public class GameActivity extends Activity implements ServiceObserver, ResponseListener {

    private String moniker;
    private int type;
    private HttpPeerDiscoveryRequest httpGenesisPeerDiscoveryRequest;
    private HttpPeerDiscoveryRequest httpCurrentPeerDiscoveryRequest;
    private List<Peer> genesisPeers;
    private List<Peer> currentPeers;
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

    private void getPeers(final String peerIP) {
        try {
            httpGenesisPeerDiscoveryRequest =
                    HttpPeerDiscoveryRequest.createGenesisPeersRequest(
                            peerIP,
                            GamingService.DEFAULT_DISCOVERY_PORT,
                            new ResponseListener() {
                                @Override
                                public void onReceivePeers(List<Peer> genesisPeers) {
                                    genesisPeers = genesisPeers;

                                    httpCurrentPeerDiscoveryRequest =
                                            HttpPeerDiscoveryRequest.createCurrentPeersRequest(
                                                    peerIP,
                                                    GamingService.DEFAULT_DISCOVERY_PORT,
                                                    GameActivity.this,
                                                    GameActivity.this);

                                    httpCurrentPeerDiscoveryRequest.send();
                                }

                                @Override
                                public void onFailure(Error error) {
                                    GameActivity.this.onFailure(error);
                                }
                            }, this);
        } catch (IllegalArgumentException ex) {
            return;
        }

        httpGenesisPeerDiscoveryRequest.send();
    }


    @Override
    public void onReceivePeers(List<Peer> currentPeers) {
        this.currentPeers = currentPeers;
    }

    @Override
    public void onFailure(io.mosaicnetworks.babble.discovery.ResponseListener.Error error) {
        int messageId;
        switch (error) {
            case INVALID_JSON:
                messageId = R.string.peers_json_error_alert_message;
                break;
            case CONNECTION_ERROR:
                messageId = R.string.peers_connection_error_alert_message;
                break;
            case TIMEOUT:
                messageId = R.string.peers_timeout_error_alert_message;
                break;
            default:
                messageId = R.string.peers_unknown_error_alert_message;
        }
    }

    public void join(String moniker) {
        String ip = Utils.getIPAddr(this);
        this.getPeers(ip);
        this.gamingService.configureJoin(this.genesisPeers, this.currentPeers, moniker, ip);
        this.gamingService.state.setMoniker(moniker);
        this.gamingService.registerObserver(this);
        this.gamingService.start();
    }

    @Override
    public void stateUpdated() {
        // Splash here
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
