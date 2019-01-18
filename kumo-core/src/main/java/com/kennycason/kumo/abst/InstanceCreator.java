package com.kennycason.kumo.abst;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class InstanceCreator {

    private static final String PACKAGE;

    static {
        boolean isAndroid;
        try{
            //Just try to access an Android class to see what platform we're on.
            //The class is either not found (runtime on a system where we need to use AWT)
            //or a stub exception is thrown (during test, when Maven still provides the stub framework)
            Class.forName("android.graphics.Canvas").newInstance();

            isAndroid = true;
        }catch (Exception ex){
            isAndroid = false;
        }

        String basePckg = "com.kennycason.kumo";

        if(isAndroid){
            PACKAGE = basePckg + ".androidimpl.";
        }else{
            PACKAGE = basePckg + ".awtimpl.";
        }
    }

    public static ColorAbst color(int r, int g, int b){
        String name = "ColorImpl";
        try {

            Class<? extends ColorAbst> clazz = (Class<? extends ColorAbst>) Class.forName(PACKAGE + name);
            Constructor<? extends ColorAbst> con = clazz.getConstructor(int.class, int.class, int.class);
            return con.newInstance(r, g, b);

        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public static ColorAbst color(int color){
        return color(color, false);
    }

    public static ColorAbst color(int color, boolean alphaValid){
        String name = "ColorImpl";
        try {

            Class<? extends ColorAbst> clazz = (Class<? extends ColorAbst>) Class.forName(PACKAGE + name);
            Constructor<? extends ColorAbst> con = clazz.getConstructor(int.class, boolean.class);
            return con.newInstance(color, alphaValid);

        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public static DimensionAbst dimension(int x, int y){
        String name = "DimensionImpl";
        try {

            Class<? extends DimensionAbst> clazz = (Class<? extends DimensionAbst>) Class.forName(PACKAGE + name);
            Constructor<? extends DimensionAbst> con = clazz.getConstructor(int.class, int.class);
            return con.newInstance(x, y);

        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public static FontAbst font(String fontFamily, FontAbst.Face weight, int size){
        String name = "FontImpl";
        try {

            Class<? extends FontAbst> clazz = (Class<? extends FontAbst>) Class.forName(PACKAGE + name);
            Constructor<? extends FontAbst> con = clazz.getConstructor(String.class, FontAbst.Face.class, int.class);
            return con.newInstance(fontFamily, weight, size);

        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public static FontAbst font(InputStream inputStream){
        String name = "FontImpl";
        try {

            Class<? extends FontAbst> clazz = (Class<? extends FontAbst>) Class.forName(PACKAGE + name);
            Constructor<? extends FontAbst> con = clazz.getConstructor(InputStream.class);
            return con.newInstance(inputStream);

        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public static GraphicsAbst graphics(ImageAbst img){
        String name = "GraphicsImpl";
        try {

            Class<? extends GraphicsAbst> clazz = (Class<? extends GraphicsAbst>) Class.forName(PACKAGE + name);
            Constructor<? extends GraphicsAbst> con = clazz.getConstructor(ImageAbst.class);
            return con.newInstance(img);

        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public static ImageAbst image(int width, int height){
        String name = "ImageImpl";
        try {

            Class<? extends ImageAbst> clazz = (Class<? extends ImageAbst>) Class.forName(PACKAGE + name);
            Constructor<? extends ImageAbst> con = clazz.getConstructor(int.class, int.class);
            return con.newInstance(width, height);

        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public static ImageAbst image(InputStream inputStream){
        String name = "ImageImpl";
        try {

            Class<? extends ImageAbst> clazz = (Class<? extends ImageAbst>) Class.forName(PACKAGE + name);
            Constructor<? extends ImageAbst> con = clazz.getConstructor(InputStream.class);
            return con.newInstance(inputStream);

        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public static PointAbst point(int x, int y){
        String name = "PointImpl";
        try {

            Class<? extends PointAbst> clazz = (Class<? extends PointAbst>) Class.forName(PACKAGE + name);
            Constructor<? extends PointAbst> con = clazz.getConstructor(int.class, int.class);
            return con.newInstance(x, y);

        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public static ImageWriterAbst imageWriter(){
        String name = "ImageWriterImpl";
        try {

            Class<? extends ImageWriterAbst> clazz = (Class<? extends ImageWriterAbst>) Class.forName(PACKAGE + name);
            return clazz.newInstance();

        } catch(ClassNotFoundException | IllegalAccessException | InstantiationException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public static ImageRotatorAbst imageRotator(){
        String name = "ImageRotatorImpl";

        try {

            Class<? extends ImageRotatorAbst> clazz = (Class<? extends ImageRotatorAbst>) Class.forName(PACKAGE + name);
            return clazz.newInstance();

        } catch(ClassNotFoundException | IllegalAccessException | InstantiationException ex){
            ex.printStackTrace();
        }
        return null;
    }
}
