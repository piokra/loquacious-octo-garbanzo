package com.panpiotr.ostolenkyo.OCR.SVM;

import com.panpiotr.ostolenkyo.Bitmap;
import com.panpiotr.ostolenkyo.OCR.Utill.BitmapAlgebra;
import com.panpiotr.ostolenkyo.OCR.Utill.CoreFunctions.CoreFunction;

/**
 * Created by Pan Piotr on 03/12/2015.
 */
public class SVM {
    private final CoreFunction mCoreFunction;
    private final Bitmap mVector;
    private final float mB;

    public SVM(CoreFunction cf, Bitmap vector, float b) {
        mCoreFunction = cf;
        mVector = BitmapAlgebra.multiply(1, vector);
        mB = b;
    }


    float evaluateValue(Bitmap sample) {
        return CoreFunction(sample, mVector) + mB;
    }

    float evaluateSign(Bitmap sample) {
        return Math.signum(evaluateValue(sample));
    }
}
