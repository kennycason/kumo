package com.kennycason.kumo.collide.checkers;

import com.kennycason.kumo.draw.Point;
import com.kennycason.kumo.collide.Collidable;
import com.kennycason.kumo.image.CollisionRaster;

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
        final int startX = Math.max(position.getX(), position2.getX());
        final int endX = Math.min(position.getX() + collidable.getDimension().getWidth(),
                                  position2.getX() + collidable2.getDimension().getWidth());

        final int startY = Math.max(position.getY(), position2.getY());
        final int endY = Math.min(position.getY() + collidable.getDimension().getHeight(),
                                  position2.getY() + collidable2.getDimension().getHeight());

        final int endX1 = endX - position.getX();
        final int endX2 = endX - position2.getX();

        final int stop1 = position.getX() + -1;
        final int stop2 = position2.getX() + -1;

        // this is the fast path of finding collisions: we expect the none transparent
        // pixel to be around the center, using padding will increase this effect.
        final int stepSize = ((endY - startY) / 3) + 1;

        for (int i = stepSize - 1; i >= 0; i--) {
            for (int y = startY + i; y < endY; y += stepSize) {
                final int yOfPosition = y - position.getY();
                int absolute1 = position.getX() + collisionRaster.nextNotTransparentPixel(
                        startX - position.getX(), endX1, yOfPosition
                );

                if (absolute1 == stop1) {
                    continue;
                }

                final int yOfPosition2 = y - position2.getY();
                int absolute2 = position2.getX() + collisionRaster2.nextNotTransparentPixel(
                        startX - position2.getX(), endX2, yOfPosition2
                );

                if (absolute2 == stop2) {
                    continue;
                }

                while (true) {
                    if (absolute1 > absolute2) {
                        absolute2 = position2.getX() + collisionRaster2.nextNotTransparentPixel(
                                absolute1 - position2.getX(), endX2, yOfPosition2
                        );

                        if (absolute2 == stop2) {
                            break;
                        }
                    } else if (absolute1 < absolute2) {
                        absolute1 = position.getX() + collisionRaster.nextNotTransparentPixel(
                                absolute2 - position.getX(), endX1, yOfPosition
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
