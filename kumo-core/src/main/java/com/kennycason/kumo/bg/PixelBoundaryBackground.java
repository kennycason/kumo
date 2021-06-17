package com.kennycason.kumo.bg;

import com.kennycason.kumo.collide.RectanglePixelCollidable;
import com.kennycason.kumo.image.CollisionRaster;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Class creates a Background Mode based on the transparent Pixel-boundaries of a loaded image
 * @author kenny
 * @version 2014.06.30
 */
public class PixelBoundaryBackground implements Background {

    private final CollisionRaster collisionRaster;
    
    private final Point position = new Point(0, 0);

    /**
     * Creates a PixelBoundaryBackground using an InputStream to load an image
     *
     * @param imageInputStream InputStream containing an image file
     * @throws IOException when fails to open file stream
     */
    public PixelBoundaryBackground(final InputStream imageInputStream) throws IOException {
        final BufferedImage bufferedImage = ImageIO.read(imageInputStream);
        this.collisionRaster = new CollisionRaster(bufferedImage);
    }
    /**
     * Creates a PixelBoundaryBackground using an InputStream to load an image
     *
     * @param imageInputStream InputStream containing an image file
     * @param alpha The transparent index, should range from 0 to 10
     * @throws IOException when fails to open file stream
     */
    public PixelBoundaryBackground(final InputStream imageInputStream, int alpha) throws Exception {
        final BufferedImage bufferedImage = ImageIO.read(imageInputStream);
        BufferedImage tansBackgroundImage = TransBackground(bufferedImage,alpha);
        this.collisionRaster = new CollisionRaster(tansBackgroundImage);
    }

    /**
     * Creates a PixelBoundaryBackground using an image from the input file
     *
     * @param file a File pointing to an image
     * @throws IOException when fails to open file stream
     */
    public PixelBoundaryBackground(final File file) throws IOException {
        this(new FileInputStream(file));
    }

    /**
     * Creates a PixelBoundaryBackground using an image-path
     *
     * @param filepath path to an image file
     * @throws IOException when fails to open file stream
     */
    public PixelBoundaryBackground(final String filepath) throws IOException {
        this(new File(filepath));
    }

    private BufferedImage TransBackground(BufferedImage srcImage, int alpha) throws Exception {
        int imgHeight = srcImage.getHeight();
        int imgWidth = srcImage.getWidth();
        //get the pixel of left upper corner
        int c = srcImage.getRGB(3, 3);
        if (alpha < 0 || alpha > 10) {
            throw new Exception("alpha out of range");
        }
        BufferedImage bufferedImage = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_4BYTE_ABGR);
        for(int i = 0; i < imgWidth; i++){
            for(int j = 0; j < imgHeight; j++) {
                if(srcImage.getRGB(i, j) == c){//for background
                    bufferedImage.setRGB(i, j, c & 0x00ffffff);
                }
                else{//not background
                    int rgb = bufferedImage.getRGB(i, j);
                    rgb = ((alpha * 255 / 10) << 24) | (rgb & 0x00ffffff);
                    bufferedImage.setRGB(i, j, rgb);
                }
            }
        }
        return bufferedImage;
    }
    
    @Override
    public void mask(final RectanglePixelCollidable background) {
        final Dimension dimensionOfShape = collisionRaster.getDimension();
        final Dimension dimensionOfBackground = background.getDimension();
        
        final int minY = Math.max(position.y, 0);
        final int minX = Math.max(position.x, 0);

        final int maxY = dimensionOfShape.height - position.y - 1;
        final int maxX = dimensionOfShape.width - position.x - 1;

        final CollisionRaster rasterOfBackground = background.getCollisionRaster();

        for (int y = 0; y < dimensionOfBackground.height; y++) {
            if (y < minY || y > maxY) {
                for (int x = 0; x < dimensionOfBackground.width; x++) {
                    rasterOfBackground.setPixelIsNotTransparent(position.x + x, position.y + y);
                }
            } else {
                for (int x = 0; x < dimensionOfBackground.width; x++) {
                    if (x < minX || x > maxX || collisionRaster.isTransparent(x, y)) {
                        rasterOfBackground.setPixelIsNotTransparent(position.x + x, position.y + y);
                    }
                }
            }

        }
    }
}
