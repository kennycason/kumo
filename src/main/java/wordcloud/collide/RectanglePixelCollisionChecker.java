package wordcloud.collide;

import java.awt.image.BufferedImage;

/**
 * Created by kenny on 7/1/14.
 */
public class RectanglePixelCollisionChecker implements CollisionChecker {

    private static final RectangleCollisionChecker RECTANGLE_COLLISION_CHECKER = new RectangleCollisionChecker();
    /*
        test
        testX,testY____ testX+width
            |                 |
            | sprite          |
            | thisX,thisY     |__ thisX+width
            |  |              |       |
            |__|______________|       |
            testY+height              |
               |______________________|
          thisY+height
*/
    @Override
    public boolean collide(Collidable collidable, Collidable collidable2) {
	    // check if bounding boxes intersect
        if(!RECTANGLE_COLLISION_CHECKER.collide(collidable, collidable2)) {
            return false;
        }

        // get the overlapping box
        int inter_x0 = Math.max(collidable.getX(), collidable2.getX());
        int inter_x1 = Math.min(collidable.getX() + collidable.getWidth(), collidable2.getX() + collidable2.getWidth());

        int inter_y0 = Math.max(collidable.getY(), collidable2.getY());
        int inter_y1 = Math.min(collidable.getY() + collidable.getHeight(), collidable2.getY() + collidable2.getHeight());
        //System.out.println(inter_x0 + " " + inter_x1 + ", " + inter_y0 + " " + inter_y1);

        for(int y = inter_y0 ; y < inter_y1 ; y++) {
            for(int x = inter_x0 ; x < inter_x1 ; x++) {
                // compute offsets for surface
                if((!isTransparent(collidable2.getBufferedImage(), x - collidable2.getX(), y - collidable2.getY()))
                        && (!isTransparent(collidable.getBufferedImage(), x - collidable.getX(), y - collidable.getY()))) {
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
