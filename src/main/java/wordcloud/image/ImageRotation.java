package wordcloud.image;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Created by kenny on 6/29/14.
 */
public class ImageRotation {

    public static BufferedImage rotate90(final BufferedImage bufferedImage) {
        return rotate(bufferedImage, Math.PI / 2);
    }

    public static BufferedImage rotateMinus90(final BufferedImage bufferedImage) {
        return rotate(bufferedImage, -Math.PI / 2);
    }

    public static BufferedImage rotate(final BufferedImage bufferedImage, double theta) {
        final int width = bufferedImage.getWidth();
        final int height = bufferedImage.getHeight();
        BufferedImage rotatedImage = new BufferedImage(height, width, BufferedImage.TYPE_INT_ARGB);
        AffineTransform xform = new AffineTransform();
        xform.translate(0.5 * height, 0.5 * width);
        xform.rotate(theta);
        xform.translate(-0.5 * width, -0.5 * height);

        Graphics2D graphics2D = (Graphics2D) rotatedImage.getGraphics();
        graphics2D.drawImage(bufferedImage, xform, null);
        return rotatedImage;
    }

}
