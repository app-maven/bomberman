package io.appmaven.bomberman.scenes;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;

public class SceneManager {
    private ArrayList<Scene> scenes = new ArrayList<>();
    public static int ACTIVE_SCENE;

    public SceneManager(Resources res) {
        this.ACTIVE_SCENE = 0;
        scenes.add(new GamePlayScene(res));
    }

    public void receiveTouch(MotionEvent event) {
        scenes.get(ACTIVE_SCENE).receiveTouch(event);
    }

    public void update(){
        scenes.get(ACTIVE_SCENE).update();

    }

    public void draw(Canvas canvas) {
        scenes.get(ACTIVE_SCENE).draw(canvas);
    }
}