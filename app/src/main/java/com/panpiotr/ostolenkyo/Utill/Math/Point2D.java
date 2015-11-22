package com.panpiotr.ostolenkyo.Utill.Math;

/**
 * Created by Pan Piotr on 22/11/2015.
 */
public class Point2D {
    public float x;
    public float y;

    public Point2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public static Point2D multiply(float l, Point2D r) {
        return new Point2D(l * r.x, l * r.y);
    }

    public static Point2D multiply(Point2D r, float l) {
        return multiply(l, r);
    }

    public static Point2D add(Point2D l, Point2D r) {
        return new Point2D(l.x + r.x, l.y + r.y);
    }

    public static Point2D subtract(Point2D l, Point2D r) {
        return new Point2D(l.x - r.x, l.y - r.y);
    }

    public Point2D clone() {
        return new Point2D(x, y);
    }

    public float length() {
        return (float) Math.sqrt((double) squareLength());
    }

    public float squareLength() {
        return x * x + y * y;
    }

}
