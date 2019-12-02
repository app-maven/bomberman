package io.appmaven.bomberman;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import io.mosaicnetworks.babble.node.BabbleNode;
import io.mosaicnetworks.babble.node.KeyPair;

public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(new GameView(this));




//        BabbleNode babbleNode = new BabbleNode(peersJSON, privateKeyHex, netAddr,
//                moniker, new BabbleNodeListeners() {
//            @Override
//            public void onException(String msg) {
//                //do something if babble throws an exception
//            }
//
//            @Override
//            public byte[] onReceiveTransactions(byte[][] transactions) {
//                //process transactions which have gone through consensus
//
//                //return the state hash
//                return new byte[0];
//            }
//        });
//
//        babbleNode.run();
    }
}
