package com.panpiotr.ostolenkyo.OCR.Utill.CoreFunctions;

import com.panpiotr.ostolenkyo.Bitmap;
import com.panpiotr.ostolenkyo.OCR.Utill.BitmapAlgebra;
import com.panpiotr.ostolenkyo.OCR.Utill.BitmapAlgebraException;

/**
 * Created by Pan Piotr on 03/12/2015.
 */
public class RadialCore implements CoreFunction {
    final float mSigma;

    public RadialCore(float sigma) {
        mSigma = sigma;
    }

    @Override
    public float evaluate(Bitmap l, Bitmap r) {
        float ret = 1;
        float t = 0;
        try {
            Bitmap n = BitmapAlgebra.subtract(l, r);
            t = BitmapAlgebra.scalarProduct(n, n);


        } catch (BitmapAlgebraException e) {
            return Float.NaN;
        }

        return Math.exp(-t / (2 * sigma * sigma));
    }
}
