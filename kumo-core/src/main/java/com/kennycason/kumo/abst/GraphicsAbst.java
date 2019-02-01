package com.kennycason.kumo.abst;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class GraphicsAbst {

    public abstract void drawImg(ImageAbst img, int x, int y);

    public abstract void drawString(String s, int x, int y, ColorAbst color);

    public abstract void drawRect(ColorAbst color, int x, int y, int width, int height);

    public abstract void enableAntiAliasing();

    public abstract void setFont(FontAbst font);

    public abstract FontMetricsAbst getFontMetrics();

    public static GraphicsAbst get(ImageAbst img){
        String name = "GraphicsImpl";
        try {

            Class<? extends GraphicsAbst> clazz = (Class<? extends GraphicsAbst>) Class.forName(Platform.PACKAGE + name);
            Constructor<? extends GraphicsAbst> con = clazz.getConstructor(ImageAbst.class);
            return con.newInstance(img);

        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex){
            ex.printStackTrace();
        }
        return null;
    }
}
