package io.appmaven.bomberman.sprites;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;


public class CharacterSprite {
    // direction = 0 up, 1 left, 2 down, 3 right,
    // animation = 3 back, 1 left, 0 front, 2 right
    private static final int[] DIRECTION_TO_ANIMATION_MAP = { 3, 1, 0, 2 };
    private static final int BMP_ROWS = 4;
    private static final int BMP_COLUMNS = 3;

    private final int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private final int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private final int xVelocity = 5;
    private final int yVelocity = 5;

    private Bitmap image;
    private int currentFrame = 0;
    private int width;
    private int height;
    private int x, y = 50;
    private int newX, newY = 50;

    public CharacterSprite(Bitmap bitmap) {
        this.image = bitmap;
        this.width = image.getWidth() / BMP_COLUMNS;
        this.height = image.getHeight() / BMP_ROWS;
    }


    public void draw(Canvas canvas) {
        int srcX = this.currentFrame * this.width;
        int srcY = getAnimationRow() * this.height;
        Rect src = new Rect(srcX, srcY,srcX + this.width,srcY + this.height);
        Rect dst = new Rect(this.x, this.y,this.x + this.width,this.y + this.height);
        canvas.drawBitmap(image, src, dst,null);
    }

    public void update() {

        if(this.newX < this.x - xVelocity) {
            this.x -= xVelocity;
            currentFrame = ++currentFrame % BMP_COLUMNS;
        } else if(this.newX > this.x + xVelocity) {
            this.x += xVelocity;
            currentFrame = ++currentFrame % BMP_COLUMNS;
        }

        if(this.newY < this.y - yVelocity) {
            this.y -= yVelocity;
            currentFrame = ++currentFrame % BMP_COLUMNS;
        } else if(this.newY > this.y + yVelocity) {
            this.y += yVelocity;
            currentFrame = ++currentFrame % BMP_COLUMNS;
        }
    }

    public void moveTo(float newX, float newY) {
        this.newX = (int) (newX - (float) this.width / 2);
        this.newY = (int) (newY - (float) this.height / 2);


        if(this.newX <= (float) width / 2){
            this.newX += (float) width / 2;
        }

        if(this.newX >= screenWidth - width) {
            this.newX -= (float) width / 2;
        }

        if(this.newY <= (float) height / 2){
            this.newY += (float) height / 2;
        }

        if(this.newY >= screenHeight - height) {
            this.newY -= (float) height / 2;
        }
    }

    // direction = 0 up, 1 left, 2 down, 3 right,
    // animation = 3 back, 1 left, 0 front, 2 right
    private int getAnimationRow() {
        int fixX = 1;
        int fixY = 1;
        if(this.newX < this.x) {
            fixX = -1;
        }
        if(this.newY < this.y) {
            fixY = -1;
        }
        double dirDouble = (Math.atan2(this.xVelocity*fixX, this.yVelocity*fixY) / (Math.PI / 2) + 2);
        int direction = (int) Math.round(dirDouble) % BMP_ROWS;
        return DIRECTION_TO_ANIMATION_MAP[direction];
    }
}
