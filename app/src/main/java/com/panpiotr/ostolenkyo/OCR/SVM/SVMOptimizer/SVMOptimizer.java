package com.panpiotr.ostolenkyo.OCR.SVM.SVMOptimizer;

import android.util.Log;

import com.panpiotr.ostolenkyo.Bitmap;
import com.panpiotr.ostolenkyo.OCR.SVM.SVM;
import com.panpiotr.ostolenkyo.OCR.SVM.SVMWrongBitmapDimension;
import com.panpiotr.ostolenkyo.OCR.Utill.BitmapAlgebra;
import com.panpiotr.ostolenkyo.OCR.Utill.BitmapAlgebraException;
import com.panpiotr.ostolenkyo.OCR.Utill.CoreFunctions.CoreFunction;
import com.panpiotr.ostolenkyo.OCR.Utill.LearningData;
import com.panpiotr.ostolenkyo.OCR.Utill.LearningSet;
import com.panpiotr.ostolenkyo.OCR.Utill.LearningType;

/**
 * Created by Pan Piotr on 05/12/2015.
 */
public class SVMOptimizer {
    private final LearningSet mLearningSet;
    private final LearningType mTypeToDetect;
    private final CoreFunction mCoreFunction;
    private final float[][] mScalarProducts;
    private final float mFailChange = (float) (1 / Math.PI);
    private final float mChange = 1.05f;
    private final float mEpsilon = (float) 10e-9;
    private float[] mBest;
    private float mBestValue;
    private float mCurrentStep = 1.0f;
    private float mLastChange = 1.0f;
    private float mMaxLagrangeMultiplier;

    public SVMOptimizer(LearningSet learningSet, LearningType typeToDetect, CoreFunction coreFunction) {
        mLearningSet = learningSet;
        mTypeToDetect = typeToDetect;
        mCoreFunction = coreFunction;
        mScalarProducts = new float[mLearningSet.getDataSize()][];
        for (int i = 0; i < mScalarProducts.length; i++) {
            mScalarProducts[i] = new float[mScalarProducts.length];
        }
    }

    protected float getY(LearningData data) {
        if (data.Type == mTypeToDetect) return 1.0f;
        return -1.0f;
    }

