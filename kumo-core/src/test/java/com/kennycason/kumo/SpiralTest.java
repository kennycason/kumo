package com.kennycason.kumo;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javax.imageio.ImageIO;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author joerg1985
 */
public class SpiralTest {

    @Test
    public void MaxRadius() throws IOException {
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

                List<Point> original = originalSpiral(
                        dimension, start, dimension.width
                );
                List<Point> optimized = originalSpiral(
                        dimension, start, WordCloud.computeRadius(dimension, start)
                );

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
    
    @Test
    public void NoIdenticalPoints() throws IOException {
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

                List<Point> points = optimizedSpiral(dimension, start);
                Set<Point> unique = new HashSet<>(points);
                
                Assert.assertEquals(
                        "no duplicate points", 
                        points.size(), unique.size()
                );
            }
        }
    }

    private List<Point> originalSpiral(Dimension dimension, Point start, int maxRadius) {
        List<Point> points = new ArrayList<>();
        Point position = new Point();

        for (int r = 0; r < maxRadius; r += 2) {
            for (int x = -r; x <= r; x++) {
                if (start.x + x < 0) {
                    continue;
                }
                if (start.x + x >= dimension.width) {
                    continue;
                }
                
                position.x = start.x + x; 

                // try positive root
                final int y1 = (int) Math.sqrt(r * r - x * x);
                if (start.y + y1 >= 0 && start.y + y1 < dimension.height) {
                    position.y = start.y + y1; 
                    points.add(new Point(position));
                }
                // try negative root
                final int y2 = -y1;
                if (start.y + y2 >= 0 && start.y + y2 < dimension.height) {
                    position.y = start.y + y2; 
                    points.add(new Point(position));
                }
            }
        }

        return points;
    }
    
    private List<Point> optimizedSpiral(Dimension dimension, Point start) {
        final int maxRadius = WordCloud.computeRadius(dimension, start);
        
        List<Point> points = new ArrayList<>();
        Point position = new Point();

        for (int r = 0; r < maxRadius; r += 2) {
            for (int x = Math.max(-start.x, -r); x <= Math.min(r, dimension.width - start.x - 1); x++) {
                position.x = start.x + x;

                final int offset = (int) Math.sqrt(r * r - x * x);
                
                // try positive root
                position.y = start.y + offset;
                if (position.y >= 0 && position.y < dimension.height) {
                    points.add(new Point(position));
                }
                
                // try negative root (if offset != 0)
                position.y = start.y - offset;
                if (offset != 0 && position.y >= 0 && position.y < dimension.height) {
                    points.add(new Point(position));
                }
            }
        }

        return points;
    }
}
