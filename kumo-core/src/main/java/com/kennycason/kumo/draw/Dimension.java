package com.kennycason.kumo.draw;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Dimension implements IDimension{

    private IDimension platformSpecificImplementation;

    public Dimension(int x, int y){
        String name = "DimensionImpl";
        try {

            Class<? extends IDimension> clazz = (Class<? extends IDimension>) Class.forName(Platform.PACKAGE + name);
            Constructor<? extends IDimension> con = clazz.getConstructor(int.class, int.class);
            platformSpecificImplementation = con.newInstance(x, y);

        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public int getWidth() {
        return platformSpecificImplementation.getWidth();
    }

    @Override
    public int getHeight() {
        return platformSpecificImplementation.getHeight();
    }
}
