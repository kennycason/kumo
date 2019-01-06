package com.kennycason.kumo.image;

import com.kennycason.kumo.interfaces.GraphicsAbst;
import com.kennycason.kumo.interfaces.ImageAbst;
import com.kennycason.kumo.interfaces.InstanceCreator;

/**
 * Created by kenny on 6/29/14.
 */
public class ImageRotation {

    private ImageRotation() {}

    public static ImageAbst rotate90(final ImageAbst bufferedImage) {
        return rotate(bufferedImage, Math.PI / 2);
    }

    public static ImageAbst rotateMinus90(final ImageAbst bufferedImage) {
        return rotate(bufferedImage, -Math.PI / 2);
    }

    public static ImageAbst rotate(final ImageAbst image, final double theta) {
        if (theta == 0.0) { return image; }

        final double sin = Math.abs(Math.sin(theta));
        final double cos = Math.abs(Math.cos(theta));
        final int weight = image.getWidth();
        final int height = image.getHeight();
        final int newWeight = (int) Math.floor(weight * cos + height * sin);
        final int newHeight = (int) Math.floor(height * cos + weight * sin);

        final ImageAbst result = InstanceCreator.image(newWeight, newHeight);
        final GraphicsAbst graphics = InstanceCreator.graphics(result);
        graphics.translate((newWeight - weight) / 2, (newHeight - height) / 2);
        graphics.rotate(theta, weight / 2, height / 2);
        graphics.drawAndFinish(image);

        return result;
    }

}
