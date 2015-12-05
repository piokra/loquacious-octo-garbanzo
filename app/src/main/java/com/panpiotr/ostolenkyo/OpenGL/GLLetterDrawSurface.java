package com.panpiotr.ostolenkyo.OpenGL;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

import com.panpiotr.ostolenkyo.Bitmap;
import com.panpiotr.ostolenkyo.OCR.SVM.SVM;
import com.panpiotr.ostolenkyo.OCR.SVM.SVMOptimizer.SVMOptimizer;
import com.panpiotr.ostolenkyo.OCR.SVM.SVMOptimizer.SVMOptimizerException;
import com.panpiotr.ostolenkyo.OCR.SVM.SVMWrongBitmapDimension;
import com.panpiotr.ostolenkyo.OCR.Utill.CoreFunctions.RadialCore;
import com.panpiotr.ostolenkyo.OCR.Utill.LearningData;
import com.panpiotr.ostolenkyo.OCR.Utill.LearningSet;
import com.panpiotr.ostolenkyo.OCR.Utill.LearningType;
import com.panpiotr.ostolenkyo.Utill.Math.Point2D;

import java.util.ArrayList;

/**
 * Created by Pan Piotr on 22/11/2015.
 */
public class GLLetterDrawSurface extends GLSurfaceView {

    private final GLLetterDrawRenderer mRenderer;
    private final ArrayList<Point2D> mPointHistory = new ArrayList<>();
    // TODO: 05/12/2015 remove this szit
    nextLetterStuff NLS = new nextLetterStuff();
    private float mPreviousX;
    private float mPreviousY;
    private boolean mDown = false;
    private float mViewWidth = 0;
    private float mViewHeight = 0;
    private boolean mNew = true;

    public GLLetterDrawSurface(Context context) {
        super(context);
        mRenderer = new GLLetterDrawRenderer();
        setEGLContextClientVersion(2);
        setRenderer(mRenderer);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mViewHeight = h;
        mViewWidth = w;

    }

    // TODO: 05/12/2015 do something more zeneral
    public void nextLetter() {
        NLS.currentLetter++;
        if (NLS.currentLetter == NLS.maxLetters) {
            SVMOptimizer optimizer = new SVMOptimizer(NLS.learningSet, LearningType.getType(0), new RadialCore(0.88f));
            try {
                NLS.svm = optimizer.getBestSVM(1000, 2.0f);
            } catch (SVMWrongBitmapDimension svmWrongBitmapDimension) {
                svmWrongBitmapDimension.printStackTrace();
            } catch (SVMOptimizerException e) {
                e.printStackTrace();
            }
        }
    }

    public void clear() {
        mRenderer.mLetters.popLetter();
        mRenderer.mLetters.updatePoints();
    }

    public void proceed() {
        Bitmap bitmap = mRenderer.mLetters.resterize(128, 128);
        bitmap.log("bitmap");
        NLS.learningSet.addData(new LearningData(bitmap, LearningType.getType(NLS.currentLetter)));
        mRenderer.mLetters.clear();
        mRenderer.mLetters.updatePoints();

    }

    @Override
    public boolean onTouchEvent(MotionEvent me) {


        switch (me.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPreviousX = me.getX();
                mPreviousY = me.getY();
                mDown = true;
            case MotionEvent.ACTION_UP:
                mDown = false;
                mPointHistory.clear();
                Log.d("point", "UP");
                mNew = true;
            case MotionEvent.ACTION_MOVE:
                float x = me.getX();
                float y = me.getY();
                Point2D t = new Point2D(x, y);
                if ((x - mPreviousX) * (x - mPreviousX) + (y - mPreviousY) * (y - mPreviousY) > 100f) {
                    mPreviousX = x;
                    mPreviousY = y;

                    mPointHistory.add(new Point2D(mPreviousX / (mViewWidth / 2) - 1f, -mPreviousY / (mViewHeight / 2) + 1f));
                    if (!mNew)
                        mRenderer.mLetters.popLetter();
                    mRenderer.mLetters.addLetter(new Letter(0.1f, mPointHistory));
                    mNew = false;
                }
        }
        return true;
    }

    private class nextLetterStuff {
        private final LearningSet learningSet = new LearningSet();
        private final int maxLetters = 2;
        private int currentLetter = 0;
        private SVM svm = null;

    }
}
