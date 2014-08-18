package wordcloud;

import wordcloud.collide.Collidable;
import wordcloud.collide.checkers.CollisionChecker;
import wordcloud.collide.Vector2d;
import wordcloud.image.CollisionRaster;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by kenny on 6/29/14.
 */
public class Word implements Collidable {

    private final CollisionChecker collisionChecker;

    private final String word;

    private final Color color;

    private Vector2d position = new Vector2d(0, 0);

    private BufferedImage bufferedImage;

    private CollisionRaster collisionRaster;

    public Word(String word, Color color, FontMetrics fontMetrics, CollisionChecker collisionChecker) {
        this.word = word;
        this.color = color;
        this.collisionChecker = collisionChecker;
        // get the height of a line of text in this font and render context
        final int maxDescent = fontMetrics.getMaxDescent();
        final int maxAscent = fontMetrics.getMaxAscent();
        // get the advance of my text in this font and render context
        final int width = fontMetrics.stringWidth(word);
        fontMetrics.getHeight();

        this.bufferedImage = new BufferedImage(width, maxAscent, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D graphics = (Graphics2D) this.bufferedImage.getGraphics();
        graphics.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        graphics.setColor(color);
        graphics.setFont(fontMetrics.getFont());
        graphics.drawString(word, 0, maxAscent - maxDescent);

        this.collisionRaster = new CollisionRaster(this.bufferedImage);
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
        this.collisionRaster = new CollisionRaster(bufferedImage);
    }

    public String getWord() {
        return word;
    }

    public Vector2d getPosition() {
        return position;
    }

    public int getX() {
        return position.getX();
    }

    public int getY() {
        return position.getY();
    }

    public void setXY(Vector2d position) {
        this.position = position;
    }

    public int getWidth() {
        return bufferedImage.getWidth();
    }

    public int getHeight() {
        return bufferedImage.getHeight();
    }

    @Override
    public CollisionRaster getCollisionRaster() {
        return collisionRaster;
    }

    @Override
    public boolean collide(Collidable collidable) {
        return collisionChecker.collide(this, collidable);
    }

    public void draw(CollisionRaster collisionRaster) {
        collisionRaster.mask(collisionRaster, position.getX(), position.getY());
    }

    @Override
    public String toString() {
        return "WordRectangle{" +
                "word='" + word + '\'' +
                ", color=" + color +
                ", x=" + getX() +
                ", y=" + getY() +
                ", width=" + bufferedImage.getWidth() +
                ", height=" + bufferedImage.getHeight() +
                '}';
    }

}
