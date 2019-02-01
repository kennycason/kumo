package com.kennycason.kumo.abst;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class ImageAbst<IMPL> implements VaryingImpl<IMPL>{

    public abstract int getColor(int x, int y);

    public abstract int getWidth();

    public abstract int getHeight();

    public static ImageAbst get(int width, int height){
        String name = "ImageImpl";
        try {

            Class<? extends ImageAbst> clazz = (Class<? extends ImageAbst>) Class.forName(Platform.PACKAGE + name);
            Constructor<? extends ImageAbst> con = clazz.getConstructor(int.class, int.class);
            return con.newInstance(width, height);

        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public static ImageAbst get(InputStream inputStream){
        String name = "ImageImpl";
        try {

            Class<? extends ImageAbst> clazz = (Class<? extends ImageAbst>) Class.forName(Platform.PACKAGE + name);
            Constructor<? extends ImageAbst> con = clazz.getConstructor(InputStream.class);
            return con.newInstance(inputStream);

        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex){
            ex.printStackTrace();
        }
        return null;
    }
}
