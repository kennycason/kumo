package wordcloud.image;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by kenny on 7/4/14.
 */
public class TestImageData {

    @Test
    public void basicTests() {
        final int width = 10;
        final int height = 5;
        final CollisionRaster collisionRaster = new CollisionRaster(width, height);

        assertEquals(width, collisionRaster.getWidth());
        assertEquals(height, collisionRaster.getHeight());
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                assertTrue(collisionRaster.isTransparent(x, y));
            }
        }
    }


}
