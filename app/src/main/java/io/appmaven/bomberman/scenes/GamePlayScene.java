package io.appmaven.bomberman.scenes;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import java.util.ArrayList;

import io.appmaven.bomberman.R;
import io.appmaven.bomberman.babble.Service;
import io.appmaven.bomberman.models.Player;
import io.appmaven.bomberman.sprites.Grid;
import io.appmaven.bomberman.sprites.TempSprite;

public class GamePlayScene implements Scene {
    private ArrayList<TempSprite> temps = new ArrayList<>();

    private Grid grid;
    private Resources res;
    private Handler handler;

    private Context context;

    public GamePlayScene(Context context, Resources res) {
        this.context = context;
        this.res = res;
        this.grid = new Grid(BitmapFactory.decodeResource(res, R.drawable.tile));
        this.handler = new Handler();

        Player p1 = new Player(getRandomSprite(), Service.getInstance().state.getMoniker(), 10, 10);
        Service.addPlayer(p1);

        Player p2 = new Player(getRandomSprite(), "Player 9999", 10, 10);
        Service.addPlayer(p2);
    }

    private Bitmap getRandomSprite() {
        TypedArray images = this.res.obtainTypedArray(R.array.avatars);

        int choice = (int) (Math.random() * images.length());

        Bitmap image =  BitmapFactory.decodeResource(this.res, images.getResourceId(choice, R.drawable.bad1));
        images.recycle();

        return image;
    }

    private Bitmap createSprite(int resource) {
        return BitmapFactory.decodeResource(this.res, resource);
    }

    // TODO: This bugs out the sprites -> Perhaps something to do with UI threading
    private void sendNotification(final String notification) {
        handler.post(new Runnable(){
            public void run(){
                Toast.makeText(context, notification, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void update() {
        grid.update();

        for(Player player : Service.getInstance().state.getPlayers().values()) {
            player.update();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if(canvas != null) {
            grid.draw(canvas);
            for(Player player : Service.getInstance().state.getPlayers().values()) {
                if(!player.isDead()) {
                    player.draw(canvas);
                }
            }
            for(TempSprite splash : this.temps) {
                splash.draw(canvas);
            }
        }
    }

    @Override
    public void terminate() {
        SceneManager.setActiveScene(0);
    }

    @Override
    public void receiveTouch(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            // Fetch my player
            final Player p1 = Service.getMyPlayer();

            for(Player p2 : Service.getInstance().state.getPlayers().values()) {
                // check if player is not me
                if(!p2.getName().equalsIgnoreCase(p1.getName())) {

                    // if click another player and hes not dead
                    if(p2.isClicked(event.getX(), event.getY()) && !p2.isDead()) {

                        // minimum distance required to hit player
                        if (p2.getDistanceFrom(p1.getX(), p1.getY()) <= 300) {
                            int hit = p1.attack(p2);
                            Log.i("Attack", "You attack dealing " + hit + "damage");

                            if(hit < 0) {
                                Log.i("Attack", "You can attack in " + p1.getAttackTimer() + " seconds");
                                // sendNotification("You can attack in " + myPlayer.getAttackTimer() + " seconds.");
                            } else {
                                Service.applyDamage(p2.getName(), hit);

                                if (hit > 0) {
                                    if(hit >= p2.getHp()) {
                                        hit = p2.getHp();

                                        temps.add(new TempSprite(temps, createSprite(R.drawable.skull), p2.getX() + p2.width / 2, p2.getY() + 10, hit));
                                    } else {
                                        temps.add(new TempSprite(temps, createSprite(R.drawable.blood3), p2.getX() + p2.width / 2, p2.getY() + 5, hit));
                                    }
                                } else {
                                    temps.add(new TempSprite(temps, createSprite(R.drawable.splash3), p2.getX() + p2.width / 2, p2.getY() + 5, hit));
                                }
                            }

                            return;
                        } else {
                            Log.i("Attack", "You can't attack from that far");
                            // sendNotification("You can't attack from that far.");
                            return;
                        }

                    }

                }
            }

            // move player;
            Service.movePlayer(p1.getName(), (int) event.getX(), (int) event.getY());
        }
    }
}
