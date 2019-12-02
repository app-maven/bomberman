package io.appmaven.bomberman.models;

import io.mosaicnetworks.babble.node.TxConsumer;

public class BabbleNodeListener implements TxConsumer {
    @Override
    public byte[] onReceiveTransactions(byte[][] transactions) {
        return new byte[0];
    }
}
