package com.panpiotr.ostolenkyo.OpenGL;

import java.util.ArrayList;

/**
 * Created by Pan Piotr on 22/11/2015.
 */
public class Drawer {
    private final ArrayList<Drawable> mDrawables = new ArrayList<Drawable>();

    public void addDrawable(Drawable drawable) {
        mDrawables.add(drawable);
    }

    public void draw() {
        for (Drawable drawable : mDrawables) {

            drawable.draw();
        }
    }
}
