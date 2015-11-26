package wordcloud.bg;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import wordcloud.collide.Collidable;
import wordcloud.collide.Vector2d;

/**
 * Created by kenny on 6/30/14.
 */
public class RectangleBackground implements Background {

    private final int x;
    
    private final int y;
    
    private final int width;

    private final int height;

    public RectangleBackground(int width, int height) {
        this(0, 0, width, height);
    }

    public RectangleBackground(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public RectangleBackground(Point startingPoint, Dimension dimensions) {
        this(startingPoint.x, startingPoint.y, dimensions.width, dimensions.height);
    }
    
    public RectangleBackground(Rectangle rectangle) {
        this(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    @Override
    public boolean isInBounds(Collidable collidable) {
        final Vector2d position = collidable.getPosition();
        return position.getX() >= x && 
               position.getX() + collidable.getWidth() < (x + width)  && 
               position.getY() >= y && 
               position.getY() + collidable.getHeight() < (y + height);
    }

    @Override
    public String toString() {
        return "RectangleBackground [x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + "]";
    }

}
