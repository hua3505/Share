package com.example.wolf.myanimationdemo.interpolator;

import android.animation.TimeInterpolator;

/**
 * Created by wolf on 15/10/31.
 */
public class SinInterpolator implements TimeInterpolator {
    @Override
    public float getInterpolation(float input) {
        return (float) Math.sin(input * Math.PI);
    }
}
