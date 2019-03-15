package com.kennycason.kumo;

import com.kennycason.kumo.collide.Collidable;
import com.kennycason.kumo.collide.checkers.CollisionChecker;
import com.kennycason.kumo.draw.*;
import com.kennycason.kumo.image.CollisionRaster;

/**
 * Created by kenny on 6/29/14.
 */
public class Word implements Collidable {

    private final CollisionChecker collisionChecker;

    private final String word;

    private final Color color;

    private final Point position = new Point(0, 0);

    private Image bufferedImage;

    private CollisionRaster collisionRaster;

    public Word(final String word,
                final Color color,
                final FontMetrics fontMetrics,
                final CollisionChecker collisionChecker) {
        this.word = word;
        this.color = color;
        this.collisionChecker = collisionChecker;
        // get the height of a line of text in this font and render context
        final int maxDescent = fontMetrics.getBottom();
        // get the advance of my text in this font and render context
        final int width = fontMetrics.measure(word);

        this.bufferedImage = new Image(width, fontMetrics.getTop());
        final Graphics graphics = new Graphics(bufferedImage);
        graphics.enableAntiAliasing();
        graphics.setFont(fontMetrics.getFont());

        graphics.drawString(word, 0, fontMetrics.getTop() - maxDescent, color);

        this.collisionRaster = new CollisionRaster(this.bufferedImage);
    }

    public Image getBufferedImage() {
        return bufferedImage;
    }

    public void setImage(final Image bufferedImage) {
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
                ", x=" + position.getX() +
                ", y=" + position.getY() +
                ", width=" + bufferedImage.getWidth() +
                ", height=" + bufferedImage.getHeight() +
                '}';
    }

}
