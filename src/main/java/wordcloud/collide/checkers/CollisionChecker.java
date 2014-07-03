package wordcloud.collide.checkers;

import wordcloud.collide.Collidable;

/**
 * Created by kenny on 7/1/14.
 */
public interface CollisionChecker {
    boolean collide(Collidable collidable, Collidable collidable2);
}
