package io.appmaven.bomberman.models;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import io.appmaven.bomberman.sprites.CharacterSprite;

public class Player extends CharacterSprite {
    private int hp;
    private int maxHit;
    private String name;
    private Random rnd;
    private Player target;
    private boolean isAttacking = false;
    public boolean isDead = false;
    private int attackTimer = 5;


    public Player(Bitmap image, String name, int hp, int max) {
        super(image);
        this.name = name;
        this.hp = hp;
        this.maxHit = max;
        this.rnd = new Random();
        this.startTick();
    }

    private void takeHit(int hit) {
        if(this.hp > 0 && this.hp > hit) {
            this.hp -= hit;
        } else {
            this.hp = 0;
        }

        if(this.hp <= 0) {
            this.isAttacking = false;
            this.isDead = true;
        }
        Log.i("Player: " + this.name, "Getting hit: " + hit);
        Log.i("Player: " + this.name, "HP: " + this.hp);
        Log.i("Player: " + this.name, "Dead: " + this.isDead);
    }

    public void setTarget(Player target) {
        this.isAttacking = true;
        this.target = target;
    }

    public int attack() {
        if(this.attackTimer <= 0) {
            if (this.target != null && this.target != this && !this.target.isDead) {
                int hit = calculateNextHit();
                this.target.takeHit(hit);
                this.attackTimer = 2;
                return hit;
            }
        } else {
            Log.i("You can attack in", "seconds: " + this.attackTimer);
        }
        return -1;
    }

    private int calculateNextHit() {
        return this.rnd.nextInt(this.maxHit);
    }

    public boolean isClicked(float x, float y) {
        if(x < this.x + this.width && x >= this.x){
            if(y < this.y + this.height && y >= this.y) {
                return true;
            }
        }
        return false;
    }


    private void startTick() {
        final Handler handler = new Handler();
        Timer timer = new Timer(false);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(attackTimer > 0) {
                            attackTimer--;
                        }
                    }
                });
            }
        };

        timer.scheduleAtFixedRate(timerTask, 1000, 1000); // every 5 seconds.
    }

    public float getDistanceFrom(int x2, int y2) {
        double x = Math.pow((x2-this.x), 2);
        double y = Math.pow((y2-this.y), 2);
        return (float) Math.sqrt(x + y);
    }
}
