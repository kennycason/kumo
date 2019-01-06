package com.kennycason.kumo;

import com.kennycason.kumo.collide.checkers.CollisionChecker;
import com.kennycason.kumo.collide.Collidable;
import com.kennycason.kumo.image.CollisionRaster;

import java.awt.Color;
import java.awt.Point;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

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
                final CollisionChecker collisionChecker) {
        this.word = word;
        this.color = color;
        this.collisionChecker = collisionChecker;
        // get the height of a line of text in this font and render context
        final int maxDescent = fontMetrics.getMaxDescent();
        // get the advance of my text in this font and render context
        final int width = fontMetrics.stringWidth(word);

        this.bufferedImage = new BufferedImage(width, fontMetrics.getHeight(), BufferedImage.TYPE_INT_ARGB);
        final Graphics2D graphics = (Graphics2D) this.bufferedImage.getGraphics();
        graphics.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        graphics.setColor(color);
        graphics.setFont(fontMetrics.getFont());

        graphics.drawString(word, 0, fontMetrics.getHeight() - maxDescent);

        this.collisionRaster = new CollisionRaster(this.bufferedImage);
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
        return "WordRectangle{" +
                "word='" + word + '\'' +
                ", color=" + color +
                ", x=" + position.x +
                ", y=" + position.y +
                ", width=" + bufferedImage.getWidth() +
                ", height=" + bufferedImage.getHeight() +
                '}';
    }

}
