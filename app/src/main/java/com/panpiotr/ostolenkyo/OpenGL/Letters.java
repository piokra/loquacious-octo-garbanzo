package com.panpiotr.ostolenkyo.OpenGL;

import android.opengl.GLES20;
import android.util.Log;
import android.util.Pair;

import com.panpiotr.ostolenkyo.Bitmap;
import com.panpiotr.ostolenkyo.OpenGL.Utill.ByteBufferHelper;
import com.panpiotr.ostolenkyo.OpenGL.Utill.ShaderException;
import com.panpiotr.ostolenkyo.OpenGL.Utill.ShaderUtillity;
import com.panpiotr.ostolenkyo.OpenGL.Utill.Triangle;
import com.panpiotr.ostolenkyo.Utill.Math.Point2D;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by Pan Piotr on 22/11/2015.
 */
public class Letters implements Drawable {

    private final ArrayList<Letter> mLetters;

    int mVertexCount = 0;
    FloatBuffer mPointsBuffer = null;
    int mPositionHandle = 0;
    private float[] mPoints;
    private boolean mInitialized = false;
    private String mFragmentShaderCode = "void main()" +
            "{" +
            "   gl_FragColor = vec4(0,0,1,1);" +
            "}";
    private String mVertexShaderCode = " attribute vec2 aPos;" +
            " void main()" +
            "{" +
            "gl_Position = vec4(aPos,0,1);" +
            "}";
    private int mProgram = 0;
    private Pair<Point2D, Point2D> mCircumscribedRectangle = null;

    public Letters(Letter[] letters) {
        mLetters = new ArrayList<>();
        Collections.addAll(mLetters, letters);

        updatePoints();
    }

    public Letters(Collection<Letter> letters) {
        mLetters = new ArrayList<>();
        mLetters.addAll(letters);
        updatePoints();

    }

    public Letters() {
        mLetters = new ArrayList<>();
    }

    protected Pair<Point2D, Point2D> getCircumscribedRectangle() {
        if (mCircumscribedRectangle != null) return mCircumscribedRectangle;
        float minX = Float.MAX_VALUE, minY = Float.MAX_VALUE;
        float maxX = Float.MIN_VALUE, maxY = Float.MIN_VALUE;
        for (int i = 0; i < mPoints.length / 2; i++) {
            if (minX > mPoints[2 * i]) minX = mPoints[2 * i];
            if (minY > mPoints[2 * i + 1]) minY = mPoints[2 * i + 1];
            if (maxX < mPoints[2 * i]) maxX = mPoints[2 * i];
            if (maxY < mPoints[2 * i + 1]) maxY = mPoints[2 * i + 1];
        }
        mCircumscribedRectangle = new Pair<>(new Point2D(minX, minY), new Point2D(maxX, maxY));
        return mCircumscribedRectangle;
    }

    public void clear() {
        mLetters.clear();
    }


    public void setFragmentShaderCode(String string) {
        mFragmentShaderCode = string;
    }

    public void setVertexShaderCode(String string) {
        mVertexShaderCode = string;
    }

    public void addLetter(Letter letter) {
        mLetters.add(letter);
        updatePoints();
    }

    public void popLetter() {
        if (mLetters.size() == 0) return;
        mLetters.remove(mLetters.size() - 1);
    }

    public void updatePoints() {
        mPoints = null;
        mCircumscribedRectangle = null;
        int size = 0;
        for (Letter letter : mLetters) {

            size += letter.getPoints().length;

        }
        mPoints = new float[size];
        int i = 0;
        for (Letter letter : mLetters) {
            for (float f : letter.getPoints()) {
                mPoints[i] = f;
                i++;
            }
        }
        mPointsBuffer = ByteBufferHelper.createByteBufferFromArray(mPoints);
        mVertexCount = mPoints.length / 2;
    }

    @Override
    public void draw() {

        if (!mInitialized) {
            initialize();
        }

        GLES20.glUseProgram(mProgram);
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, 2, GLES20.GL_FLOAT, false, 8, mPointsBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, mVertexCount);
        GLES20.glDisableVertexAttribArray(mPositionHandle);

    }

    private void initialize() {
        mProgram = GLES20.glCreateProgram();

        try {

            int vertex = ShaderUtillity.createAndCompileShader(GLES20.GL_VERTEX_SHADER, mVertexShaderCode);
            int fragment = ShaderUtillity.createAndCompileShader(GLES20.GL_FRAGMENT_SHADER, mFragmentShaderCode);
            GLES20.glAttachShader(mProgram, vertex);
            GLES20.glAttachShader(mProgram, fragment);
            GLES20.glLinkProgram(mProgram);
            GLES20.glValidateProgram(mProgram);
            mPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPos");
            mInitialized = true;

        } catch (ShaderException e) {
            Log.d(".Letters.initalize", e.toString());
        }
    }

    public Bitmap resterize(int height, int width) {
        float[] values = new float[height * width];
        Bitmap ret = new Bitmap(width, height, values);
        Pair<Point2D, Point2D> circumscribed = getCircumscribedRectangle();
        float zeroX = circumscribed.first.x;
        float zeroY = circumscribed.first.y;
        float xPerOne = (circumscribed.second.x - circumscribed.first.x) / (float) width;
        float yPerOne = (circumscribed.second.y - circumscribed.first.y) / (float) height;
        for (int i = 0; i < mPoints.length / 6; i++) {
            Triangle t = new Triangle(new Point2D(mPoints[6 * i], mPoints[6 * i + 1]), new Point2D(mPoints[6 * i + 2], mPoints[6 * i + 3]), new Point2D(mPoints[6 * i + 4], mPoints[6 * i + 5]));
            t.resterize(ret, zeroX, zeroY, xPerOne, yPerOne);
        }


        return ret;
    }


}
