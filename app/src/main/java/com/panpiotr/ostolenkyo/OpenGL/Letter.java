package com.panpiotr.ostolenkyo.OpenGL;

import android.util.Log;

import com.panpiotr.ostolenkyo.Utill.Math.Point2D;

/**
 * Created by Pan Piotr on 22/11/2015.
 */
public class Letter {

    private final float[] mPoints;
    private final float mRadius;
    private final float[] mInput;

    public Letter(float radius, float[] points) {
        mPoints = new float[12 + 12 * (points.length / 2 - 1)];
        mRadius = radius;
        mInput = points;
        if (points.length == 0) return;
        if (points.length == 2) {
            Point2D o = new Point2D(mInput[0], mInput[1]);
            Point2D vector = new Point2D(1, 0);
            addFirstTriangle(o, vector);
            addLastTriangle(o, vector);
        } else {

            Point2D point = new Point2D(mInput[0], mInput[1]);
            Point2D next = new Point2D(mInput[2], mInput[3]);
            Point2D vector = Point2D.subtract(next, point);
            addFirstTriangle(point, vector);
            int length = mInput.length / 2;

            for (int j = 0; j < length - 1; j++) {
                next = new Point2D(mInput[(j + 1) * 2], mInput[(j + 1) * 2 + 1]);
                addTriangle(j + 6, point, next, vector);
                vector = Point2D.subtract(next, point);
                point = next;

            }
            addLastTriangle(next, vector);
            for (int i = 0; i < mPoints.length / 2; i++) {
                Log.d("TUCK FRUMP", String.valueOf(mPoints[i * 2]) + " " + String.valueOf(mPoints[i * 2 + 1]));
            }
        }

    }

    // point is a starting point, vector points towards next point if such exists otherwise any vector
    protected void putTriangle(int position, Point2D f, Point2D s, Point2D t) {
        mPoints[position] = f.x;
        mPoints[position + 1] = f.y;
        mPoints[position + 2] = s.x;
        mPoints[position + 3] = s.y;
        mPoints[position + 4] = t.x;
        mPoints[position + 5] = t.y;

    }

    protected void addFirstTriangle(Point2D point, Point2D vector) {
        Point2D normalized = Point2D.multiply(1 / vector.length(), vector);
        Point2D rotatedU = new Point2D(normalized.y, -normalized.x);
        Point2D rotatedD = Point2D.multiply(-1, rotatedU);

        rotatedU = Point2D.multiply(mRadius, rotatedU);
        rotatedD = Point2D.multiply(mRadius, rotatedD);
        normalized = Point2D.multiply(-mRadius, normalized);

        Point2D o = point;
        Point2D p1 = Point2D.add(normalized, o);
        Point2D p2 = Point2D.add(rotatedD, o);
        Point2D p3 = Point2D.add(rotatedU, o);
        putTriangle(0, p1, p2, p3);

    }

    protected void addLastTriangle(Point2D point, Point2D vector) {
        Point2D normalized = Point2D.multiply(1 / vector.length(), vector);
        Point2D rotatedU = new Point2D(normalized.y, -normalized.x);
        Point2D rotatedD = Point2D.multiply(-1, rotatedU);

        rotatedU = Point2D.multiply(mRadius, rotatedU);
        rotatedD = Point2D.multiply(mRadius, rotatedD);
        normalized = Point2D.multiply(mRadius, normalized);

        Point2D o = point;
        Point2D p1 = Point2D.add(normalized, o);
        Point2D p2 = Point2D.add(rotatedD, o);
        Point2D p3 = Point2D.add(rotatedU, o);
        putTriangle(mPoints.length - 6, p1, p2, p3);
    }

    protected void addTriangle(int position, Point2D point, Point2D next, Point2D vector) {
        Point2D normalized = Point2D.multiply(1 / vector.length(), vector);
        Point2D rotatedU = new Point2D(normalized.y, -normalized.x);
        Point2D rotatedD = Point2D.multiply(-1, rotatedU);

        rotatedU = Point2D.multiply(mRadius, rotatedU);
        rotatedD = Point2D.multiply(mRadius, rotatedD);
        normalized = Point2D.multiply(-mRadius, normalized);

        Point2D o = point;
        Point2D p1 = Point2D.add(rotatedD, point);
        Point2D p2 = Point2D.add(rotatedD, next);
        Point2D p3 = Point2D.add(rotatedU, next);
        Point2D p4 = Point2D.add(rotatedU, point);
        putTriangle(position, p1, p2, p3);
        putTriangle(position + 6, p3, p4, p1);
    }


    public float[] getPoints() {
        return mPoints;
    }
}
