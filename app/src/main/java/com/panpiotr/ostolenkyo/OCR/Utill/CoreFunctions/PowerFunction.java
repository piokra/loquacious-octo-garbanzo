package com.panpiotr.ostolenkyo.OCR.Utill.CoreFunctions;

import com.panpiotr.ostolenkyo.Bitmap;
import com.panpiotr.ostolenkyo.OCR.Utill.BitmapAlgebra;
import com.panpiotr.ostolenkyo.OCR.Utill.BitmapAlgebraException;

/**
 * Created by Pan Piotr on 03/12/2015.
 */
public class PowerFunction implements CoreFunction {
    private final int mN;

    public PowerFunction(int n) {
        mN = n;
    }

    @Override
    public float evaluate(Bitmap l, Bitmap r) {
        float ret = 1;
        float t = 0;
        try {
            t = BitmapAlgebra.scalarProduct(l, r);

        } catch (BitmapAlgebraException e) {
            return Float.NaN;
        }
        for (int i = 0; i < mN; i++) {
            ret *= (t + 1);
        }
        return ret;
    }
}
