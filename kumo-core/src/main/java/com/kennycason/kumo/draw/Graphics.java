package com.kennycason.kumo.draw;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Graphics implements IGraphics{

    private IGraphics platformSpecificImplementation;

    public Graphics(IImage img){
        String name = "GraphicsImpl";
        try {

            Class<? extends IGraphics> clazz = (Class<? extends IGraphics>) Class.forName(Platform.PACKAGE + name);
            Constructor<? extends IGraphics> con = clazz.getConstructor(IImage.class);
            platformSpecificImplementation = con.newInstance(img);

        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void drawImg(IImage img, int x, int y) {
        platformSpecificImplementation.drawImg(img, x, y);
    }

    @Override
    public void drawString(String s, int x, int y, IColor color) {
        platformSpecificImplementation.drawString(s, x, y, color);
    }

    @Override
    public void drawRect(IColor color, int x, int y, int width, int height) {
        platformSpecificImplementation.drawRect(color, x, y, width, height);
    }

    @Override
    public void setFont(IFont font) {
        platformSpecificImplementation.setFont(font);
    }

    @Override
    public FontMetrics getFontMetrics() {
        return new FontMetrics(platformSpecificImplementation.getFontMetrics());
    }
}
