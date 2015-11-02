package com.example.wolf.myanimationdemo.view;

import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by wolf on 15/11/2.
 */
public class Demo3View extends View {
    private static final float BALL_RADIUS = 40;

    private boolean mHasInited;
    private int mWidth;
    private int mHeight;
    private Ball mBall = new Ball(BALL_RADIUS);
    private Paint mPaint;
    private ValueAnimator mAnimator;

    public Demo3View(Context context, AttributeSet attrs) {
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

        mAnimator = ValueAnimator.ofFloat(0.25f, 0.75f, 0.25f, 0.75f);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setDuration(2000);
        mAnimator.setRepeatCount(-1);
        mAnimator.setEvaluator(new FloatEvaluator() {
            @Override
            public Float evaluate(float fraction, Number startValue, Number endValue) {
                return (fraction > 0.5) ? endValue.floatValue() : startValue.floatValue();
            }
        });
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float x = animation.getAnimatedFraction() * mWidth;
                float y = (float) animation.getAnimatedValue() * mHeight;
                mBall.moveTo(x, y);
                invalidate();
            }
        });
        mAnimator.start();
    }

    private class Ball {
        private float mCx = 0;
        private float mCy = 0;
        private float mRaius = 0;

        Ball(float radius) {
            mRaius = radius;
            mCx = mRaius;
            mCy = mHeight - mRaius;
        }

        void draw(Canvas canvas, Paint paint) {
            canvas.drawCircle(mCx, mCy, mRaius, paint);
        }

        void moveTo(float cx, float cy) {
            mCx = cx;
            mCy = cy;
        }
    }
}
