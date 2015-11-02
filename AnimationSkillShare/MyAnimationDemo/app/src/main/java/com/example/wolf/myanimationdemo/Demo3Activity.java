package com.example.wolf.myanimationdemo;

import android.app.Activity;
import android.os.Bundle;

import com.example.wolf.myanimationdemo.view.Demo3View;

/**
 * Created by wolf on 15/11/1.
 */
public class Demo3Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo3);
        Demo3View view = (Demo3View) findViewById(R.id.animation_view);
        view.startAnimation();
    }
}
