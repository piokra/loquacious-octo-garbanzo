package com.panpiotr.ostolenkyo;

/**
 * Created by Pan Piotr on 24/11/2015.
 */
public class Bitmap {
    public final int Width;
    public final int Height;
    public final int ColorsPerPixel;
    final float[] mBitmap;

    public Bitmap(int width, int height, int colorsPerPixel, float[] bits) {
        Width = width;
        Height = height;
        ColorsPerPixel = colorsPerPixel;
        mBitmap = bits;
    }

    public float get(int x, int y) {
        return mBitmap[x * Height + y];
    }

    public void set(float val, int x, int y) {
        mBitmap[x * Height + y] = val;
    }
}
