package com.example.wolf.myanimationdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.wolf.myanimationdemo.view.Demo4View;

/**
 * Created by wolf on 15/11/2.
 */
public class Demo4Activity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo4);

        final Demo4View animationView = (Demo4View) findViewById(R.id.animation_view);


        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("demo4activity", "start click");
                animationView.startAnimation();
            }
        });

        findViewById(R.id.end).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationView.endAnimation();
            }
        });

        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationView.cancelAnimation();
            }
        });
    }
}
