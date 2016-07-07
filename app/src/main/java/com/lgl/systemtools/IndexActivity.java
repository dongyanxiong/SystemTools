package com.lgl.systemtools;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.daimajia.numberprogressbar.NumberProgressBar;

import java.util.Timer;
import java.util.TimerTask;

/*
 *  项目名：  SystemTools 
 *  包名：    com.lgl.systemtools
 *  文件名:   IndexActivity
 *  创建者:   LGL
 *  创建时间:  2016/7/7 17:18
 *  描述：    加载页
 */
public class IndexActivity extends AppCompatActivity {

    //定时器
    private Timer timer;
    //明明API中有监听，为何调用不了？代码bug反馈给代码家
    private NumberProgressBar number_progress_bar;

    private static final int TIME_OUT = 10;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case TIME_OUT:
                    if (number_progress_bar.getProgress() == 100) {
                        startActivity(new Intent(IndexActivity.this, MainActivity.class));
                        finish();
                    }else{
                        handler.sendEmptyMessageDelayed(TIME_OUT,100);
                    }
                    break;
            }

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        initView();
        handler.sendEmptyMessageDelayed(TIME_OUT,20);
    }

    //初始化View
    private void initView() {
        number_progress_bar = (NumberProgressBar) findViewById(R.id.number_progress_bar);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        number_progress_bar.incrementProgressBy(1);
                    }
                });
            }
        }, 1000, 20);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}
