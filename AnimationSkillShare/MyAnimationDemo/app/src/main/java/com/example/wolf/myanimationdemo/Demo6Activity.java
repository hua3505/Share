package com.example.wolf.myanimationdemo;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Time;

/**
 * Created by wolf on 15/11/5.
 */
public class Demo6Activity extends Activity {

    private ImageView mImageView;
    private TextView mTextView;
    private double fps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo6);

        mImageView = (ImageView) findViewById(R.id.imageview);
        mTextView = (TextView) findViewById(R.id.text);
        startAnimation();
    }

    private void startAnimation() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mImageView, "rotation", 0, 360);
        objectAnimator.setDuration(2000);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);

        fps = -10;

        TimeAnimator timeAnimator = new TimeAnimator();
        timeAnimator.setTimeListener(new TimeAnimator.TimeListener() {
            @Override
            public void onTimeUpdate(TimeAnimator animation, long totalTime, long deltaTime) {
                double currentFps;
                if (deltaTime != 0) {
                    currentFps = 1000 / deltaTime;
                } else {
                    currentFps = 0.9 * fps;
                }
                if (fps < 0) {
                    fps = currentFps;
                } else {
                    fps = fps * 0.9 + 0.1 * currentFps;
                }
                updateFps(fps);
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(objectAnimator).with(timeAnimator);
        animatorSet.start();
    }

    private void updateFps(double fps) {
        String text = String.format("fps: %.2f", fps);
        mTextView.setText(text);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }
}
