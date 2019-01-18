package com.kennycason.kumo.androidimpl;

import android.graphics.Point;
import com.kennycason.kumo.abst.PointAbst;

public class PointImpl extends PointAbst<Point> {

    public PointImpl(int x, int y) {
        super(x, y);
    }

    @Override
    public Point getActual() {
        return new Point(getX(), getY());
    }
}
