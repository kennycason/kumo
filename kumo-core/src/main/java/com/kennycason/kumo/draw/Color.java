package com.kennycason.kumo.draw;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Color implements IColor, VaryingImpl{

    private IColor platformSpecificImplementation;

    public Color(int r, int g, int b){
        String name = "ColorImpl";
        try {

            Class<? extends IColor> clazz = (Class<? extends IColor>) Class.forName(Platform.PACKAGE + name);
            Constructor<? extends IColor> con = clazz.getConstructor(int.class, int.class, int.class);
            platformSpecificImplementation = con.newInstance(r, g, b);

        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex){
            ex.printStackTrace();
        }
    }

    public Color(int color){
        String name = "ColorImpl";
        try {

            Class<? extends IColor> clazz = (Class<? extends IColor>) Class.forName(Platform.PACKAGE + name);
            Constructor<? extends IColor> con = clazz.getConstructor(int.class, boolean.class);
            platformSpecificImplementation = con.newInstance(color, false);

        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex){
            ex.printStackTrace();
        }
    }

    public Color(int color, boolean alphaValid){
        String name = "ColorImpl";
        try {

            Class<? extends IColor> clazz = (Class<? extends IColor>) Class.forName(Platform.PACKAGE + name);
            Constructor<? extends IColor> con = clazz.getConstructor(int.class, boolean.class);
            platformSpecificImplementation = con.newInstance(color, alphaValid);

        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public int getInt() {
        return platformSpecificImplementation.getInt();
    }

    @Override
    public int getRed() {
        return platformSpecificImplementation.getRed();
    }

    @Override
    public int getGreen() {
        return platformSpecificImplementation.getGreen();
    }

    @Override
    public int getBlue() {
        return platformSpecificImplementation.getBlue();
    }

    @Override
    public Object getActual() {
        if(platformSpecificImplementation instanceof VaryingImpl){
            return ((VaryingImpl) platformSpecificImplementation).getActual();
        }

        return null;
    }
}
