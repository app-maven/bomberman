package io.appmaven.bomberman.sprites;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;



public class Grid {
    private final int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private final int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    private Bitmap image;
    private ArrayList<Tile> tiles;

    public Grid(Bitmap image) {
        this.image = image;
        this.tiles = new ArrayList<>();
        populateTiles();
    }

    private void populateTiles() {
        int left = 0, top = 0;
        float width = this.image.getWidth();
        float height = this.image.getHeight();

        while (left < screenWidth) {
            while (top < screenHeight) {
                Tile tile = new Tile(this.image, left, top);
                tiles.add(tile);
                top += height;
            }
            left += width;
            top = 0;
        }
    }


    public void draw(Canvas canvas) {
        for (Tile tile : this.tiles) {
            tile.draw(canvas);
        }
    }

    public void update() {
        for (Tile tile : this.tiles) {
            tile.update();
        }
    }
}
