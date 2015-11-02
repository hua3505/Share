package com.example.wolf.myanimationdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.example.wolf.myanimationdemo.view.SinInterpolatorView;

/**
 * Created by wolf on 15/10/30.
 */
public class Demo2Activity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo2);

        final SinInterpolatorView view = ((SinInterpolatorView) findViewById(R.id.animation_view));
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.startAnimation();
            }
        });
    }
}
