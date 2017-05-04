package com.zhoumushui.bootplayer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.storage.StorageManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zhoumushui.bootplayer.model.MultiGridItem;
import com.zhoumushui.bootplayer.ui.VideoPlayActivity;
import com.zhoumushui.bootplayer.util.HintUtil;
import com.zhoumushui.bootplayer.util.MediaUtil;
import com.zhoumushui.bootplayer.util.MyLog;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView textState;
    private FloatingActionButton floatingActionPlay;

    private Context context;

    private ArrayList<MultiGridItem> contentList;

    private static final String[] PERMISSION_EXTERNAL_STORAGE = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int REQUEST_EXTERNAL_STORAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();

        setContentView(R.layout.activity_main);

        textState = (TextView) findViewById(R.id.textState);
        floatingActionPlay = (FloatingActionButton) findViewById(R.id.floatingActionPlay);
        floatingActionPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPermissionAndPlayVideo();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        getPermissionAndPlayVideo();
    }

    private void getPermissionAndPlayVideo() {
        String pathExtSd = "/storage/F672-13E1";
        String[] pathSD = getExtSDCardPath(context);
        if (pathSD.length > 0) {
            for (int i = 0; i < pathSD.length; i++) {
                MyLog.i("pathSD[" + i + "]:" + pathSD[i]);
                if (!"/storage/emulated/0".equals(pathSD[i])) {
                    pathExtSd = pathSD[i];
                }
            }
        }
        if (new File(pathExtSd).exists()) {
            int permissionWrite = ActivityCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionWrite != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        PERMISSION_EXTERNAL_STORAGE, REQUEST_EXTERNAL_STORAGE);
            } else {

                contentList = new ArrayList<MultiGridItem>();
                contentList.addAll(MediaUtil.getVideos(context, pathExtSd));

                if (!contentList.isEmpty()) {
                    Intent intentPlay = new Intent(context, VideoPlayActivity.class);
                    MyApp.arrayListVideo = contentList;
                    intentPlay.putExtra("POSITION", 0);
                    intentPlay.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intentPlay);
                    textState.setText("");
                } else {
                    textState.setText("SD根目录无FLV视频");
                    HintUtil.showToast(context, "SD根目录无FLV视频");
                }
            }
        } else {
            textState.setText("SD卡不存在");
            HintUtil.showToast(context, "SD卡不存在");
        }
    }


    /**
     * 获取外置SD卡路径
     */
    public static String[] getExtSDCardPath(Context context) {
        StorageManager storageManager = (StorageManager) context.getSystemService(Context
                .STORAGE_SERVICE);
        try {
            Class<?>[] paramClasses = {};
            Method getVolumePathsMethod = StorageManager.class.getMethod("getVolumePaths", paramClasses);
            getVolumePathsMethod.setAccessible(true);
            Object[] params = {};
            Object invoke = getVolumePathsMethod.invoke(storageManager, params);
            return (String[]) invoke;
        } catch (NoSuchMethodException e1) {
            e1.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


}
