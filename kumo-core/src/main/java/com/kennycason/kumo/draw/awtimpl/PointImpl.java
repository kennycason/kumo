package com.kennycason.kumo.draw.awtimpl;

import com.kennycason.kumo.draw.IPoint;
import com.kennycason.kumo.draw.VaryingImpl;

import java.awt.*;

public class PointImpl implements IPoint, VaryingImpl<Point> {

    private Point actual;

    public PointImpl(int x, int y) {
        actual = new Point(x, y);
    }

    @Override
    public Point getActual() {
        return actual;
    }

    @Override
    public int getX() {
        return (int) actual.getX();
    }

    @Override
    public int getY() {
        return (int) actual.getY();
    }

    @Override
    public void setX(int x) {
        actual.x = x;
    }

    @Override
    public void setY(int y) {
        actual.y = y;
    }
}
