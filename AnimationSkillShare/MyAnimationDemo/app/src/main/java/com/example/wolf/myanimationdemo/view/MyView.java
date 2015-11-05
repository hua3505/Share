package com.example.wolf.myanimationdemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wolf on 15/11/5.
 */
public class MyView extends View {

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setHeight(int height) {
        ViewGroup.LayoutParams lp = getLayoutParams();
        lp.height = height;
        setLayoutParams(lp);
        Log.v("MyView", "setHeight " + height);
    }
}
