package com.kennycason.kumo.draw;

import java.io.IOException;
import java.io.OutputStream;

public class ImageWriter implements IImageWriter{

    private IImageWriter platformSpecificImplementation;

    public ImageWriter(){
        String name = "ImageWriterImpl";
        try {

            Class<? extends IImageWriter> clazz = (Class<? extends IImageWriter>) Class.forName(Platform.PACKAGE + name);
            platformSpecificImplementation = clazz.newInstance();

        } catch(ClassNotFoundException | IllegalAccessException | InstantiationException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void write(IImage image, String format, OutputStream outputStream) throws IOException {
        platformSpecificImplementation.write(image, format, outputStream);
    }
}
