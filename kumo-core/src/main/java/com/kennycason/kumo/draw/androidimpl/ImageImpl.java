package com.kennycason.kumo.draw.androidimpl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.kennycason.kumo.draw.IImage;

import java.io.InputStream;

public class ImageImpl implements IImage<Bitmap> {

    private Bitmap bitmap;

    public ImageImpl(int width, int height) {
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    }

    public ImageImpl(InputStream fromInputStream){
        bitmap = BitmapFactory.decodeStream(fromInputStream);
    }

    public ImageImpl(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    @Override
    public int getColor(int x, int y) {
        return bitmap.getPixel(x, y);
    }

    @Override
    public int getWidth() {
        return bitmap.getWidth();
    }

    @Override
    public int getHeight() {
        return bitmap.getHeight();
    }

    @Override
    public Bitmap getActual() {
        return bitmap;
    }
}
