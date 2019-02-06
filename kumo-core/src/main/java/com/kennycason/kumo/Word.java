package com.kennycason.kumo;

import com.kennycason.kumo.collide.checkers.CollisionChecker;
import com.kennycason.kumo.collide.Collidable;
import com.kennycason.kumo.image.CollisionRaster;
import com.kennycason.kumo.image.ImageRotation;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

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
    private final double theta;

    public Word(final String word,
            final Color color,
            final FontMetrics fontMetrics,
            final CollisionChecker collisionChecker,
            final double theta) {
        this.word = word;
        this.color = color;
        this.collisionChecker = collisionChecker;
        this.theta = theta;
        this.bufferedImage = render(word, color, fontMetrics, theta);

        this.collisionRaster = new CollisionRaster(this.bufferedImage);
    }

    private BufferedImage render(final String text, final Color fontColor, final FontMetrics fontMetrics, double theta) {
        // get the advance of my text in this font and render context
        final int width = fontMetrics.stringWidth(text);
        // get the height of a line of text in this font and render context
        final int height = fontMetrics.getHeight();

        BufferedImage rendered = new BufferedImage(
                width, height, BufferedImage.TYPE_INT_ARGB
        );
        
        final Graphics2D gOfRendered = (Graphics2D) rendered.getGraphics();
        
        // set the rendering hint here to ensure the font metrics are correct
        gOfRendered.setRenderingHints(getRenderingHints());
        gOfRendered.setColor(fontColor);
        gOfRendered.setFont(fontMetrics.getFont());
        
        gOfRendered.drawString(
                text, 0, height - fontMetrics.getMaxDescent() - fontMetrics.getLeading()
        );
        
        return ImageRotation.rotate(rendered, theta);
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

    public double getTheta() {
        return theta;
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
    
    public static RenderingHints getRenderingHints() {
        Map<RenderingHints.Key, Object> hints = new HashMap<>();
        hints.put(
                RenderingHints.KEY_ALPHA_INTERPOLATION,
                RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY
        );
        hints.put(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        hints.put(
                RenderingHints.KEY_COLOR_RENDERING,
                RenderingHints.VALUE_COLOR_RENDER_QUALITY
        );
        hints.put(
                RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON
        );
        hints.put(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC
        );
        hints.put(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB
        );
        
        return new RenderingHints(hints);
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
