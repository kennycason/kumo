package com.kennycason.kumo.bg;

import com.kennycason.kumo.collide.RectanglePixelCollidable;
import com.kennycason.kumo.draw.Dimension;
import com.kennycason.kumo.draw.Point;
import com.kennycason.kumo.collide.Collidable;
import com.kennycason.kumo.image.CollisionRaster;

/**
 * Created by kenny on 6/30/14.
 */
public class CircleBackground implements Background {

    private final int radius;

    private final Point position;

    public CircleBackground(final int radius) {
        this.radius = radius;
        this.position = new Point(0, 0);
    }
    
    @Override
    public void mask(RectanglePixelCollidable background) {
        Dimension dimensionOfBackground = background.getDimension();
        CollisionRaster rasterOfBackground = background.getCollisionRaster();

        for (int y = 0; y < dimensionOfBackground.getHeight(); y++) {
            for (int x = 0; x < dimensionOfBackground.getWidth(); x++) {
                if (!inCircle(x, y)) {
                    rasterOfBackground.setPixelIsNotTransparent(
                            position.getX() + x, position.getY() + y
                    );
                }
            }
        }
    }

    private boolean inCircle(final int x, final int y) {
        final int centerX = position.getX() + x - radius;
        final int centerY = position.getY() + y - radius;
        return  (centerX * centerX) + (centerY * centerY) <= radius * radius;
    }

}
