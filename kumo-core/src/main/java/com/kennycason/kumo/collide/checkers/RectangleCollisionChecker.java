package com.kennycason.kumo.collide.checkers;

import com.kennycason.kumo.collide.Collidable;
import com.kennycason.kumo.interfaces.PointAbst;

/**
 * Created by kenny on 6/29/14.
 */
public class RectangleCollisionChecker implements CollisionChecker {

    @Override
    public boolean collide(final Collidable collidable, final Collidable collidable2) {
        final PointAbst position = collidable.getPosition();
        final PointAbst position2 = collidable2.getPosition();

        if ((position.getX() + collidable.getDimension().getWidth() < position2.getX())
                || (position2.getX() + collidable2.getDimension().getWidth() < position.getX())) {
            return false;
        }
        if ((position.getY() + collidable.getDimension().getHeight() < position2.getY())
                || (position2.getY() + collidable2.getDimension().getHeight() < position.getY())) {
            return false;
        }
        return true;
    }

}
