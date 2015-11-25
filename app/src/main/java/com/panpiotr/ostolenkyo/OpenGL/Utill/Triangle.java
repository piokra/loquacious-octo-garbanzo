package com.panpiotr.ostolenkyo.OpenGL.Utill;

import android.util.Pair;

import com.panpiotr.ostolenkyo.Bitmap;
import com.panpiotr.ostolenkyo.Utill.Math.Point2D;

/**
 * Created by Pan Piotr on 25/11/2015.
 */
public class Triangle {
    public final Point2D First;
    public final Point2D Second;
    public final Point2D Third;

    public Triangle(Point2D first, Point2D second, Point2D third) {
        First = first;
        Second = second;
        Third = third;
    }

    public Triangle sortVerticesByX() {
        Point2D first = First;
        Point2D second = Second;
        Point2D third = Third;
        Point2D t;
        if (first.x > second.x) {
            t = first;
            first = second;
            second = t;
        }
        if (second.x > third.x) {
            t = second;
            second = third;
            third = t;
        }
        if (first.x > second.x) {
            t = first;
            first = second;
            second = t;
        }
        return new Triangle(first.clone(), second.clone(), third.clone());
    }

    public Triangle sortVerticesByY() {
        Point2D first = First;
        Point2D second = Second;
        Point2D third = Third;
        Point2D t;
        if (first.y > second.y) {
            t = first;
            first = second;
            second = t;
        }
        if (second.y > third.y) {
            t = second;
            second = third;
            third = t;
        }
        if (first.y > second.y) {
            t = first;
            first = second;
            second = t;
        }
        return new Triangle(first.clone(), second.clone(), third.clone());
    }

    protected Pair<Point2D, Point2D> getCircumscribedRectangle() {
        Triangle x = sortVerticesByX();
        Triangle y = sortVerticesByY();

        return new Pair<>(new Point2D(x.First.x, y.First.y), new Point2D(x.Third.x, y.Third.y));
    }

    //TODO actaul triangle collision
    protected boolean collides(Point2D point) {
        return true;
    }

    //TODO a decent resterization
    public void resterize(Bitmap bitmap, float zeroX, float zeroY, float xPerOne, float yPerOne) {
        Pair<Point2D, Point2D> rectangle = getCircumscribedRectangle();
        int startX = (int) ((rectangle.first.x - zeroX) / xPerOne);
        int startY = (int) ((rectangle.first.y - zeroY) / yPerOne);
        int width = (int) ((rectangle.second.x - rectangle.first.x) / xPerOne);
        int height = (int) ((rectangle.second.y - rectangle.first.y) / yPerOne);
        for (int i = 0; i < width; i++) {
            float pointX = zeroX + i * xPerOne;
            for (int j = 0; j < height; j++) {
                float pointY = zeroY + j * yPerOne;
                if (collides(new Point2D(pointX, pointY))) {
                    bitmap.set(1, startX + i, startY + j);
                }
            }
        }


    }

}
