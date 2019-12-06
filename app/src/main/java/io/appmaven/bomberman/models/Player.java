package io.appmaven.bomberman.models;

import android.graphics.Bitmap;
import android.os.Handler;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import io.appmaven.bomberman.sprites.CharacterSprite;

public class Player extends CharacterSprite {
    private String name;

    private int hp;
    private int maxHit;

    private boolean isAttacking = false;
    private boolean isDead = false;

    private int attackTimer = 3;

    // hits
    private ArrayList<Integer> hits = new ArrayList<>();

    public Player(Bitmap image, String name, int hp, int max) {
        super(image);
        this.name = name;
        this.hp = hp;
        this.maxHit = max;

        this.x = 200;
        this.newX = 200;

        this.y = 200;
        this.newY = 200;

        this.startTick();
    }

    public void takeHit(int hit) {
        this.hits.add(hit);
    }

    private void applyDamage() {
        int totalDmg = 0;

        for(int n : hits) {
            totalDmg += n;
        }

        if(this.getHp() > totalDmg) {
            this.setHp(this.getHp() - totalDmg);
        } else {
            this.setHp(0);
        }

        // clear hits
        this.hits.clear();
    }

    @Override
    public void update() {
        super.update();
        this.applyDamage();
    }

    public int attack(Player player) {
        if(this.attackTimer <= 0) {
            if (player != null && player != this && !player.isDead) {
                int hit = calculateNextHit();

                this.attackTimer = 2;

                return hit;
            }
        }

        return -1;
    }

    private int calculateNextHit() {
        Random rnd = new Random();
        return rnd.nextInt(this.maxHit);
    }

    public boolean isClicked(float x, float y) {
        if(x < this.x + this.width && x >= this.x) {
            return (y < this.y + this.height && y >= this.y);
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
        if(hp < 0) {
            this.hp = 0;
        } else {
            this.hp = hp;
        }
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

    public void setNewPosition(int x, int y) {
        this.newX = x;
        this.newY = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}
