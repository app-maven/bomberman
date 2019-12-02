package io.appmaven.bomberman;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.widget.ImageView;

import io.appmaven.bomberman.sprites.CharacterSprite;
import io.appmaven.bomberman.sprites.Grid;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread thread;
    private CharacterSprite characterSprite;
    private Grid grid;


    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);
        setFocusable(true);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        grid = new Grid(BitmapFactory.decodeResource(getResources(), R.drawable.tile));
        characterSprite = new CharacterSprite(BitmapFactory.decodeResource(getResources(), R.drawable.bad1));
        thread.setRunning(true);
        thread.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while(retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }


    public void update() {
        grid.update();
        characterSprite.update();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if(canvas != null) {
            // canvas.drawColor(Color.WHITE);
            grid.draw(canvas);
            characterSprite.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            characterSprite.moveTo(event.getX(), event.getY());
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            // add code here if particle behavior changes on finger lift
        }
        return false;
    }
}
