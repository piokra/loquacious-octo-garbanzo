package com.panpiotr.ostolenkyo.OpenGL;

import android.opengl.GLES20;
import android.util.Log;
import android.util.Pair;

import com.panpiotr.ostolenkyo.Bitmap;
import com.panpiotr.ostolenkyo.OpenGL.Utill.ByteBufferHelper;
import com.panpiotr.ostolenkyo.OpenGL.Utill.ShaderException;
import com.panpiotr.ostolenkyo.OpenGL.Utill.ShaderUtillity;
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
    protected Pair<Point2D, Point2D> getCircumscribedRectangle
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

    {
        if (mCircumscribedRectangle != null) return mCircumscribedRectangle;
        float minX = Float.MAX_VALUE, minY = Float.MAX_VALUE;
        float maxX = Float.MIN_VALUE, maxY = Float.MIN_VALUE;
        for (int i = 0; i < mPoints / 2; i++) {
            if (minX > mPoints[2 * i]) minX = mPoints[2 * i];
            if (minY > mPoints[2 * i + 1]) minY = mPoints[2 * i + 1];
            if (maxX < mPoints[2 * i]) maxX = mPoints[2 * i];
            if (maxY < mPoints[2 * i + 1]) maxY = mPoints[2 * i + 1];
        }
        mCircumscribedRectangle = Pair <>(new Point2D(minX, minY), new Point2D(maxX, maxY));
        return mCircumscribedRectangle;
    }

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
        Bitmap ret = new Bitmap(width, height, 1, values);
        for (int i = 0; i < mPoints / 6; i++) {
            resterizeTriangle(ret, i);
        }


        return ret;
    }

    protected void resterizeTriangle(Bitmap bitmap, int triangle) {
        final float maxWidth = 2.0f;
        final float maxHeight = 2.0f;
        Pair<Point2D, Point2D> rectangle = getCircumscribedRectangle();
        final float actaulWidth = rectangle.second.x - rectangle.first.x;
        final float actualHeight = rectangle.second.y - rectangle.first.y;
        final float diffXPerPixel = actualWidth / (float) bitmap.Width;
        final float diffYPerPixel = actualHeight / (float) bitmap.Height;

        final Point2D vertex1 = new Point2D(mPoints[6 * triangle], mPoints[6 * triangle + 1]);
        final Point2D vertex2 = new Point2D(mPoints[6 * triangle + 2], mPoints[6 * triangle + 3]);
        final Point2D vertex3 = new Point2D(mPoints[6 * triangle + 4], mPoints[6 * triangle + 5]);

        final Point2D vec12 = Point2D.subtract(vertex2, vertex1);
        final Point2D vec23 = Point2D.subtract(vertex3, vertex2);
        final Point2D vec31 = Point2D.subtract(vertex1, vertex3);

        //divide triangle into three ortogonal triangles
        int firstWidth = (int) Math.abs(vec12.x / diffXPerPixel);
        int firstStartX = (int) (Math.abs(vertex1.x - rectangle.first.x) / diffXPerPixel);
        int firstStartY = (int) (Math.abs(vertex1.y - rectangle.first.y) / diffYPerPixel);
        int firstDirX = (int) Math.signum(vec12.x);
        int firstDirY = (int) Math.signum(vec12.y);

    }

    protected
    protected void resterizeOrtogonalTriangle(Bitmap bitmap, Point2D first, Point2D second, Point2D third) {
        //find left bot

    }
}
