package com.kennycason.kumo.androidimpl;

import android.graphics.Typeface;
import com.kennycason.kumo.abst.FontAbst;

import java.io.File;
import java.io.InputStream;

public class FontImpl extends FontAbst<Typeface> {

    private Typeface typeface;

    private float size;

    public FontImpl(String fontFamily, FontAbst.Face weight, int size){
        typeface = Typeface.create(fontFamily, getStyle(weight));
        this.size = size;
    }

    public FontImpl(InputStream fromInputStream){
        throw new UnsupportedOperationException("Constructing a font from an InputStream is not supported on Android. Use new FontImpl(file) instead.");
    }

    public FontImpl(File file){
        typeface = Typeface.createFromFile(file);
        size = 40;
    }

    public FontImpl(Typeface typeface){
        this.typeface = typeface;
        size = 40;
    }

    public FontImpl(Typeface typeface, float size){
        this.typeface = typeface;
        this.size = size;
    }

    @Override
    public void registerIfNecessary() {
        //Not necessary on Android
    }

    @Override
    public FontAbst withSize(float size) {
        this.size = size;
        return this;
    }

    public float getSize() {
        return size;
    }

    @Override
    public Typeface getActual() {
        return typeface;
    }

    private int getStyle(FontAbst.Face weight){
        if(weight == Face.BOLD){
            return Typeface.BOLD;
        }else if(weight == Face.ITALIC){
            return Typeface.ITALIC;
        }else {
            return Typeface.NORMAL;
        }
    }
}
