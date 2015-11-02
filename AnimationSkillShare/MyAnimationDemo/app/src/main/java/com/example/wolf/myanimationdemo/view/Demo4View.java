package com.example.wolf.myanimationdemo.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

/**
 * Created by wolf on 15/11/2.
 */
public class Demo4View extends View {

    private static final float BALL_RADIUS = 40;

    private boolean mHasInited;
    private int mWidth;
    private int mHeight;
    private Ball mBall = new Ball(BALL_RADIUS);
    private Paint mPaint;
    private ValueAnimator mAnimator;

    public Demo4View(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void init() {
        if (!mHasInited) {
            mWidth = getWidth();
            mHeight = getHeight();
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setColor(Color.GREEN);
            mHasInited = true;
            mBall.moveTo(mWidth / 2, BALL_RADIUS * 2);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        init();

        mBall.draw(canvas, mPaint);
    }

    public void startAnimation() {
        if (mAnimator != null && mAnimator.isRunning()) {
            return;
        }

        Log.i("demo4view", "start ");

        mAnimator = ValueAnimator.ofFloat(0, 1);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.setDuration(3000);
        mAnimator.setRepeatCount(-1);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float y = (float) mAnimator.getAnimatedValue();
                mBall.moveTo(mWidth / 2f, mHeight * y);
                Log.i("Demo4View", "" + mWidth / 2 + ", " + mHeight * y);
                invalidate();
            }
        });
        mAnimator.start();
    }

    public void endAnimation() {
        if (mAnimator != null) {
            mAnimator.end();
        }
    }

    public void cancelAnimation() {
        if (mAnimator != null) {
            mAnimator.cancel();
        }
    }
}
