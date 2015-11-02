package com.example.wolf.myanimationdemo.view;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by wolf on 15/11/2.
 */
public class Ball {
    private float mCx = 0;
    private float mCy = 0;
    private float mRaius = 0;

    Ball(float radius) {
        mRaius = radius;
        mCx = mRaius * 2;
        mCy = mRaius * 2;
    }

    void draw(Canvas canvas, Paint paint) {
        canvas.drawCircle(mCx, mCy, mRaius, paint);
    }

    void moveTo(float cx, float cy) {
        mCx = cx;
        mCy = cy;
    }
}
