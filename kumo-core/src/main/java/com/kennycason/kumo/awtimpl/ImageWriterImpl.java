package com.kennycason.kumo.awtimpl;

import com.kennycason.kumo.interfaces.ImageAbst;
import com.kennycason.kumo.interfaces.ImageWriterAbst;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.OutputStream;

public class ImageWriterImpl extends ImageWriterAbst {

    @Override
    public void write(ImageAbst image, String format, OutputStream outputStream) throws IOException {
        ImageIO.write(((ImageImpl)image).getActual(), format, outputStream);
    }
}
