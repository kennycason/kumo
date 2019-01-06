package com.kennycason.kumo.awtimpl;

import com.kennycason.kumo.interfaces.PointAbst;

import java.awt.*;

public class PointImpl extends PointAbst<Point> {

    public PointImpl(int x, int y) {
        super(x, y);
    }

    @Override
    public Point getActual() {
        return new Point(getX(), getY());
    }
}
