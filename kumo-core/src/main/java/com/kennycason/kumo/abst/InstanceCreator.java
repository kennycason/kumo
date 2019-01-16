package com.kennycason.kumo.abst;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class InstanceCreator {

    private static final String PACKAGE = "com.kennycason.kumo";

    public static ColorAbst color(int r, int g, int b){
        String awtName = ".awtimpl.ColorImpl";
        try {

            Class<? extends ColorAbst> clazz = (Class<? extends ColorAbst>) Class.forName(PACKAGE + awtName);
            Constructor<? extends ColorAbst> con = clazz.getConstructor(int.class, int.class, int.class);
            return con.newInstance(r, g, b);

        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex){
            throw new IllegalStateException("Unexpected exception: " + ex.getMessage());
        }
    }

    public static ColorAbst color(int color){
        return color(color, false);
    }

    public static ColorAbst color(int color, boolean alphaValid){
        String awtName = ".awtimpl.ColorImpl";
        try {

            Class<? extends ColorAbst> clazz = (Class<? extends ColorAbst>) Class.forName(PACKAGE + awtName);
            Constructor<? extends ColorAbst> con = clazz.getConstructor(int.class, boolean.class);
            return con.newInstance(color, alphaValid);

        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex){
            throw new IllegalStateException("Unexpected exception: " + ex.getMessage());
        }
    }

    public static DimensionAbst dimension(int x, int y){
        String awtName = ".awtimpl.DimensionImpl";
        try {

            Class<? extends DimensionAbst> clazz = (Class<? extends DimensionAbst>) Class.forName(PACKAGE + awtName);
            Constructor<? extends DimensionAbst> con = clazz.getConstructor(int.class, int.class);
            return con.newInstance(x, y);

        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex){
            throw new IllegalStateException("Unexpected exception: " + ex.getMessage());
        }
    }

    public static FontAbst font(String fontFamily, FontAbst.Face weight, int size){
        String awtName = ".awtimpl.FontImpl";
        try {

            Class<? extends FontAbst> clazz = (Class<? extends FontAbst>) Class.forName(PACKAGE + awtName);
            Constructor<? extends FontAbst> con = clazz.getConstructor(String.class, FontAbst.Face.class, int.class);
            return con.newInstance(fontFamily, weight, size);

        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex){
            throw new IllegalStateException("Unexpected exception: " + ex.getMessage());
        }
    }

    public static FontAbst font(InputStream inputStream){
        String awtName = ".awtimpl.FontImpl";
        try {

            Class<? extends FontAbst> clazz = (Class<? extends FontAbst>) Class.forName(PACKAGE + awtName);
            Constructor<? extends FontAbst> con = clazz.getConstructor(InputStream.class);
            return con.newInstance(inputStream);

        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex){
            throw new IllegalStateException("Unexpected exception: " + ex.getMessage());
        }
    }

    public static GraphicsAbst graphics(ImageAbst img){
        String awtName = ".awtimpl.GraphicsImpl";
        try {

            Class<? extends GraphicsAbst> clazz = (Class<? extends GraphicsAbst>) Class.forName(PACKAGE + awtName);
            Constructor<? extends GraphicsAbst> con = clazz.getConstructor(ImageAbst.class);
            return con.newInstance(img);

        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex){
            throw new IllegalStateException("Unexpected exception: " + ex.getMessage());
        }
    }

    public static ImageAbst image(int width, int height){
        String awtName = ".awtimpl.ImageImpl";
        try {

            Class<? extends ImageAbst> clazz = (Class<? extends ImageAbst>) Class.forName(PACKAGE + awtName);
            Constructor<? extends ImageAbst> con = clazz.getConstructor(int.class, int.class);
            return con.newInstance(width, height);

        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex){
            throw new IllegalStateException("Unexpected exception: " + ex.getMessage());
        }
    }

    public static ImageAbst image(InputStream inputStream){
        String awtName = ".awtimpl.ImageImpl";
        try {

            Class<? extends ImageAbst> clazz = (Class<? extends ImageAbst>) Class.forName(PACKAGE + awtName);
            Constructor<? extends ImageAbst> con = clazz.getConstructor(InputStream.class);
            return con.newInstance(inputStream);

        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex){
            throw new IllegalStateException("Unexpected exception: " + ex.getMessage());
        }
    }

    public static PointAbst point(int x, int y){
        String awtName = ".awtimpl.PointImpl";
        try {

            Class<? extends PointAbst> clazz = (Class<? extends PointAbst>) Class.forName(PACKAGE + awtName);
            Constructor<? extends PointAbst> con = clazz.getConstructor(int.class, int.class);
            return con.newInstance(x, y);

        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex){
            throw new IllegalStateException("Unexpected exception: " + ex.getMessage());
        }
    }

    public static ImageWriterAbst imageWriter(){
        String awtName = ".awtimpl.ImageWriterImpl";
        try {

            Class<? extends ImageWriterAbst> clazz = (Class<? extends ImageWriterAbst>) Class.forName(PACKAGE + awtName);
            return clazz.newInstance();

        } catch(ClassNotFoundException | IllegalAccessException | InstantiationException ex){
            throw new IllegalStateException("Unexpected exception: " + ex.getMessage());
        }
    }
}
