package com.panpiotr.ostolenkyo.OCR.Utill.CoreFunctions;

import com.panpiotr.ostolenkyo.Bitmap;
import com.panpiotr.ostolenkyo.OCR.Utill.BitmapAlgebra;
import com.panpiotr.ostolenkyo.OCR.Utill.BitmapAlgebraException;

/**
 * Created by Pan Piotr on 03/12/2015.
 */
public class DefaultFunction implements CoreFunction {
    @Override
    public float evaluate(Bitmap l, Bitmap r) {
        try {
            return BitmapAlgebra.scalarProduct(l, r);

        } catch (BitmapAlgebraException e) {
            return Float.NaN;
        }
    }
}
