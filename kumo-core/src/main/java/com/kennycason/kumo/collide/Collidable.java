package com.kennycason.kumo.collide;

import com.kennycason.kumo.abst.DimensionAbst;
import com.kennycason.kumo.abst.PointAbst;
import com.kennycason.kumo.image.CollisionRaster;

/**
 * Created by kenny on 6/29/14.
 */
public interface Collidable {
    boolean collide(Collidable collidable);
    PointAbst getPosition();
    DimensionAbst getDimension();
    CollisionRaster getCollisionRaster();
}
