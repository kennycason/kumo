package com.kennycason.kumo.abst;

import java.io.InputStream;

public class InstanceCreator {

    public static ColorAbst color(int r, int g, int b){
        return null;
    }

    public static ColorAbst color(int color){
        return null;
    }

    public static DimensionAbst dimension(int x, int y){
        return null;
    }

    public static FontAbst font(String fontFamily, FontAbst.Face weight, int size){
        return null;
    }

    public static FontAbst font(InputStream inputStream){
        return null;
    }

    public static FontMetricsAbst fontMetrics(){
        return null;
    }

    public static GraphicsAbst graphics(ImageAbst img){
        return null;
    }

    public static ImageAbst image(int width, int height){
        return null;
    }

    public static ImageAbst image(InputStream inputStream){
        return null;
    }

    public static PointAbst point(int x, int y){
        return null;
    }

    public static ImageWriterAbst imageWriter(){
        return null;
    }
}
