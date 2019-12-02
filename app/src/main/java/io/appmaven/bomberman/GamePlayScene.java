package io.appmaven.bomberman;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;

import io.appmaven.bomberman.models.Player;
import io.appmaven.bomberman.sprites.Grid;
import io.appmaven.bomberman.sprites.TempSprite;

public class GamePlayScene implements Scene {
    private SceneManager manager;
    private Player player;
    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<TempSprite> temps = new ArrayList<>();
    private Grid grid;
    private Resources res;


    public GamePlayScene(Resources res) {
        this.res = res;
        this.grid = new Grid(BitmapFactory.decodeResource(res, R.drawable.tile));
        this.player = new Player(createSprite(R.drawable.good1), "Nami", 10, 3);
        this.populatePlayers(1);
    }

    private void populatePlayers(int amount){
        for(int i=0;i<amount;i++){
            this.players.add(new Player(createSprite(R.drawable.bad1), "Player" + i, 10, 3));
        }
        this.players.add(this.player);
    }

    private Bitmap createSprite(int resource) {
        return BitmapFactory.decodeResource(this.res, resource);
    }

    @Override
    public void update() {
        grid.update();
        for(Player player : this.players) {
            player.update();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if(canvas != null) {
            grid.draw(canvas);
            for(Player player : this.players) {
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
            for(Player player : this.players) {
                if(player.isClicked(event.getX(), event.getY())){
                    this.player.setTarget(player);
                    int hit = this.player.attack();
                    if(hit > 0){
                        temps.add(new TempSprite(temps, createSprite(R.drawable.blood3), player.x + player.width/2, player.y + player.height/2, hit));
                    } else if(hit == 0) {
                        temps.add(new TempSprite(temps, createSprite(R.drawable.splash3), player.x + player.width/2, player.y + player.height/2, hit));
                    }
                    if(player.isDead){
                        this.players.remove(player);
                    }
                    return;
                }
            }
            this.player.moveTo(event.getX(), event.getY());
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            // add code here if particle behavior changes on finger lift
        }
    }
}
