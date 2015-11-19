package com.example.wolf.myanimationdemo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.wolf.myanimationdemo.R;
import com.example.wolf.myanimationdemo.WatchFaceDrawer;


/**
 * 4.4w.2表盘方式
 * @author trentyang
 */
public class WatchFaceView extends View {

    private static final String TAG = WatchFaceView.class.getSimpleName();
    private WatchFaceDrawer mWatchFaceDrawer;
    private int mWidth;
    private int mHeight;

    public WatchFaceView(Context context) {
        this(context, null);
    }

    public WatchFaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WatchFaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mWatchFaceDrawer = new WatchFaceDrawer(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mWidth == 0 && mHeight == 0) {
            Drawable drawable = getResources().getDrawable(R.drawable.watch_bg);
            Bitmap bm = ((BitmapDrawable) drawable).getBitmap();
            mWidth = (int) (bm.getWidth() * 0.666);
            mHeight = (int) (bm.getHeight() * 0.666);
            // NOTE: preview放在tvdpi(213dp):320x320px => xxhdpi(480dp):721x721px
            Log.i(TAG, "onMeasure mWidth:" + mWidth + ";mHeight:" + mHeight);
        }
        int widthSpec = MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.EXACTLY);
        int heightSpec = MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY);
        setMeasuredDimension(widthSpec, heightSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mWatchFaceDrawer.draw(canvas, getWidth(), getHeight());
    }

    public void setInAmbientMode(boolean flag) {
        mWatchFaceDrawer.setInAmbientMode(flag);
    }

    public void setWeatherTemp(int weatherTemp) {
        mWatchFaceDrawer.setWeatherTemp(weatherTemp);
    }

    public void setWeatherIcon(String weatherIcon) {
        mWatchFaceDrawer.setWeatherIcon(weatherIcon);
    }

}