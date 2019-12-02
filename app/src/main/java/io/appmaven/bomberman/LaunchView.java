package io.appmaven.bomberman;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class LaunchView extends SurfaceView implements SurfaceHolder.Callback {

    public LaunchView(Context context) {
        super(context);
        getHolder().addCallback(this);
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
