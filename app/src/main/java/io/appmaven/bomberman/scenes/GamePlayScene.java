package io.appmaven.bomberman.scenes;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Handler;
import android.view.MotionEvent;
import android.widget.Toast;

import java.util.ArrayList;


import io.appmaven.bomberman.R;
import io.appmaven.bomberman.models.GamingService;
import io.appmaven.bomberman.models.Player;
import io.appmaven.bomberman.models.PlayerState;
import io.appmaven.bomberman.sprites.Grid;
import io.appmaven.bomberman.sprites.TempSprite;

public class GamePlayScene implements Scene {
    private ArrayList<TempSprite> temps = new ArrayList<>();
    private Grid grid;
    private Resources res;
    private Context context;
    private Handler handler;

    public GamePlayScene(Context context, Resources res) {
        this.context = context;
        this.res = res;
        this.grid = new Grid(BitmapFactory.decodeResource(res, R.drawable.tile));
        this.handler = new Handler();

        PlayerState myPlayerState = new PlayerState(GamingService.getInstance().state.getMoniker(), 200, 200, 10);
        myPlayerState.setAvatar(getRandomSprite());
        myPlayerState.setMax(3);
        GamingService.addNewPlayer(myPlayerState);

        PlayerState secondPlayerState = new PlayerState("Player511", 300, 200, 5);
        secondPlayerState.setAvatar(getRandomSprite());
        secondPlayerState.setMax(3);
        GamingService.addNewPlayer(secondPlayerState);
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

        for(Player player : GamingService.getInstance().state.getLocalPlayers().values()) {
            player.update();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if(canvas != null) {
            grid.draw(canvas);
            for(Player player : GamingService.getInstance().state.getLocalPlayers().values()) {
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
            final Player myPlayer = GamingService.getInstance().state.getLocalPlayers().get(GamingService.getInstance().state.getMoniker());

            for(Player other : GamingService.getInstance().state.getLocalPlayers().values()) {
                if(!other.getName().equalsIgnoreCase(myPlayer.getName())) {
                    if(other.isClicked(event.getX(), event.getY()) && !other.isDead()) {
                        if (other.getDistanceFrom(myPlayer.x, myPlayer.y) <= 150) {
                            int hit = myPlayer.attack(other);
                            if(hit < 0) {
                                // sendNotification("You can attack in " + myPlayer.getAttackTimer() + " seconds.");
                            } else {
                                GamingService.applyDamage(other.getName(), hit);
                                if (hit > 0) {
                                    if(hit >= other.getHp()) {
                                        hit = other.getHp();
                                        temps.add(new TempSprite(temps, createSprite(R.drawable.skull), other.x + other.width / 2, other.y + 10, hit));
                                    } else {
                                        temps.add(new TempSprite(temps, createSprite(R.drawable.blood3), other.x + other.width / 2, other.y + 5, hit));
                                    }
                                } else {
                                    temps.add(new TempSprite(temps, createSprite(R.drawable.splash3), other.x + other.width / 2, other.y + 5, hit));
                                }
                            }
                            return;
                        } else {
                            // sendNotification("You can't attack from that far.");
                            return;
                        }
                    }
                }
            }
            GamingService.movePlayer(myPlayer.getName(), (int) event.getX(), (int) event.getY());
        }
    }
}
