package com.kennycason.kumo.abst;

import java.io.IOException;
import java.io.OutputStream;

public abstract class ImageWriterAbst {
    public abstract void write(ImageAbst image, String format, OutputStream outputStream) throws IOException;

    public static ImageWriterAbst get(){
        String name = "ImageWriterImpl";
        try {

            Class<? extends ImageWriterAbst> clazz = (Class<? extends ImageWriterAbst>) Class.forName(Platform.PACKAGE + name);
            return clazz.newInstance();

        } catch(ClassNotFoundException | IllegalAccessException | InstantiationException ex){
            ex.printStackTrace();
        }
        return null;
    }
}
