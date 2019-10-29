package com.kennycason.kumo.draw.androidimpl;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import com.kennycason.kumo.draw.IImage;
import com.kennycason.kumo.draw.IImageRotator;

public class ImageRotatorImpl implements IImageRotator {

    @Override
    public IImage rotate(IImage image, double theta) {
        if (theta == 0.0) { return image; }

        final double sin = Math.abs(Math.sin(theta));
        final double cos = Math.abs(Math.cos(theta));
        final int weight = image.getWidth();
        final int height = image.getHeight();
        final int newWeight = (int) Math.floor(weight * cos + height * sin);
        final int newHeight = (int) Math.floor(height * cos + weight * sin);

        Bitmap original = (Bitmap) image.getActual();

        Matrix matrix = new Matrix();
        matrix.postTranslate((newWeight - weight) / 2F, (newHeight - height) / 2F);
        matrix.postRotate((float) theta, weight / 2F, height / 2F);
        Bitmap rotated = Bitmap.createBitmap(
                original,
                0,
                0,
                weight,
                height,
                matrix,
                true);

        return new ImageImpl(rotated);
    }

}
