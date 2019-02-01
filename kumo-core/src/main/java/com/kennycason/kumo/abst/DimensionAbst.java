package com.kennycason.kumo.abst;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class DimensionAbst<IMPL> implements VaryingImpl<IMPL>{

    private int width;
    private int height;

    public DimensionAbst(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public static DimensionAbst get(int x, int y){
        String name = "DimensionImpl";
        try {

            Class<? extends DimensionAbst> clazz = (Class<? extends DimensionAbst>) Class.forName(Platform.PACKAGE + name);
            Constructor<? extends DimensionAbst> con = clazz.getConstructor(int.class, int.class);
            return con.newInstance(x, y);

        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex){
            ex.printStackTrace();
        }
        return null;
    }
}
