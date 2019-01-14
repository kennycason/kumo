package com.kennycason.kumo;

import com.kennycason.kumo.collide.checkers.CollisionChecker;
import com.kennycason.kumo.collide.Collidable;
import com.kennycason.kumo.image.CollisionRaster;

import java.awt.*;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Created by kenny on 6/29/14.
 */
public class Word implements Collidable {

    private final CollisionChecker collisionChecker;

    private final String word;

    private final Color color;

    private final Point position = new Point(0, 0);

    private BufferedImage bufferedImage;

    private CollisionRaster collisionRaster;

    public Word(final String word,
            final Color color,
            final FontMetrics fontMetrics,
            final CollisionChecker collisionChecker,
            final double theta) {
        this.word = word;
        this.color = color;
        this.collisionChecker = collisionChecker;
        this.bufferedImage = render(fontMetrics, word, theta, color);

        this.collisionRaster = new CollisionRaster(this.bufferedImage);
    }

    static AtomicLong n = new AtomicLong();

    private BufferedImage render(final FontMetrics fontMetrics, final String text, double theta, final Color color1) {
        // get the height of a line of text in this font and render context
        final int maxDescent = fontMetrics.getMaxDescent();
        // get the advance of my text in this font and render context
        //TODO: stringWidth is not allways correct e.g. 
        /*
        for (int i = 0; i <= 360; i += 5) {
            Word word = new Word("test test IIII", Color.red, new BufferedImage(
                    10, 10, BufferedImage.TYPE_4BYTE_ABGR
            ).createGraphics().getFontMetrics(
                    new Font("Arial", 0, 40)
            ), null, Math.PI * i / 180.0);

            try {
                File file = new File("output\\w" + i + ".png");
                ImageIO.write(word.getBufferedImage(), "png", file);
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(Word.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        */
        final int width = fontMetrics.stringWidth(text);

        // add 2 pixels space for antialiasing
        final int height = fontMetrics.getHeight() + 2;

        final double sin = Math.abs(Math.sin(theta));
        final double cos = Math.abs(Math.cos(theta));

        final int rotatedWidth = (int) Math.floor(width * cos + height * sin);
        final int rotatedHeight = (int) Math.floor(height * cos + width * sin);

        BufferedImage image = new BufferedImage(
                rotatedWidth, rotatedHeight, BufferedImage.TYPE_INT_ARGB
        );
        final Graphics2D graphics = (Graphics2D) image.getGraphics();
        final int centerX = rotatedWidth / 2;
        final int centerY = rotatedHeight / 2;

        // we rotate the word around the center of the image
        graphics.rotate(theta, centerX, centerY);

        graphics.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB
        );
        
        graphics.setColor(color1);
        graphics.setFont(fontMetrics.getFont());
        graphics.drawString(
                text, 
                centerX - width / 2, 
                centerY + (fontMetrics.getHeight() - maxDescent) / 2
        );

        return image;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public void setBufferedImage(final BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
        this.collisionRaster = new CollisionRaster(bufferedImage);
    }

    public String getWord() {
        return word;
    }

    public Point getPosition() {
        return position;
    }

    public Dimension getDimension() {
        return collisionRaster.getDimension();
    }

    @Override
    public CollisionRaster getCollisionRaster() {
        return collisionRaster;
    }

    @Override
    public boolean collide(final Collidable collidable) {
        return collisionChecker.collide(this, collidable);
    }

    public void draw(final CollisionRaster collisionRaster) {
        collisionRaster.mask(collisionRaster, position);
    }

    @Override
    public String toString() {
        return "Word{"
                + "word='" + word + '\''
                + ", color=" + color
                + ", x=" + position.x
                + ", y=" + position.y
                + ", width=" + bufferedImage.getWidth()
                + ", height=" + bufferedImage.getHeight()
                + '}';
    }

}
