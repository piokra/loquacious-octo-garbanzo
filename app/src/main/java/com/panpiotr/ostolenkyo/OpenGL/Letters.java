package com.panpiotr.ostolenkyo.OpenGL;

import android.opengl.GLES20;
import android.util.Log;

import com.panpiotr.ostolenkyo.OpenGL.Utill.ByteBufferHelper;
import com.panpiotr.ostolenkyo.OpenGL.Utill.ShaderException;
import com.panpiotr.ostolenkyo.OpenGL.Utill.ShaderUtillity;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collection;

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

    public Letters(Letter[] letters) {
        mLetters = new ArrayList<>();
        for (Letter let : letters) mLetters.add(let);
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

    private void updatePoints() {
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
}
