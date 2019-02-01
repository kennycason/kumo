package com.kennycason.kumo.abst;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class PointAbst<IMPL> implements VaryingImpl<IMPL> {

    private int x;
    private int y;

    public PointAbst(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public static PointAbst get(int x, int y){
        String name = "PointImpl";
        try {

            Class<? extends PointAbst> clazz = (Class<? extends PointAbst>) Class.forName(Platform.PACKAGE + name);
            Constructor<? extends PointAbst> con = clazz.getConstructor(int.class, int.class);
            return con.newInstance(x, y);

        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex){
            ex.printStackTrace();
        }
        return null;
    }
}
