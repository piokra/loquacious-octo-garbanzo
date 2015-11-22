package com.panpiotr.ostolenkyo.OpenGL.Utill;

import android.opengl.GLES20;

public class ShaderUtillity {

    public static int createAndCompileShader(int type, String code) throws ShaderException {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, code);
        GLES20.glCompileShader(shader);
        String info = GLES20.glGetShaderInfoLog(shader);
        if (shader == 0)
            throw new ShaderException("Failed to compile shader of type: " + String.valueOf(type) + "\n" + info);
        if (!info.isEmpty())
            throw new ShaderException("Failed to compile shader of type: " + String.valueOf(type) + "\n" + info);
        return shader;
    }

}
