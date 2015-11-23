package com.panpiotr.ostolenkyo.OpenGL;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

import com.panpiotr.ostolenkyo.Utill.Math.Point2D;

import java.util.ArrayList;

/**
 * Created by Pan Piotr on 22/11/2015.
 */
public class GLLetterDrawSurface extends GLSurfaceView {

    private final GLLetterDrawRenderer mRenderer;
    private float mPreviousX;
    private float mPreviousY;
    private ArrayList<Point2D> mPointHistory = new ArrayList<>();
    private boolean mDown = false;
    public GLLetterDrawSurface(Context context) {
        super(context);
        mRenderer = new GLLetterDrawRenderer();
        setEGLContextClientVersion(2);
        setRenderer(mRenderer);

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
            case MotionEvent.ACTION_MOVE:
                float x = me.getX();
                float y = me.getY();
                Point2D t = new Point2D(x, y);
                t.logPoint("point");
                if ((x - mPreviousX) * (x - mPreviousX) + (y - mPreviousY) * (y - mPreviousY) > 100f) {
                    mPreviousX = x;
                    mPreviousY = y;
                    /* TODO replace with actual screen size */
                    mPointHistory.add(new Point2D(mPreviousX / 240f - 1f, -mPreviousY / 400f + 1f));
                    mRenderer.mLetters.popLetter();
                    mRenderer.mLetters.addLetter(new Letter(0.1f, mPointHistory));
                }
        }
        return true;
    }
}
