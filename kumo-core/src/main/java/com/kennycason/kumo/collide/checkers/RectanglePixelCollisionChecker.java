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

        // this is the fast path of finding collisions:
        // we expect the none transparent pixel to be around the center.
        // using padding will increase this effect.

        // check only every 9th pixel to move faster to the center.
        if (fastCollide(
                collisionRaster, position, collisionRaster2, position2,
                startX, endX, startY, endY, 9
        )) {
            return true;
        }
        
        // check only every 4th pixel to move faster to the center on another raster than 9.
        if (fastCollide(
                collisionRaster, position, collisionRaster2, position2,
                startX, endX, startY, endY, 4
        )) {
            return true;
        }
        
        // test all pixels for collision
        for (int y = startY; y < endY; y++) {
            int yOfPosition = y - position.y;
            int yOfPosition2 = y - position2.y;

            if (collisionRaster2.lineIsTransparent(yOfPosition2)
             || collisionRaster.lineIsTransparent(yOfPosition)) {
               continue;
            }
            
            for (int x = startX; x < endX; x++) {
                // compute offsets for surface
                if (!collisionRaster2.isTransparent(x - position2.x, yOfPosition2)
                        && !collisionRaster.isTransparent(x - position.x, yOfPosition)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean fastCollide(final CollisionRaster collisionRaster, final Point position, final CollisionRaster collisionRaster2, final Point position2, final int startX, final int endX, final int startY, final int endY, int stepSize) {
        for (int y = startY + stepSize; y < endY; y += stepSize) {
            int yOfPosition = y - position.y;
            int yOfPosition2 = y - position2.y;
            
            if (collisionRaster2.lineIsTransparent(yOfPosition2)
             || collisionRaster.lineIsTransparent(yOfPosition)) {
               continue;
            }
            
            for (int x = startX + stepSize; x < endX; x += stepSize) {
                // compute offsets for surface
                if (!collisionRaster2.isTransparent(x - position2.x, yOfPosition2)
                        && !collisionRaster.isTransparent(x - position.x, yOfPosition)) {
                    return true;
                }
            }
        }
        return false;
    }
}
