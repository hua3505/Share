package com.example.wolf.myanimationdemo;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by wolf on 15/11/5.
 */
public class Demo5Activity extends Activity {

    private View mView;
    private ObjectAnimator mObjectAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo5);
        mView = findViewById(R.id.view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startAnimation();
    }

    public void startAnimation() {
        if (mObjectAnimator != null && mObjectAnimator.isRunning()) {
            return;
        }

        mObjectAnimator = ObjectAnimator.ofInt(mView, "height", 0, 1920);
        mObjectAnimator.setDuration(1000);
        mObjectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mObjectAnimator.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mObjectAnimator.end();
    }
}
