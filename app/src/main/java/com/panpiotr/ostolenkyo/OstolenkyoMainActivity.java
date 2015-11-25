package com.panpiotr.ostolenkyo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.panpiotr.ostolenkyo.OpenGL.GLLetterDrawSurface;


public class OstolenkyoMainActivity extends Activity {

    GLLetterDrawSurface mGLView;
    private final View.OnClickListener clearListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mGLView.clear();
        }
    };
    private final View.OnClickListener proceedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mGLView.proceed();
            Log.d("Helo", "hello");
        }
    };
    private View mContentView;
    private View mView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_layout);
        LinearLayout ll = (LinearLayout) findViewById(R.id.OpenGLLayout);
        GLLetterDrawSurface view = new GLLetterDrawSurface(this);
        mGLView = view;
        ll.addView(mGLView);
        Button clear = (Button) findViewById(R.id.Clear);
        Button proceed = (Button) findViewById(R.id.Proceed);
        clear.setOnClickListener(clearListener);
        proceed.setOnClickListener(proceedListener);
        mContentView = mView;

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

}
