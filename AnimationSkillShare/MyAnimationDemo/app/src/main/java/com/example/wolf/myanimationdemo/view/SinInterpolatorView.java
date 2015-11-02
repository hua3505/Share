package com.example.wolf.myanimationdemo.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

import com.example.wolf.myanimationdemo.interpolator.SinInterpolator;

/**
 * 三角函数sin做插值器
 * Created by wolf on 15/10/30.
 */
public class SinInterpolatorView extends View {
    private static final float BALL_RADIUS = 40;
    private static final long ANIMATION_DURATION = 2 * 1000;

    private boolean mHasInited;
    private int mWidth;
    private int mHeight;
    private Ball mBall = new Ball(BALL_RADIUS);
    private Paint mPaint;
    private ValueAnimator mAnimator;

    public SinInterpolatorView(Context context, AttributeSet attrs) {
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
        if (mAnimator != null && mAnimator.isStarted()) {
            return;
        }

        mAnimator = ValueAnimator.ofFloat(0, 1);
        mAnimator.setInterpolator(new SinInterpolator());
        mAnimator.setDuration(ANIMATION_DURATION);
        mAnimator.setRepeatCount(0);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cx = (mWidth - 2 * BALL_RADIUS) * animation.getCurrentPlayTime() * 1f / animation.getDuration()
                        + BALL_RADIUS;
                float cy = (mHeight - 2 * BALL_RADIUS) * (1 - (float) animation.getAnimatedFraction())
                        + BALL_RADIUS;
                Log.v("SinInterpolatorView", "onAnimationUpdate " + cx + "," + cy);
                mBall.moveTo(cx, cy);
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
