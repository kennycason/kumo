package wordcloud.padding;

import wordcloud.Word;
import wordcloud.collide.Vector2d;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by kenny on 7/1/14.
 * Add pixel padding around the numbers
 */
public class WordPixelPadder implements Padder {

    private RectanglePadder rectanglePadder = new RectanglePadder();

    public void pad(final Word word, final int padding, final Color color) {
        if(padding <= 0) { return; }
        rectanglePadder.pad(word, padding, color);

        final BufferedImage bufferedImage = word.getBufferedImage();

        final Set<Vector2d> toPad = new HashSet<>();
        final int width = bufferedImage.getWidth();
        final int height = bufferedImage.getHeight();

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                if(shouldPad(word, x, y, padding, color)) {
                    toPad.add(new Vector2d(x, y));
                }
            }
        }
        for(Vector2d padPoint : toPad) {
            bufferedImage.setRGB(padPoint.getX(), padPoint.getY(), color.getRGB());
        }
        word.setBufferedImage(bufferedImage);
    }

    private boolean shouldPad(final Word word, final int cx, final int cy, final int padding, final Color color) {
        final BufferedImage bufferedImage = word.getBufferedImage();

        if(!isTransparent(bufferedImage, cx, cy)) { return false; }

        for(int y = cy - padding; y <= cy + padding; y++) {
            for(int x = cx - padding; x <= cx + padding; x++) {
                if(x == cx && y == cy) { continue; }
                if(inBounds(bufferedImage, x, y)) {
                    if(!isTransparent(bufferedImage, x, y)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean inBounds(final BufferedImage bufferedImage, final int x, final int y) {
        return x >= 0
                && y >= 0
                && x < bufferedImage.getWidth()
                && y < bufferedImage.getHeight();
    }

    private boolean isTransparent(final BufferedImage bufferedImage, final int x, final int y) {
        int pixel = bufferedImage.getRGB(x, y);
        if((pixel & 0xFF000000) == 0x00000000) {
            return true;
        }
        return false;
    }

}
