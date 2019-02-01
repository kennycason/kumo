package com.kennycason.kumo.abst;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class ColorAbst<IMPL> implements VaryingImpl<IMPL>{

    public abstract int getInt();

    public abstract int getRed();
    public abstract int getGreen();
    public abstract int getBlue();

    public static ColorAbst get(int r, int g, int b){
        String name = "ColorImpl";
        try {

            Class<? extends ColorAbst> clazz = (Class<? extends ColorAbst>) Class.forName(Platform.PACKAGE + name);
            Constructor<? extends ColorAbst> con = clazz.getConstructor(int.class, int.class, int.class);
            return con.newInstance(r, g, b);

        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public static ColorAbst get(int color){
        return get(color, false);
    }

    public static ColorAbst get(int color, boolean alphaValid){
        String name = "ColorImpl";
        try {

            Class<? extends ColorAbst> clazz = (Class<? extends ColorAbst>) Class.forName(Platform.PACKAGE + name);
            Constructor<? extends ColorAbst> con = clazz.getConstructor(int.class, boolean.class);
            return con.newInstance(color, alphaValid);

        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex){
            ex.printStackTrace();
        }
        return null;
    }
}
