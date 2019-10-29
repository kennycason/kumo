package com.kennycason.kumo.draw.awtimpl;

import com.kennycason.kumo.draw.IImage;
import com.kennycason.kumo.draw.IImageWriter;
import com.kennycason.kumo.draw.VaryingImpl;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;

public class ImageWriterImpl implements IImageWriter {

    @Override
    public void write(IImage image, String format, OutputStream outputStream) throws IOException {
        ImageIO.write((RenderedImage) image.getActual(), format, outputStream);
    }
}
