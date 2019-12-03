package io.appmaven.bomberman.scenes;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;


import io.appmaven.bomberman.R;
import io.appmaven.bomberman.models.GamingService;
import io.appmaven.bomberman.models.Player;
import io.appmaven.bomberman.sprites.Grid;
import io.appmaven.bomberman.sprites.TempSprite;
import io.appmaven.bomberman.transactions.ApplyDamageTx;
import io.appmaven.bomberman.transactions.MovePlayerTx;
import io.appmaven.bomberman.transactions.NewPlayerTx;

public class GamePlayScene implements Scene {
    private ArrayList<TempSprite> temps = new ArrayList<>();
    private Grid grid;
    private Resources res;
    private Player player;
    private boolean gameOver = false;


    public GamePlayScene(Resources res) {
        this.res = res;
        this.grid = new Grid(BitmapFactory.decodeResource(res, R.drawable.tile));
        this.player = new Player(getRandomSprite(), GamingService.getInstance().state.getMoniker(), 10, 3);
        NewPlayerTx tx = new NewPlayerTx(this.player);
        GamingService.submit(tx);
    }

    private Bitmap getRandomSprite() {
        TypedArray images = this.res.obtainTypedArray(R.array.avatars);
        int choice = (int) (Math.random() * images.length());
        return BitmapFactory.decodeResource(this.res, images.getResourceId(choice, R.drawable.bad1));
    }

    private Bitmap createSprite(int resource) {
        return BitmapFactory.decodeResource(this.res, resource);
    }

    @Override
    public void update() {
        grid.update();

        for(Player player : GamingService.getInstance().state.getPlayers().values()) {
            player.update();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if(canvas != null) {
            grid.draw(canvas);
            for(Player player : GamingService.getInstance().state.getPlayers().values()) {
                player.draw(canvas);
            }
            for(TempSprite splash : this.temps) {
                splash.draw(canvas);
            }
        }
    }

    @Override
    public void terminate() {
        SceneManager.ACTIVE_SCENE = 0;

    }

    @Override
    public void receiveTouch(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            for(Player player : GamingService.getInstance().state.getPlayers().values()) {
                if(player != this.player) {
                    if(player.isClicked(event.getX(), event.getY())) {
                        if (player.getDistanceFrom(this.player.x, this.player.y) <= 150) {
                            int hit = this.player.attack(player);

                            if (hit > 0) {
                                ApplyDamageTx tx = new ApplyDamageTx(player.getName(), hit);
                                GamingService.submit(tx);
                                temps.add(new TempSprite(temps, createSprite(R.drawable.blood3), player.x + player.width / 2, player.y + player.height / 2, hit));
                            } else if (hit == 0) {
                                ApplyDamageTx tx = new ApplyDamageTx(player.getName(), hit);
                                GamingService.submit(tx);
                                temps.add(new TempSprite(temps, createSprite(R.drawable.splash3), player.x + player.width / 2, player.y + player.height / 2, hit));
                            }
                            if (player.isDead()) {
                                GamingService.getInstance().state.getPlayers().remove(player);
                            }

                            return;
                        } else {
                            Log.i("Distance: ", "You need to be standing closer to attack.");
                            return;
                        }
                    }
                }
            }

            MovePlayerTx tx = new MovePlayerTx(this.player.getName(), (int) event.getX(), (int) event.getY());
            GamingService.submit(tx);

        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            // add code here if particle behavior changes on finger lift
        }
    }
}
