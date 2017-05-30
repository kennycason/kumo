package com.kennycason.kumo.collide.checkers;

import com.kennycason.kumo.collide.Collidable;

/**
 * Created by kenny on 7/1/14.
 */
public interface CollisionChecker {
    boolean collide(Collidable collidable, Collidable collidable2);
}
