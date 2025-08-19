package com.kennycason.kumo.bg;

import com.kennycason.kumo.collide.Collidable;
import com.kennycason.kumo.collide.RectanglePixelCollidable;
import com.kennycason.kumo.image.CollisionRaster;
import sun.font.FontDesignMetrics;
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
public class PixelBoundryBackground implements Background {

    private final CollisionRaster collisionRaster;
    
    private final Point position = new Point(0, 0);

    /**
     * Creates a PixelBoundaryBackground using an InputStream to load an image
     *
     * @param imageInputStream InputStream containing an image file
     * @throws IOException when fails to open file stream
     */

    public PixelBoundryBackground(final InputStream imageInputStream) throws IOException {
        final BufferedImage bufferedImage = ImageIO.read(imageInputStream);
        /**
         *  preprocess the image if the image is not
         */
        //CS304 Issue link: https://github.com/kennycason/kumo/issues/81
        BufferedImage b = preprocess(bufferedImage,5);
        // changed
        this.collisionRaster = new CollisionRaster(b);
    }

    /**
     *
     * @param srcImage srcImage will be preprocessed , the method will set the background to be transparent
     * @param alpha  alpha is a transparency
     * @return we will return a bufferedimage object which has been preprocessed
     */
    //CS304 Issue link: https://github.com/kennycason/kumo/issues/81
    private BufferedImage preprocess(BufferedImage srcImage,int alpha) {
        int imgHeight = srcImage.getHeight();//取得图片的长和宽
        int imgWidth = srcImage.getWidth();
        int c = srcImage.getRGB(3, 3);
        //防止越位
        if (alpha < 0) {
            alpha = 0;
        } else if (alpha > 10) {
            alpha = 10;
        }
        BufferedImage bi = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_4BYTE_ABGR);//Create a new type that supports transparent BufferedImage
        for(int i = 0; i < imgWidth; ++i)//Copy the contents of the original image to the new image, and set the background to transparent
        {
            for(int j = 0; j < imgHeight; ++j)
            {
                //Set the background to transparent
                if(srcImage.getRGB(i, j) == c){
                    bi.setRGB(i, j, c & 0x00ffffff);
                }
                //Set transparency
                else{
                    int rgb = bi.getRGB(i, j);
                    rgb = ((alpha * 255 / 10) << 24) | (rgb & 0x00ffffff);
                    bi.setRGB(i, j, rgb);
                }
            }
        }
        return bi;
    }

    /**
     * Creates a PixelBoundaryBackground using an image from the input file
     *
     * @param file a File pointing to an image
     * @throws IOException when fails to open file stream
     */
    public PixelBoundryBackground(final File file) throws IOException {
        this(new FileInputStream(file));
    }

    /**
     * Creates a PixelBoundaryBackground using an image-path
     *
     * @param filepath path to an image file
     * @throws IOException when fails to open file stream
     */
    public PixelBoundryBackground(final String filepath) throws IOException {
        this(new File(filepath));
    }

    /**
     *Creates a PixelBoundaryBackground using an image-path
     *
     * @param example a string which you want to use to be a background
     * @param size the size of each character in the string
     */
    //CS304 Issue link: https://github.com/kennycason/kumo/issues/70
    public PixelBoundryBackground(String example , int size){
        Font font = new Font("微软雅黑", Font.BOLD, size);
        FontDesignMetrics metrics = FontDesignMetrics.getMetrics(font);
        int width = getWordWidth(font, example);
        int height = metrics.getHeight();
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        //Set the back to white
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
        graphics.setFont(font);
        graphics.setColor(Color.BLACK);
        graphics.drawString(example, 0, metrics.getAscent());//图片上写文字
        graphics.dispose();
        BufferedImage b = preprocess(bufferedImage,5);
        // changed
        this.collisionRaster = new CollisionRaster(b);
      //  this.collisionRaster = new CollisionRaster(bufferedImage);
      //  write(bufferedImage, "D:\\Gin_app\\IDEA\\kumocore\\test.jpg");
    }

    /**
     *
     * @param font the font of each character
     * @param content the content of the input string
     * @return return a width of a word
     */
    //CS304 Issue link: https://github.com/kennycason/kumo/issues/70
    private static int getWordWidth(Font font, String content) {
        FontDesignMetrics metrics = FontDesignMetrics.getMetrics(font);
        int width = 0;
        for (int i = 0; i < content.length(); i++) {
            width += metrics.charWidth(content.charAt(i));
        }
        return width;
    }
    @Override
    public void mask(RectanglePixelCollidable background) {
        Dimension dimensionOfShape = collisionRaster.getDimension();
        Dimension dimensionOfBackground = background.getDimension();
        
        int minY = Math.max(position.y, 0);
        int minX = Math.max(position.x, 0);

        int maxY = dimensionOfShape.height - position.y - 1;
        int maxX = dimensionOfShape.width - position.x - 1;

        CollisionRaster rasterOfBackground = background.getCollisionRaster();

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
