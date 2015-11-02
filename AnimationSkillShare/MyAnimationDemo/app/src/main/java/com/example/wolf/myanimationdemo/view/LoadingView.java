package com.example.wolf.myanimationdemo.view;

import android.animation.TimeAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Interpolator;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.example.wolf.myanimationdemo.R;

public class LoadingView extends ImageView {

    private static final int ANIMATION_DURATION = 1000;

    private boolean mHasInited;
    private Bitmap mLoadingBitmap;
    private Bitmap mMaskBitmap;
    private int mWidth;
    private int mHeight;
    private float mMaskTop;
    private Paint mPaint;
    private ValueAnimator mAnimator;

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void init() {
        if (!mHasInited) {
            mHasInited = true;

            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            mPaint.setFilterBitmap(true);

            mMaskBitmap = getMaskBitmap();
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        init();

        int sc = canvas.saveLayer(0, 0, mWidth, mHeight, null, Canvas.ALL_SAVE_FLAG);
        canvas.drawBitmap(mLoadingBitmap, 0, 0, mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_ATOP));
        canvas.drawBitmap(mMaskBitmap, 0, mMaskTop, mPaint);
        mPaint.setXfermode(null);
        canvas.restoreToCount(sc);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable drawable = getResources().getDrawable(R.drawable.loading);
        mLoadingBitmap = ((BitmapDrawable) drawable).getBitmap();
        mWidth = mLoadingBitmap.getWidth();
        mHeight = mLoadingBitmap.getHeight();
        int newWidthMeasureSpec = MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.EXACTLY);
        int newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY);
        super.onMeasure(newWidthMeasureSpec, newHeightMeasureSpec);
    }

    public Bitmap getMaskBitmap() {
        Bitmap bm = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        canvas.drawColor(getResources().getColor(R.color.purple));
        return bm;
    }

    public void startLoadingAnimation() {
        if (mAnimator != null && mAnimator.isStarted()) {
            return;
        }

        mAnimator = ValueAnimator.ofFloat(1, 0);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setDuration(ANIMATION_DURATION);
        mAnimator.setRepeatCount(Animation.INFINITE);
        mAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mMaskTop = (Float) animation.getAnimatedValue() * mHeight;
                invalidate();
            }
        });
        mAnimator.start();
    }

    public void stopLoadingAnimation() {
        if (mAnimator != null) {
            mAnimator.end();
        }
    }
}
