package wordcloud.collide;

/**
 * Created by kenny on 6/29/14.
 */
public class RectangleCollisionChecker implements CollisionChecker {

    @Override
    public boolean collide(final Collidable c1, Collidable c2) {
        if((c1.getX() + c1.getWidth() < c2.getX()) || (c2.getX() + c2.getWidth() < c1.getX())) {
            return false;
        }
        if((c1.getY() + c1.getHeight() < c2.getY()) || (c2.getY() + c2.getHeight() < c1.getY())) {
            return false;
        }
        return true;
    }

}
