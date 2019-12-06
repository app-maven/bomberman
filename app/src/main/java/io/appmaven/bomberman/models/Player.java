package io.appmaven.bomberman.models;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
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

    public int getNextHit() {
        Log.i("getNextHit: ", "Player Attack Timer: " + attackTimer);
        if(this.attackTimer <= 0) {
            int hit = calculateNextHit();
            this.attackTimer = 2;
            return hit;
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
                        Log.i("startTick: ", "Player Attack Timer: " + attackTimer);
                    }
                });
            }
        };

        timer.scheduleAtFixedRate(timerTask, 0, 1000);
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
        final int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        final int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

        this.newX = (int) (x - (float) this.width / 2);
        this.newY = (int) (y - (float) this.height / 2);


        if(this.newX <= (float) this.width / 2){
            this.newX += (float) this.width / 2;
        }

        if(this.newX >= screenWidth - this.width) {
            this.newX -= (float) this.width / 2;
        }

        if(this.newY <= (float) this.height / 2){
            this.newY += (float) this.height / 2;
        }

        if(this.newY >= screenHeight - this.height) {
            this.newY -= (float) this.height / 2;
        }
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