    protected float deduceB(Bitmap vector) throws SVMWrongBitmapDimension {
        vector = BitmapAlgebra.normalize(vector);
        int bestCorrect = 0;
        int bestWrong = 0;
        float bestB = Float.NaN;
        float worstB = Float.NaN;
        for (int i = 0; i < mLearningSet.getDataSize(); i++) {
            LearningData xl = mLearningSet.get(i);
            for (int j = 0; j < mLearningSet.getDataSize(); j++) {
                LearningData xr = mLearningSet.get(j);
                if (getY(xl) != getY(xr)) {
                    try {
                        float curB = BitmapAlgebra.scalarProduct(vector, BitmapAlgebra.subtract(xl.Bitmap, xr.Bitmap));
                        int currentCorrect = 0;
                        int currentWrong = 0;
                        SVM currentSVM = new SVM(mCoreFunction, vector, curB);
                        for (int k = 0; k < mLearningSet.getDataSize(); k++) {
                            LearningData curData = mLearningSet.get(k);
                            if (currentSVM.evaluateSign(curData.Bitmap) == getY(curData)) {
                                currentCorrect++;
                            } else {
                                currentWrong++;
                            }
                        }
                        if (currentCorrect > bestCorrect) {
                            bestB = curB;
                            bestCorrect = currentCorrect;
                        }
                        if (currentWrong > bestWrong) {
                            worstB = curB;
                            bestWrong = currentWrong;
                        }
                    } catch (BitmapAlgebraException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        if (bestB == Float.NaN) throw new SVMWrongBitmapDimension();

        Log.d("SVMOptimizer", "Deduce B");
        StringBuilder sb = new StringBuilder();
        sb.append("Best Correct ");
        sb.append(bestCorrect);
        sb.append(" Best Wrong ");
        sb.append(bestWrong);
        sb.append(" bestB ");
        sb.append(bestB);
        sb.append(" worstB ");
        sb.append(worstB);

        Log.d("SVMOptimizer", sb.toString());

        return bestB;
    }

    protected void calculateScalarProducts() {
        for (int i = 0; i < mScalarProducts.length; i++) {
            for (int j = 0; j < mScalarProducts[i].length; j++) {
                mScalarProducts[i][j] = mCoreFunction.evaluate(mLearningSet.get(i).Bitmap, mLearningSet.get(j).Bitmap);
            }
        }
    }

    protected float[] arraySum(float[] l, float[] r) {
        float[] ret = new float[r.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = l[i] + r[i];
        }
        return r;
    }

    protected float[] arrayMul(float alpha, float[] r) {
        float[] ret = new float[r.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = alpha * r[i];
        }
        return r;
    }

    protected void trimMultipliers(float[] in, float max) {
        for (int i = 0; i < in.length; i++) {
            if (in[i] < 0) in[i] = 0;
            if (in[i] > max) in[i] = max;
        }
    }

    protected float[] countGradient(float[] arguments) {
        float[] floats = new float[mLearningSet.getDataSize()];
        for (int i = 0; i < floats.length; i++) {
            float der = 1;
            for (int j = 0; j < floats.length; j++) {
                if (i != j)
                    der -= 0.5 * arguments[j] * getY(mLearningSet.get(j)) * getY(mLearningSet.get(i)) * mScalarProducts[i][j];
            }
            floats[i] = der;
        }
        return floats;
    }

    protected float countLagrangian(float[] arguments) {
        float sum = 0;
        for (float argument : arguments) {
            sum += argument;
        }
        int i = 0;
        int j = 0;
        for (float xi : arguments) {
            j = 0;
            for (float xj : arguments) {
                sum -= 0.5 * getY(mLearningSet.get(i)) * getY(mLearningSet.get(j)) * xi * xj * mScalarProducts[i][j];
                j++;
            }
            i++;
        }
        return sum;
    }

    protected Bitmap countBitmap() throws BitmapAlgebraException {
        Bitmap ret = BitmapAlgebra.multiply(mBest[0] * getY(mLearningSet.get(0)), mLearningSet.get(0).Bitmap);
        for (int i = 1; i < mBest.length; i++)
            ret = BitmapAlgebra.add(ret, BitmapAlgebra.multiply(mBest[i] * getY(mLearningSet.get(i)), mLearningSet.get(i).Bitmap));
        return ret;
    }

    protected void doOneStep() {
        int localIterations = 100;
        float[] grad = countGradient(mBest);
        while (localIterations > 0) {
            if (mLastChange < 10e-6) return;
            localIterations--;
            float[] current = arraySum(mBest, arrayMul(mCurrentStep, grad));
            trimMultipliers(current, mMaxLagrangeMultiplier);
            float currentValue = countLagrangian(current);
            mCurrentStep *= mChange;
            if (currentValue > mBestValue) {
                mLastChange = currentValue - mBestValue;
                mBestValue = currentValue;
                mCurrentStep += mEpsilon;
                mBest = current;
                return;

            } else {
                mCurrentStep /= mFailChange;
            }
        }
    }

    protected Bitmap getBestBitmap(int iterations) throws BitmapAlgebraException {
        for (int i = 0; i < iterations; i++) {
            doOneStep();
            if (i % 50 == 0) {
                Log.d("SVMOptimizer", "Did 50 iterations");
                Log.d("LastChange", String.valueOf(mLastChange));
            }
        }
        return countBitmap();
    }

    public SVM getBestSVM(int iterations, float maxLagrangeMultiplier) throws SVMWrongBitmapDimension, SVMOptimizerException {
        mMaxLagrangeMultiplier = maxLagrangeMultiplier;
        mBestValue = Float.NEGATIVE_INFINITY;
        mBest = new float[mLearningSet.getDataSize()];
        Bitmap vector = null;
        try {
            vector = getBestBitmap(iterations);
        } catch (BitmapAlgebraException e) {
            e.printStackTrace();
            Log.d("SVMOptimizer", "Exception");
            throw new SVMOptimizerException();
        }
        float b = deduceB(vector);
        StringBuilder sb = new StringBuilder();
        sb.append(mBestValue);
        sb.append(" ");
        sb.append(mCurrentStep);
        Log.d("SVMOptimizer", sb.toString());
        return new SVM(mCoreFunction, vector, b);
    }
}
