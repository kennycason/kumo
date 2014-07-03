package wordcloud.collide.checkers;

import wordcloud.collide.Collidable;
import wordcloud.collide.Vector2d;

/**
 * Created by kenny on 6/29/14.
 */
public class RectangleCollisionChecker implements CollisionChecker {

    @Override
    public boolean collide(final Collidable collidable, Collidable collidable2) {
        final Vector2d position = collidable.getPosition();
        final Vector2d position2 = collidable2.getPosition();

        if((position.getX() + collidable.getWidth() < position2.getX()) || (position2.getX() + collidable2.getWidth() < position.getX())) {
            return false;
        }
        if((position.getY() + collidable.getHeight() < position2.getY()) || (position2.getY() + collidable2.getHeight() < position.getY())) {
            return false;
        }
        return true;
    }

}
