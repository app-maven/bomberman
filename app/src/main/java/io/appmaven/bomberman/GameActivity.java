package io.appmaven.bomberman;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.List;

import io.mosaicnetworks.babble.discovery.Peer;
import io.mosaicnetworks.babble.discovery.ResponseListener;
import io.mosaicnetworks.babble.node.BabbleService;
import io.mosaicnetworks.babble.utils.Utils;
import io.mosaicnetworks.babble.discovery.HttpPeerDiscoveryRequest;

import io.appmaven.bomberman.babble.Service;

public class GameActivity extends Activity implements ResponseListener {

    private HttpPeerDiscoveryRequest httpCurrentPeerDiscoveryRequest;
    private List<Peer> gPeers;
    private List<Peer> cPeers;

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
        Service.getInstance().configureNew(moniker, Utils.getIPAddr(this));

        Service.getInstance().state.setMoniker(moniker);

        // Start instance
        Service.getInstance().start();

        if (Service.getInstance().getState() != BabbleService.State.RUNNING_WITH_DISCOVERY) {
            Toast.makeText(this, "Unable to advertise peers", Toast.LENGTH_LONG).show();
        }
    }

    public void join(String moniker) {
        String ip = Utils.getIPAddr(this);

        // TODO: Remove hardcoded IP
        this.getPeers(Constants.IP);

        // Set local moniker
        Service.getInstance().state.setMoniker(moniker);

        Service.getInstance().configureJoin(this.gPeers, this.cPeers, moniker, ip);
        Service.getInstance().start();
    }

    @Override
    public void onBackPressed() {
        Service.getInstance().leave(null);

        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // RESPONSE LISTENER
    private void getPeers(final String peerIP) {
        HttpPeerDiscoveryRequest httpGenesisPeerDiscoveryRequest;

        try {
            httpGenesisPeerDiscoveryRequest =
                    HttpPeerDiscoveryRequest.createGenesisPeersRequest(
                            peerIP,
                            Service.DEFAULT_DISCOVERY_PORT,
                            new ResponseListener() {
                                @Override
                                public void onReceivePeers(List<Peer> genesisPeers) {
                                    gPeers = genesisPeers;

                                    httpCurrentPeerDiscoveryRequest =
                                            HttpPeerDiscoveryRequest.createCurrentPeersRequest(
                                                    peerIP,
                                                    Service.DEFAULT_DISCOVERY_PORT,
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

        Log.e("onFailure: ", "" + messageId);
    }
}
