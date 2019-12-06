package io.appmaven.bomberman.babble.transactions;

import com.google.gson.Gson;

import io.mosaicnetworks.babble.node.BabbleTx;

import io.appmaven.bomberman.models.ApplyDamage;

public class ApplyDamageTx extends Transaction<ApplyDamage> implements BabbleTx {
    private final static Gson gson = new Gson();

    public ApplyDamageTx(String name, int hit) {
        // set type of transaction
        this.type = Transaction.Type.APPLY_DAMAGE;


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
