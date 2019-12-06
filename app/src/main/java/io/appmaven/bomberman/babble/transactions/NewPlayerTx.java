package io.appmaven.bomberman.babble.transactions;

import com.google.gson.Gson;

import io.mosaicnetworks.babble.node.BabbleTx;

import io.appmaven.bomberman.models.Player;

public class NewPlayerTx extends Transaction<Player> implements BabbleTx {
        private final static Gson gson = new Gson();

        public NewPlayerTx(Player player) {
            // set type of transaction
            this.type = Type.NEW_PLAYER;

            // set payload data
            this.data = player;
        }

        public static NewPlayerTx fromJson(String txJson) {
            return gson.fromJson(txJson, NewPlayerTx.class);
        }

        @Override
        public byte[] toBytes() {
            return gson.toJson(this).getBytes();
        }
}
