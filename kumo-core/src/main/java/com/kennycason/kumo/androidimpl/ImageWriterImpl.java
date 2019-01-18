package com.kennycason.kumo.androidimpl;

import android.graphics.Bitmap;
import com.kennycason.kumo.abst.ImageAbst;
import com.kennycason.kumo.abst.ImageWriterAbst;

import java.io.OutputStream;


public class ImageWriterImpl extends ImageWriterAbst {

    @Override
    public void write(ImageAbst image, String format, OutputStream outputStream) {
        Bitmap.CompressFormat imgFormat;
        if(format.contains("jpeg")){
            imgFormat = Bitmap.CompressFormat.JPEG;
        }else if(format.contains("webp")){
            imgFormat = Bitmap.CompressFormat.WEBP;
        }else{
            imgFormat = Bitmap.CompressFormat.PNG;
        }

        ((ImageImpl)image).getActual().compress(imgFormat, 100, outputStream);
    }
}
