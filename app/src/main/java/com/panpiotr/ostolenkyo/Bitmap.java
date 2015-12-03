package com.panpiotr.ostolenkyo;

import android.util.Log;

/**
 * Created by Pan Piotr on 24/11/2015.
 */
public class Bitmap {
    public final int Width;
    public final int Height;
    final float[] mBitmap;

    public Bitmap(int width, int height, float[] bits) {
        Width = width;
        Height = height;
        mBitmap = bits;
    }

    public void log(String tag) {
        for (int i = 0; i < Width; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < Height; j++) {
                if (get(i, j) > 0)
                    sb.append("B");
                else
                    sb.append("W");

            }
            Log.d(tag, sb.toString());
        }
    }
    public float get(int x, int y) {
        return mBitmap[x * Height + y];
    }

    public void set(float val, int x, int y) {
        mBitmap[x * Height + y] = val;
    }
}
