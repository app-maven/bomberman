package io.appmaven.bomberman.scenes;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;

public class SceneManager {
    private ArrayList<Scene> scenes = new ArrayList<>();
    private static int ACTIVE_SCENE = 0;

    public SceneManager(Context context, Resources res) {
        scenes.add(new GamePlayScene(context, res));
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

    public static int getActiveScene() {
        return ACTIVE_SCENE;
    }

    public static void setActiveScene(int scene) {
        ACTIVE_SCENE = scene;
    }
}
