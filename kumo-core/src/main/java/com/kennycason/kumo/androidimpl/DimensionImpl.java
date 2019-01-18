package com.kennycason.kumo.androidimpl;

import com.kennycason.kumo.abst.DimensionAbst;

public class DimensionImpl extends DimensionAbst {

    public DimensionImpl(int width, int height) {
        super(width, height);
    }

    @Override
    public Object getActual() {
        throw new NullPointerException("Dimension has no actual implementation in Android!");
    }
}
