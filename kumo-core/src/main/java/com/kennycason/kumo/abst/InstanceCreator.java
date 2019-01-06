package com.kennycason.kumo.abst;

import com.kennycason.kumo.awtimpl.*;

import java.awt.*;
import java.io.InputStream;

public class InstanceCreator {

    public static ColorAbst color(int r, int g, int b){
        return new ColorImpl(r, g, b);
    }

    public static ColorAbst color(int color){
        Color c = new Color(color);
        return new ColorImpl(c.getRed(), c.getGreen(), c.getBlue());
    }

    public static DimensionAbst dimension(int x, int y){
        return new DimensionImpl(x, y);
    }

    public static FontAbst font(String fontFamily, FontAbst.Face weight, int size){
        return new FontImpl(fontFamily, weight, size);
    }

    public static FontAbst font(InputStream inputStream){
        return new FontImpl(inputStream);
    }

    public static GraphicsAbst graphics(ImageAbst img){
        return new GraphicsImpl(img);
    }

    public static ImageAbst image(int width, int height){
        return new ImageImpl(width, height);
    }

    public static ImageAbst image(InputStream inputStream){
        return new ImageImpl(inputStream);
    }

    public static PointAbst point(int x, int y){
        return new PointImpl(x, y);
    }

    public static ImageWriterAbst imageWriter(){
        return new ImageWriterImpl();
    }
}
