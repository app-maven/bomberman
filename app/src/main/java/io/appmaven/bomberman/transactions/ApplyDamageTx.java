package io.appmaven.bomberman.transactions;

import com.google.gson.Gson;

import io.mosaicnetworks.babble.node.BabbleTx;


public class ApplyDamageTx extends GameTx<ApplyDamage> implements BabbleTx {

    private final static Gson gson = new Gson();

    public ApplyDamageTx(String name, int hit) {
        // set type of transaction
        this.type = Type.APPLY_DAMAGE;


        // set payload data
        this.data = new ApplyDamage(name, hit);
    }

    public static ApplyDamageTx fromJson(String txJson) {
        return gson.fromJson(txJson, ApplyDamageTx.class);
    }

    @Override
    public byte[] toBytes() {
        return gson.toJson(this).getBytes();
    }

}

