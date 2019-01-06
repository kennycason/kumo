package com.kennycason.kumo.abst;

public abstract class ColorAbst<IMPL> implements VaryingImpl<IMPL>{

    private int red;
    private int green;
    private int blue;

    public ColorAbst(int red, int green, int blue){
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public abstract int getInt();
}
