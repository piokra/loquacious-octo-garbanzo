package com.panpiotr.ostolenkyo.OpenGL;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created by Pan Piotr on 22/11/2015.
 */
public class GLLetterDrawSurface extends GLSurfaceView {

    private final GLLetterDrawRenderer mRenderer;

    public GLLetterDrawSurface(Context context) {
        super(context);
        mRenderer = new GLLetterDrawRenderer();
        setEGLContextClientVersion(2);
        setRenderer(mRenderer);

    }
}
