package wordcloud.collide;

import java.awt.image.BufferedImage;

/**
 * Created by kenny on 7/1/14.
 */
public class RectanglePixelCollisionChecker implements CollisionChecker {

    private static final RectangleCollisionChecker RECTANGLE_COLLISION_CHECKER = new RectangleCollisionChecker();
    /*

          ax,ay ___________ax + a.width
            |                 |
            |                 |
            |  bx, by_________|__ bx + b.width
            |  |(INTERSECTION)|       |
            |__|______________|       |
            ay + height               |
               |______________________|
             by + height
          */
    @Override
    public boolean collide(Collidable collidable, Collidable collidable2) {
	    // check if bounding boxes intersect
        if(!RECTANGLE_COLLISION_CHECKER.collide(collidable, collidable2)) {
            return false;
        }

        final Vector2d position = collidable.getPosition();
        final Vector2d position2 = collidable2.getPosition();

        // get the overlapping box
        int startX = Math.max(position.getX(), position2.getX());
        int endX = Math.min(position.getX() + collidable.getWidth(), position2.getX() + collidable2.getWidth());

        int startY = Math.max(position.getY(), position2.getY());
        int endY = Math.min(position.getY() + collidable.getHeight(), position2.getY() + collidable2.getHeight());

        for(int y = startY ; y < endY ; y++) {
            for(int x = startX ; x < endX ; x++) {
                // compute offsets for surface
                if((!isTransparent(collidable2.getBufferedImage(), x - position2.getX(), y - position2.getY()))
                        && (!isTransparent(collidable.getBufferedImage(), x - position.getX(), y - position.getY()))) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isTransparent(BufferedImage bufferedImage, int x, int y) {
        int pixel = bufferedImage.getRGB(x, y);
        if((pixel & 0xFF000000) == 0x00000000) {
            return true;
        }
        return false;
    }

}
