package wordcloud.spiral;

import wordcloud.collide.Vector2d;

import java.util.Iterator;

/**
 * Created by kenny on 8/1/14.
 */
public abstract class SpiralFunction implements Iterator<Vector2d> {

    protected Vector2d start;

    protected SpiralFunction(final Vector2d start) {
        this.start = start;
    }

    @Override
    public abstract boolean hasNext();

    @Override
    public abstract Vector2d next();

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Illegal Remove from iterator");
    }

}
