package com.panpiotr.ostolenkyo.OCR.Utill;

import com.panpiotr.ostolenkyo.Bitmap;

/**
 * Created by Pan Piotr on 03/12/2015.
 */
public class BitmapAlgebra {
    private BitmapAlgebra() {

    }

    public static void normalize(Bitmap toNormalize, float currentMax, float currentMin, float newMax, float newMin) {
        final float oldLength = currentMax - currentMin;
        final float newLength = newMax - newMin;
        final float ratio = oldLength / newLength;
        for (int i = 0; i < toNormalize.Width; i++) {
            for (int j = 0; j < toNormalize.Height; j++) {
                float t = toNormalize.get(i, j);
                t = ((t - currentMin) * ratio) + newMin;
                toNormalize.set(t, i, j);
            }
        }
    }

    public static float scalarProduct(Bitmap l, Bitmap r) throws BitmapAlgebraException {
        if (l.Width != r.Width || l.Height != r.Height) throw new BitmapAlgebraException();
        float res = 0;
        for (int i = 0; i < l.Width; i++) {
            for (int j = 0; j < r.Height; j++) {
                res += l.get(i, j) * r.get(i, j);
            }
        }
        return res;
    }

    public static Bitmap multiply(float alpha, Bitmap b) {
        float[] newBitmap = new float[b.Width * b.Height];
        for (int i = 0; i < b.Width; i++) {
            for (int j = 0; j < b.Height; j++) {
                newBitmap[i * b.Height + j] = alpha * b.get(i, j);
            }
        }
        return new Bitmap(b.Width, b.Height, newBitmap);
    }

    public static Bitmap multiply(Bitmap b, float alpha) {
        return multiply(alpha, b);
    }

    public static Bitmap add(Bitmap l, Bitmap r) throws BitmapAlgebraException {
        if (l.Width != r.Width || l.Height != r.Height) throw new BitmapAlgebraException();
        float[] newBitmap = new float[l.Width * l.Height];
        for (int i = 0; i < l.Width; i++) {
            for (int j = 0; j < l.Height; j++) {
                newBitmap[i * l.Height + j] = r.get(i, j) + l.get(i, j);
            }
        }
        return new Bitmap(l.Width, l.Height, newBitmap);
    }

    public static Bitmap subtract(Bitmap l, Bitmap r) throws BitmapAlgebraException {
        if (l.Width != r.Width || l.Height != r.Height) throw new BitmapAlgebraException();
        float[] newBitmap = new float[l.Width * l.Height];
        for (int i = 0; i < l.Width; i++) {
            for (int j = 0; j < l.Height; j++) {
                newBitmap[i * l.Height + j] = l.get(i, j) - r.get(i, j);
            }
        }
        return new Bitmap(l.Width, l.Height, newBitmap);
    }
}
