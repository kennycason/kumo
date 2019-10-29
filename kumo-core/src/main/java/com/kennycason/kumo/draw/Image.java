package com.kennycason.kumo.draw;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Image implements IImage, VaryingImpl{

    private IImage platformSpecificImplementation;

    public Image(int width, int height){
        String name = "ImageImpl";
        try {

            Class<? extends IImage> clazz = (Class<? extends IImage>) Class.forName(Platform.PACKAGE + name);
            Constructor<? extends IImage> con = clazz.getConstructor(int.class, int.class);
            platformSpecificImplementation = con.newInstance(width, height);

        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex){
            ex.printStackTrace();
        }
    }

    public Image(InputStream inputStream){
        String name = "ImageImpl";
        try {

            Class<? extends IImage> clazz = (Class<? extends IImage>) Class.forName(Platform.PACKAGE + name);
            Constructor<? extends IImage> con = clazz.getConstructor(InputStream.class);
            platformSpecificImplementation = con.newInstance(inputStream);

        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex){
            ex.printStackTrace();
        }
    }

    Image(IImage from){
        platformSpecificImplementation = from;
    }

    @Override
    public int getColor(int x, int y) {
        return platformSpecificImplementation.getColor(x, y);
    }

    @Override
    public int getWidth() {
        return platformSpecificImplementation.getWidth();
    }

    @Override
    public int getHeight() {
        return platformSpecificImplementation.getHeight();
    }

    @Override
    public Object getActual() {
        return platformSpecificImplementation.getActual();
    }
}
