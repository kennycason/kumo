package com.kennycason.kumo.collide.checkers;

import com.kennycason.kumo.collide.Collidable;
import com.kennycason.kumo.image.CollisionRaster;

import java.awt.*;

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
    public boolean collide(final Collidable collidable, final Collidable collidable2) {
        // check if bounding boxes intersect
        if (!RECTANGLE_COLLISION_CHECKER.collide(collidable, collidable2)) {
            return false;
        }

        final Point position = collidable.getPosition();
        final Point position2 = collidable2.getPosition();
        final CollisionRaster collisionRaster = collidable.getCollisionRaster();
        final CollisionRaster collisionRaster2 = collidable2.getCollisionRaster();

        // get the overlapping box
        final int startX = Math.max(position.x, position2.x);
        final int endX = Math.min(position.x + collidable.getDimension().width,
                                  position2.x + collidable2.getDimension().width);

        final int startY = Math.max(position.y, position2.y);
        final int endY = Math.min(position.y + collidable.getDimension().height,
                                  position2.y + collidable2.getDimension().height);

        for (int y = startY ; y < endY ; y++) {
            for (int x = startX ; x < endX ; x++) {
                // compute offsets for surface
                if (!collisionRaster2.isTransparent(x - position2.x, y - position2.y)
                        && !collisionRaster.isTransparent(x - position.x, y - position.y)) {
                    return true;
                }
            }
        }
        return false;
    }

}
