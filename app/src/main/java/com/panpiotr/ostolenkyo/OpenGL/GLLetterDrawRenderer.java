package com.panpiotr.ostolenkyo.OpenGL;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Pan Piotr on 22/11/2015.
 */
public class GLLetterDrawRenderer implements Renderer {

    public final Letters mLetters = new Letters();

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.1f, 0, 0, 1);
        mLetters.addLetter(new Letter(0.1f, new float[]{0f, 0f, 0.1f, 0f, 0.3f, 0.3f}));
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        mLetters.draw();

    }
}
