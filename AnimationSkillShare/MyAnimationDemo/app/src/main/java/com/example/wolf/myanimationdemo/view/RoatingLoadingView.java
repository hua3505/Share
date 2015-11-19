package com.example.wolf.myanimationdemo.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.example.wolf.myanimationdemo.R;

/**
 * Created by wolf on 15/11/19.
 */
public class RoatingLoadingView extends View {
    private boolean mHasInited;
    private Bitmap mLoadingBitmap;
    private Paint mPaint;
    private float mWidth;
    private float mHeight;
    private float mCenterX;
    private float mCenterY;
    private float mMaskRotation;
    private ObjectAnimator mAnimator = new ObjectAnimator();

    public RoatingLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadBitmap();
    }

    private void init() {
        if (!mHasInited) {
            mHasInited = true;
            mWidth = getWidth();
            mHeight = getHeight();
            mCenterX = getWidth() / 2;
            mCenterY = getHeight() / 2;
            mPaint = new Paint();
            mPaint.setFilterBitmap(true);
            mPaint.setDither(true);
            mPaint.setAntiAlias(true);
            Shader shader = new SweepGradient(mCenterX, mCenterY, 0x10ffffff, 0xffffffff);
            mPaint.setShader(shader);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        init();
        int sc = canvas.saveLayer(0, 0, mWidth, mHeight, mPaint, Canvas.ALL_SAVE_FLAG);
        canvas.save();
        for (int i = 0; i < 8; ++i) {
            canvas.drawBitmap(mLoadingBitmap, 0, 0, mPaint);
            canvas.rotate(45, mCenterX, mCenterY);
        }
        canvas.restore();
        canvas.save();
        canvas.rotate(mMaskRotation, mCenterX, mCenterY);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawCircle(mCenterX, mCenterY, mWidth / 2, mPaint);
        mPaint.setXfermode(null);
        canvas.restore();
        canvas.restoreToCount(sc);
    }

    private void loadBitmap() {
        BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.rotating_loading);
        mLoadingBitmap = drawable.getBitmap();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mLoadingBitmap != null) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(mLoadingBitmap.getWidth(), MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mLoadingBitmap.getHeight(), MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void setMaskRotation(float rotation) {
        mMaskRotation = rotation;
    }

    public void startAnimation() {
        if (mAnimator != null && mAnimator.isRunning()) {
            return;
        }
        mAnimator = ObjectAnimator.ofFloat(this, "maskRotation", 0, 360);
        mAnimator.setDuration(2000);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                invalidate();
            }
        });
        mAnimator.start();
    }
}
