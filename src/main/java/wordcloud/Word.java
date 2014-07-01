package wordcloud;

import wordcloud.collide.Collidable;
import wordcloud.collide.CollisionChecker;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by kenny on 6/29/14.
 */
public class Word implements Collidable {

    private final CollisionChecker collisionChecker;

    private final String word;

    private final Color color;

    private int x = 0;

    private int y = 0;

    private BufferedImage bufferedImage;

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
        final Graphics graphics = this.bufferedImage.getGraphics();
        graphics.setColor(color);
        graphics.setFont(fontMetrics.getFont());
        graphics.drawString(word, 0, maxAscent - maxDescent);
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    public String getWord() {
        return word;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return bufferedImage.getWidth();
    }

    public int getHeight() {
        return bufferedImage.getHeight();
    }

    @Override
    public boolean collide(Collidable collidable) {
        return collisionChecker.collide(this, collidable);
    }

    public void draw(Graphics graphics) {
        graphics.setColor(color);
        graphics.drawImage(bufferedImage, x, y, null);
    }

    @Override
    public String toString() {
        return "WordRectangle{" +
                "word='" + word + '\'' +
                ", color=" + color +
                ", x=" + x +
                ", y=" + y +
                ", width=" + bufferedImage.getWidth() +
                ", height=" + bufferedImage.getHeight() +
                '}';
    }

}
