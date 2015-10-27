package com.example.wolf.myanimationdemo;

import android.animation.Animator;
import android.app.Activity;
import android.os.Bundle;

import com.example.wolf.myanimationdemo.view.LoadingView;

/**
 * Created by wolf on 15/10/26.
 */
public class Demo1Activity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo1);
        ((LoadingView) findViewById(R.id.loading_view)).startLoadingAnimation();
    }
}
