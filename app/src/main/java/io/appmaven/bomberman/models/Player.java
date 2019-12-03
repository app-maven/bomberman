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
    private boolean isAttacking = false;
    private boolean isDead = false;
    private int attackTimer = 5;


    public Player(Bitmap image, String name, int hp, int max) {
        super(image);
        this.name = name;
        this.hp = hp;
        this.maxHit = max;
        this.rnd = new Random();
        this.startTick();
    }

    public void takeHit(int hit) {
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


    public int attack(Player player) {
        if(this.attackTimer <= 0) {
            if (player != null && player != this && !player.isDead) {
                int hit = calculateNextHit();
                this.attackTimer = 5;
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


    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getMaxHit() {
        return maxHit;
    }

    public void setMaxHit(int maxHit) {
        this.maxHit = maxHit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public void setAttacking(boolean attacking) {
        isAttacking = attacking;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public int getAttackTimer() {
        return attackTimer;
    }

    public void setAttackTimer(int attackTimer) {
        this.attackTimer = attackTimer;
    }
}
