package io.appmaven.bomberman.scenes;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;


import io.appmaven.bomberman.R;
import io.appmaven.bomberman.models.GamingService;
import io.appmaven.bomberman.models.Player;
import io.appmaven.bomberman.sprites.Grid;
import io.appmaven.bomberman.sprites.TempSprite;

public class GamePlayScene implements Scene {
    private ArrayList<TempSprite> temps = new ArrayList<>();
    private Grid grid;
    private Resources res;
    private Player player;
    private Context context;

    public GamePlayScene(Context context, Resources res) {
        this.context = context;
        this.res = res;
        this.grid = new Grid(BitmapFactory.decodeResource(res, R.drawable.tile));
        this.player = new Player(getRandomSprite(), GamingService.getInstance().state.getMoniker(), 10, 3);
        GamingService.addNewPlayer(this.player);
        GamingService.addNewPlayer(new Player(getRandomSprite(), "Player51", 10, 3));
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
        SceneManager.setActiveScene(0);
    }

    @Override
    public void receiveTouch(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            for(Player p : GamingService.getInstance().state.getPlayers().values()) {
                Log.i("Player: ", p.getName() + ": " + p.x + ", " + p.y);
                if(!p.getName().equalsIgnoreCase(this.player.getName())) {
                    if(p.isClicked(event.getX(), event.getY())) {
                        // This will depend on DPI?
                        Log.i("Checking: ", p.getName() + ": " + p.x + ", " + p.y + " to " + this.player.getName() + ": " + this.player.x + ", " + this.player.y);
                        Log.i("Distance: ", p.getDistanceFrom(this.player.x, this.player.y) + "");

                        if (p.getDistanceFrom(this.player.x, this.player.y) <= 150) {
                            int hit = this.player.attack(p);

                            if (hit > 0) {
                                GamingService.applyDamage(p.getName(), hit);
                                temps.add(new TempSprite(temps, createSprite(R.drawable.blood3), p.x + p.width / 2, p.y + p.height / 2, hit));
                            } else if (hit == 0) {
                                GamingService.applyDamage(p.getName(), hit);
                                temps.add(new TempSprite(temps, createSprite(R.drawable.splash3), p.x + p.width / 2, p.y + p.height / 2, hit));
                            }
                            if (p.isDead()) {
                                GamingService.getInstance().state.getPlayers().remove(p.getName());
                            }

                            return;
                        } else {
                            // oast.makeText(this.context, "You can't attack from that far.", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                }
            }
            GamingService.movePlayer(this.player.getName(), (int) event.getX(), (int) event.getY());
        }
    }
}
