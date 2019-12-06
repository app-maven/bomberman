package io.appmaven.bomberman.sprites;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Tile {

    private Bitmap image;
    private float x, y;

    Tile(Bitmap bitmap, int x, int y) {
        this.image = bitmap;
        this.x = x;
        this.y = y;
    }


    void draw(Canvas canvas) {
        canvas.drawBitmap(this.image, this.x, this.y, null);
    }

    public void update() {

    }
}
