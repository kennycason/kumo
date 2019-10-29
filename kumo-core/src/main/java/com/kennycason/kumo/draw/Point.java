package com.kennycason.kumo.draw;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Point implements IPoint{

    private IPoint platformSpecificImplementation;

    public Point(int x, int y){
        String name = "PointImpl";
        try {

            Class<? extends IPoint> clazz = (Class<? extends IPoint>) Class.forName(Platform.PACKAGE + name);
            Constructor<? extends IPoint> con = clazz.getConstructor(int.class, int.class);
            platformSpecificImplementation = con.newInstance(x, y);

        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public int getX() {
        return platformSpecificImplementation.getX();
    }

    @Override
    public int getY() {
        return platformSpecificImplementation.getY();
    }

    @Override
    public void setX(int x) {
        platformSpecificImplementation.setX(x);
    }

    @Override
    public void setY(int y) {
        platformSpecificImplementation.setY(y);
    }
}
