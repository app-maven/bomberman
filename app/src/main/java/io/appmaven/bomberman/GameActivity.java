package io.appmaven.bomberman;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import io.appmaven.bomberman.models.GamingService;
import io.appmaven.bomberman.models.Player;
import io.appmaven.bomberman.models.PlayerState;
import io.mosaicnetworks.babble.discovery.Peer;
import io.mosaicnetworks.babble.discovery.ResponseListener;
import io.mosaicnetworks.babble.node.BabbleService;
import io.mosaicnetworks.babble.node.ServiceObserver;
import io.mosaicnetworks.babble.utils.Utils;
import io.mosaicnetworks.babble.discovery.HttpPeerDiscoveryRequest;

public class GameActivity extends Activity implements ServiceObserver, ResponseListener {

    private HttpPeerDiscoveryRequest httpGenesisPeerDiscoveryRequest;
    private HttpPeerDiscoveryRequest httpCurrentPeerDiscoveryRequest;
    private List<Peer> gPeers;
    private List<Peer> cPeers;
    private final GamingService gamingService = GamingService.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);


        Intent intent = getIntent();
        int type = intent.getIntExtra(MainActivity.EXTRA_TYPE, 0);
        String moniker = intent.getStringExtra(MainActivity.EXTRA_MONIKER);


        switch(type) {
            case 0:
                this.startNew(moniker);
                break;
            case 1:
                this.join(moniker);
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
        String ip = Utils.getIPAddr(this);

        // TODO: Remove hardcoded IP
        this.getPeers(Constants.IP);

        this.gamingService.configureJoin(this.gPeers, this.cPeers, moniker, ip);
        this.gamingService.state.setMoniker(moniker);
        this.gamingService.registerObserver(this);
        this.gamingService.start();
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
                                    gPeers = genesisPeers;

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
        this.cPeers = currentPeers;
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

    @Override
    public void stateUpdated() {
        Log.i("StateUpdated:", "Called!");
        final Map<String, PlayerState> newPlayerStates = gamingService.state.getGlobalPlayers();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for(PlayerState state : newPlayerStates.values() ) {
                    try {
                        Player p = GamingService.getInstance().state.getLocalPlayers().get(state.getName());
                        if(p  == null) {
                            GamingService.getInstance().state.getLocalPlayers().put(state.getName(), Player.makePlayer(state));
                        } else {
                            if (p.getHp() > state.getHp()) {
                                p.takeHit(p.getHp() - state.getHp());
                            }

                            if (p.x != state.getX() || p.y != state.getY()) {
                                Log.i("Move State changed", p.getName() + " From: " + p.x + ":" + p.y + " To: " + state.getX() + ":" + state.getY());
                                p.moveTo(state.getX(), state.getY());
                            }
                        }

                    } catch (Exception e) {
                        Log.i("Exception:", e.getMessage());
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        this.gamingService.leave(null);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        this.gamingService.removeObserver(this);
        super.onDestroy();
    }
}
