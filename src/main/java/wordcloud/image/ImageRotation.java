package wordcloud.image;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by kenny on 6/29/14.
 */
public class ImageRotation {

    private ImageRotation() {}

    public static BufferedImage rotate90(final BufferedImage bufferedImage) {
        return rotate(bufferedImage, Math.PI / 2);
    }

    public static BufferedImage rotateMinus90(final BufferedImage bufferedImage) {
        return rotate(bufferedImage, -Math.PI / 2);
    }

    public static BufferedImage rotate(BufferedImage image, double theta) {
        final double sin = Math.abs(Math.sin(theta)), cos = Math.abs(Math.cos(theta));
        final int w = image.getWidth();
        final int h = image.getHeight();
        final int neww = (int) Math.floor(w * cos + h * sin);
        final int newh = (int) Math.floor(h * cos + w * sin);
        BufferedImage result = new BufferedImage(neww, newh, image.getType());
        Graphics2D g = result.createGraphics();
        g.translate((neww - w) / 2, (newh - h) / 2);
        g.rotate(theta, w / 2, h / 2);
        g.drawRenderedImage(image, null);
        g.dispose();

        return result;
    }

}
