package wordcloud.spiral;

import org.junit.Test;
import wordcloud.collide.Vector2d;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by kenny on 8/1/14.
 */
public class TestRectangleSpiralFunction {

    @Test(expected = UnsupportedOperationException.class)
    public void simpleTest() {
        final Vector2d start = new Vector2d(0, 0);
        final int maxRadius = 2;
        final SpiralFunction spiralFunction = new RectangleSpiralFunction(start, maxRadius);

        assertTrue(spiralFunction.hasNext());
        assertNext(spiralFunction.next(), 0, 0, start);

        assertNext(spiralFunction.next(), 0, -1, start);
        assertNext(spiralFunction.next(), 1, -1, start);
        assertNext(spiralFunction.next(), 1, 0, start);
        assertNext(spiralFunction.next(), 1, 1, start);
        assertNext(spiralFunction.next(), 0, 1, start);
        assertNext(spiralFunction.next(), -1, 1, start);
        assertNext(spiralFunction.next(), -1, 0, start);
        assertNext(spiralFunction.next(), -1, -1, start);

        assertNext(spiralFunction.next(), -1, -2, start);
        assertNext(spiralFunction.next(), 0, -2, start);
        assertNext(spiralFunction.next(), 1, -2, start);
        assertNext(spiralFunction.next(), 2, -2, start);
        assertNext(spiralFunction.next(), 2, -1, start);
        assertNext(spiralFunction.next(), 2, 0, start);
        assertNext(spiralFunction.next(), 2, 1, start);
        assertNext(spiralFunction.next(), 2, 2, start);
        assertNext(spiralFunction.next(), 1, 2, start);
        assertNext(spiralFunction.next(), 0, 2, start);
        assertNext(spiralFunction.next(), -1, 2, start);
        assertNext(spiralFunction.next(), -2, 2, start);
        assertNext(spiralFunction.next(), -2, 1, start);
        assertNext(spiralFunction.next(), -2, 0, start);
        assertNext(spiralFunction.next(), -2, -1, start);
        assertNext(spiralFunction.next(), -2, -2, start);

        assertFalse(spiralFunction.hasNext());

        // throw exception
        spiralFunction.next();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void offsetTest() {
        final Vector2d start = new Vector2d(10, 10);
        final int maxRadius = 2;
        final SpiralFunction spiralFunction = new RectangleSpiralFunction(start, maxRadius);

        assertTrue(spiralFunction.hasNext());
        assertNext(spiralFunction.next(), 0, 0, start);

        assertNext(spiralFunction.next(), 0, -1, start);
        assertNext(spiralFunction.next(), 1, -1, start);
        assertNext(spiralFunction.next(), 1, 0, start);
        assertNext(spiralFunction.next(), 1, 1, start);
        assertNext(spiralFunction.next(), 0, 1, start);
        assertNext(spiralFunction.next(), -1, 1, start);
        assertNext(spiralFunction.next(), -1, 0, start);
        assertNext(spiralFunction.next(), -1, -1, start);

        assertNext(spiralFunction.next(), -1, -2, start);
        assertNext(spiralFunction.next(), 0, -2, start);
        assertNext(spiralFunction.next(), 1, -2, start);
        assertNext(spiralFunction.next(), 2, -2, start);
        assertNext(spiralFunction.next(), 2, -1, start);
        assertNext(spiralFunction.next(), 2, 0, start);
        assertNext(spiralFunction.next(), 2, 1, start);
        assertNext(spiralFunction.next(), 2, 2, start);
        assertNext(spiralFunction.next(), 1, 2, start);
        assertNext(spiralFunction.next(), 0, 2, start);
        assertNext(spiralFunction.next(), -1, 2, start);
        assertNext(spiralFunction.next(), -2, 2, start);
        assertNext(spiralFunction.next(), -2, 1, start);
        assertNext(spiralFunction.next(), -2, 0, start);
        assertNext(spiralFunction.next(), -2, -1, start);
        assertNext(spiralFunction.next(), -2, -2, start);

        assertFalse(spiralFunction.hasNext());

        // throw exception
        spiralFunction.next();
    }

    private void assertNext(Vector2d next, int x, int y, Vector2d start) {
        assertEquals(x + start.getX(), next.getX());
        assertEquals(y + start.getY(), next.getY());
    }

}
