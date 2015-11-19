package com.example.wolf.myanimationdemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.wolf.myanimationdemo.view.WatchFaceView;

import java.util.logging.LogRecord;

/**
 * Created by wolf on 15/11/6.
 */
public class Demo7Activity extends Activity {

    WatchFaceView mWatchFaceView;

    private static final int MSG_REFRESH = 1;

    private Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
                case MSG_REFRESH:
                    mWatchFaceView.invalidate();
                    mHandler.sendEmptyMessageDelayed(MSG_REFRESH, 20);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo7);
        mWatchFaceView = (WatchFaceView) findViewById(R.id.watchfaceview);
        mHandler.sendEmptyMessage(MSG_REFRESH);
    }
}
