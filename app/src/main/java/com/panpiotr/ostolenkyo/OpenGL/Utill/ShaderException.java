package com.panpiotr.ostolenkyo.OpenGL.Utill;

/**
 * Created by Pan Piotr on 22/11/2015.
 */

public class ShaderException extends Throwable {
    private final String mExceptionInfo;

    public ShaderException(String info) {
        mExceptionInfo = info;
    }

    public ShaderException() {
        mExceptionInfo = "No additional info";
    }

    @Override
    public String toString() {
        return mExceptionInfo;
    }
}
