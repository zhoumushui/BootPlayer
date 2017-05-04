package com.zhoumushui.bootplayer;

import java.util.ArrayList;

import android.app.Application;
import android.content.Context;

import com.zhoumushui.bootplayer.model.MultiGridItem;
import com.zhoumushui.bootplayer.util.MyUncaughtExceptionHandler;

public class MyApp extends Application {

    private Context context;

    /**
     * 应用出错:需要停止录像
     */
    public static boolean isAppException = false;


    public static boolean hasDoubleExit = false;
    /**
     * 上次按下返回键的时间
     */
    public static long wannaExitTime = 0;

    /**
     * 保存视频列表给播放使用
     */
    public static ArrayList<MultiGridItem> arrayListVideo;

    @Override
    public void onCreate() {
        context = getApplicationContext();
        MyUncaughtExceptionHandler myUncaughtExceptionHandler = MyUncaughtExceptionHandler
                .getInstance();
        myUncaughtExceptionHandler.init(context);

        super.onCreate();
    }

}
