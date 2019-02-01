package com.kennycason.kumo.abst;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class FontAbst<IMPL> implements VaryingImpl<IMPL>{

    public abstract void registerIfNecessary();

    public abstract FontAbst withSize(float size);

    public enum Face {
        PLAIN,
        BOLD,
        ITALIC
    }

    public static FontAbst get(String fontFamily, FontAbst.Face weight, int size){
        String name = "FontImpl";
        try {

            Class<? extends FontAbst> clazz = (Class<? extends FontAbst>) Class.forName(Platform.PACKAGE + name);
            Constructor<? extends FontAbst> con = clazz.getConstructor(String.class, FontAbst.Face.class, int.class);
            return con.newInstance(fontFamily, weight, size);

        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public static FontAbst get(InputStream inputStream){
        String name = "FontImpl";
        try {

            Class<? extends FontAbst> clazz = (Class<? extends FontAbst>) Class.forName(Platform.PACKAGE + name);
            Constructor<? extends FontAbst> con = clazz.getConstructor(InputStream.class);
            return con.newInstance(inputStream);

        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex){
            ex.printStackTrace();
        }
        return null;
    }
}
