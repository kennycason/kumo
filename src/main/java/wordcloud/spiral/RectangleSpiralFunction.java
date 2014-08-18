package wordcloud.spiral;

import wordcloud.collide.Vector2d;

/**
 * Created by kenny on 8/1/14.
 */
public class RectangleSpiralFunction extends SpiralFunction {

    private static enum Direction {UP,DOWN,LEFT,RIGHT}

    private Direction direction = Direction.UP;

    private int x = 0;

    private int y = 0;

    private int radius = 0;

    private final int step;

    private final int maxRadius;

    public RectangleSpiralFunction(final Vector2d start, final int maxRadius) {
        this(start, maxRadius, 2);
    }


    public RectangleSpiralFunction(final Vector2d start, final int maxRadius, final int step) {
        super(start);
        this.maxRadius = maxRadius;
        this.step = step;
    }

    @Override
    public boolean hasNext() {
        return radius <= maxRadius && x != -maxRadius && y != -maxRadius;
    }

    @Override
    public Vector2d next() {
        if(!hasNext()) {
            throw new UnsupportedOperationException("Exceeded Max Radius!");
        }
        if(radius == 0) {
            radius += step;
            return new Vector2d(x, y);
        }
        switch(direction) {
            case UP:
                y--;
                if(y == -radius) {
                    direction = Direction.RIGHT;
                }
                break;

            case RIGHT:
                x++;
                if(x == radius) {
                    direction = Direction.DOWN;
                }
                break;

            case DOWN:
                y++;
                if(y == radius) {
                    direction = Direction.LEFT;
                }
                break;

            case LEFT:
                x--;
                if(x == -radius) {
                    direction = Direction.UP;
                    radius += step;
                }
                break;

        }
        return new Vector2d(x + start.getX(), y + start.getY());
    }

}
