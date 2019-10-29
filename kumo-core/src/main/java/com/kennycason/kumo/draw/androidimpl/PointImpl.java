package com.kennycason.kumo.draw.androidimpl;

import com.kennycason.kumo.draw.IPoint;

public class PointImpl implements IPoint {

    private int x;
    private int y;

    public PointImpl(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }
}
