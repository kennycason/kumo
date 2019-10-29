package com.kennycason.kumo.draw;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Font implements IFont {

    private IFont platformSpecificImplementation;

    public Font(String fontFamily, FontFace weight, int size){
        String name = "FontImpl";
        try {

            Class<? extends IFont> clazz = (Class<? extends IFont>) Class.forName(Platform.PACKAGE + name);
            Constructor<? extends IFont> con = clazz.getConstructor(String.class, FontFace.class, int.class);
            platformSpecificImplementation = con.newInstance(fontFamily, weight, size);

        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex){
            ex.printStackTrace();
        }
    }

    public Font(InputStream inputStream){
        String name = "FontImpl";
        try {

            Class<? extends IFont> clazz = (Class<? extends IFont>) Class.forName(Platform.PACKAGE + name);
            Constructor<? extends IFont> con = clazz.getConstructor(InputStream.class);
            platformSpecificImplementation = con.newInstance(inputStream);

        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void registerIfNecessary() {
        platformSpecificImplementation.registerIfNecessary();
    }

    @Override
    public void setSize(float size) {
        platformSpecificImplementation.setSize(size);
    }

    @Override
    public float getSize() {
        return platformSpecificImplementation.getSize();
    }

    @Override
    public Object getActual() {
        return platformSpecificImplementation.getActual();
    }
}
