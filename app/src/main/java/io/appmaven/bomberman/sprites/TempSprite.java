package io.appmaven.bomberman.sprites;

import java.util.List;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

public class TempSprite {
    private float x;
    private float y;
    private Bitmap image;
    private int life = 15;
    private List<TempSprite> temps;
    private final int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private final int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private int damage;

    public TempSprite(List<TempSprite> temps, Bitmap image, float x, float y, int damage) {
        this.x = Math.min(Math.max(x - image.getWidth() / 2, 0), screenWidth - image.getWidth());
        this.y = Math.min(Math.max(y - image.getHeight() / 2, 0), screenHeight - image.getHeight());
        this.damage = damage;
        this.image = image;
        this.temps = temps;
    }

    public void draw(Canvas canvas) {
        update();
        Paint paint = new Paint();
        paint.setColor(Color.WHITE); // Text Color
        paint.setTextSize(50); // Text Size
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)); // Text Overlapping Pattern
        canvas.drawBitmap(this.image, this.x, this.y, paint);
        canvas.drawText("" + this.damage, this.x + this.image.getWidth()/2, this.y + this.image.getHeight()/2, paint);
    }

    private void update() {
        if (--life < 1) {
            temps.remove(this);
        }
    }
}