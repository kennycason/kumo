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

        final int endX1 = endX - position.x;
        final int endX2 = endX - position2.x;

        final int stop1 = position.x + -1;
        final int stop2 = position2.x + -1;
        
        // this is the fast path of finding collisions: we expect the none transparent
        // pixel to be around the center, using padding will increase this effect.
        final int stepSize = ((endY - startY) / 3) + 1;
        
        for (int i = stepSize - 1; i >= 0; i--) {
            for (int y = startY + i; y < endY; y += stepSize) {
                final int yOfPosition = y - position.y;
                int absolute1 = position.x + collisionRaster.nextNotTransparentPixel(
                        startX - position.x, endX1, yOfPosition
                );

                if (absolute1 == stop1) {
                    continue;
                }

                final int yOfPosition2 = y - position2.y;
                int absolute2 = position2.x + collisionRaster2.nextNotTransparentPixel(
                        startX - position2.x, endX2, yOfPosition2
                );

                if (absolute2 == stop2) {
                    continue;
                }
                
                while (true) {
                    if (absolute1 > absolute2) {
                        absolute2 = position2.x + collisionRaster2.nextNotTransparentPixel(
                                absolute1 - position2.x, endX2, yOfPosition2
                        );
                        
                        if (absolute2 == stop2) {
                            break;
                        } 
                    } else if (absolute1 < absolute2) {
                        absolute1 = position.x + collisionRaster.nextNotTransparentPixel(
                                absolute2 - position.x, endX1, yOfPosition
                        );
                        
                        if (absolute1 == stop1) {
                            break;
                        } 
                    } else {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
