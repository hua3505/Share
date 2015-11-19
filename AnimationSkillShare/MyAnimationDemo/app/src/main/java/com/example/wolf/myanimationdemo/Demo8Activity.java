package com.example.wolf.myanimationdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.wolf.myanimationdemo.view.RoatingLoadingView;

public class Demo8Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo8);
        RoatingLoadingView view = (RoatingLoadingView) findViewById(R.id.loading_view);
        view.startAnimation();
    }
}
