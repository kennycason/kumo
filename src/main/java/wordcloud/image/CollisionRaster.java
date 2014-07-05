package wordcloud.image;

import java.awt.image.BufferedImage;

/**
 * Created by kenny on 7/4/14.
 */
public class CollisionRaster {

    private final int[][] data;

    private final int width;

    private final int height;

    public CollisionRaster(final BufferedImage bufferedImage) {
        this.width = bufferedImage.getWidth();
        this.height = bufferedImage.getHeight();
        data = new int[width][height];
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                data[x][y] = bufferedImage.getRGB(x, y);
            }
        }
    }

    public CollisionRaster(int width, int height) {
        this.width = width;
        this.height = height;
        data = new int[width][height];
    }

    public int getRGB(int x, int y) {
        return data[x][y];
    }

    public void setRGB(int x, int y, int rgb) {
        data[x][y] = rgb;
    }

    public void mask(final CollisionRaster collisionRaster, int x, int y) {
        final int maxHeight = Math.min(y + collisionRaster.getHeight(), height);
        final int maxWidth = Math.min(x + collisionRaster.getWidth(), width);
        for(int offY = y, offY2 = 0; offY < maxHeight; offY++, offY2++) {
            for(int offX = x, offX2 = 0; offX < maxWidth; offX++, offX2++) {
                if(!collisionRaster.isTransparent(offX2, offY2)) {
                    data[offX][offY] = collisionRaster.getRGB(offX2, offY2);
                }
            }
        }
    }

    public boolean isTransparent(int x, int y) {
        return (data[x][y] & 0xFF000000) == 0x00000000;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
