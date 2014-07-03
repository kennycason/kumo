package wordcloud.collide;

import wordcloud.collide.checkers.RectanglePixelCollisionChecker;

import java.awt.image.BufferedImage;

/**
 * Created by kenny on 7/2/14.
 */
public class RectanglePixelCollidable implements Collidable {

    private static final RectanglePixelCollisionChecker RECTANGLE_PIXEL_COLLISION_CHECKER = new RectanglePixelCollisionChecker();

    private final Vector2d position;

    private final BufferedImage bufferedImage;

    public RectanglePixelCollidable(BufferedImage bufferedImage, int x, int y) {
        this.bufferedImage = bufferedImage;
        this.position = new Vector2d(x, y);
    }

    public int getX() {
        return position.getX();
    }

    public int getY() {
        return position.getY();
    }

    @Override
    public boolean collide(Collidable collidable) {
        return RECTANGLE_PIXEL_COLLISION_CHECKER.collide(this, collidable);
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    public int getWidth() {
        return bufferedImage.getWidth();
    }

    public int getHeight() {
        return bufferedImage.getHeight();
    }

    @Override
    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

}
