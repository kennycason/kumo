package com.kennycason.kumo.draw.androidimpl;

import android.graphics.Bitmap;
import com.kennycason.kumo.draw.IImage;
import com.kennycason.kumo.draw.IImageWriter;
import com.kennycason.kumo.draw.VaryingImpl;

import java.io.OutputStream;


public class ImageWriterImpl implements IImageWriter {

    @Override
    public void write(IImage image, String format, OutputStream outputStream) {
        Bitmap.CompressFormat imgFormat;
        if(format.contains("jpeg")){
            imgFormat = Bitmap.CompressFormat.JPEG;
        }else if(format.contains("webp")){
            imgFormat = Bitmap.CompressFormat.WEBP;
        }else{
            imgFormat = Bitmap.CompressFormat.PNG;
        }

        ((Bitmap) image.getActual()).compress(imgFormat, 100, outputStream);
    }
}
