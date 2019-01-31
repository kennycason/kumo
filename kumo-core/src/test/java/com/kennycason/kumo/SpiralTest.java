package com.kennycason.kumo;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author joerg1985
 */
public class SpiralTest {

    @Test
    public void NewImplementationVsOldImplementation() throws IOException {
        // draw the spiral as image?
        // red pixels -> only returned by the old implementation
        // blue pixels -> only returned by the new implementation
        // pink pixels -> returned by the old and the new implementation
        boolean debug = false;
        
        final int white = 0;
        final int red = 0xFFFF0000;
        final int blue = 0xFF0000FF;
        final int pink = red | blue;

        // we seed to get the same numbers on each run
        Random random = new Random(42);

        for (int i = 0; i < 20; i++) {
            Dimension dimension = new Dimension(
                    100 + random.nextInt(900),
                    100 + random.nextInt(900)
            );

            for (int j = 0; j < 20; j++) {
                Point start = new Point(
                        random.nextInt(dimension.width),
                        random.nextInt(dimension.height)
                );

                // the old implementation did not repect images higher than wide
                int originalRadius = dimension.width;
                int optimizedRadius = WordCloud.computeRadius(dimension, start);

                List<Point> original = spiral(dimension, start, originalRadius);
                List<Point> optimized = spiral(dimension, start, optimizedRadius);

                BufferedImage img = new BufferedImage(
                        dimension.width, dimension.height, BufferedImage.TYPE_4BYTE_ABGR
                );

                original.forEach((p) -> img.setRGB(p.x, p.y, red));
                optimized.forEach((p) -> {
                    if (img.getRGB(p.x, p.y) != 0) {
                        img.setRGB(p.x, p.y, pink);
                    } else {
                        img.setRGB(p.x, p.y, blue);
                    }
                });

                boolean next = false;
                
                for (int y = 0; !next && y < dimension.height; y++) {
                    for (int x = 0; !next && x < dimension.width; x++) {
                        int rgb = img.getRGB(x, y);

                        if (rgb == red) {
                            ImageIO.write(img, "png", new File("output\\failed_spiral_test.png"));
                            Assert.fail();
                        } else if (debug && rgb != white && rgb != pink) {
                            ImageIO.write(img, "png", new File("output\\debug_spiral_test_" + System.currentTimeMillis() + ".png"));
                            next = true;
                        } 
                    }
                }
            }
        }
    }

    private List<Point> spiral(Dimension dimension, Point start, final int maxRadius) {
        List<Point> points = new ArrayList<>();

        for (int r = 0; r < maxRadius; r += 2) {
            for (int x = -r; x <= r; x++) {
                if (start.x + x < 0) {
                    continue;
                }
                if (start.x + x >= dimension.width) {
                    continue;
                }

                // try positive root
                final int y1 = (int) Math.sqrt(r * r - x * x);
                if (start.y + y1 >= 0 && start.y + y1 < dimension.height) {
                    points.add(new Point(start.x + x, start.y + y1));
                }
                // try negative root
                final int y2 = -y1;
                if (start.y + y2 >= 0 && start.y + y2 < dimension.height) {
                    points.add(new Point(start.x + x, start.y + y2));
                }
            }
        }

        return points;
    }
}
