package com.zhoumushui.bootplayer.util;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.content.res.Resources;
import android.os.storage.StorageManager;
import android.util.TypedValue;

import com.zhoumushui.bootplayer.model.MultiGridItem;


public class MediaUtil {

    public static ArrayList<MultiGridItem> getVideos(Context context, String path) {
        ArrayList<MultiGridItem> arrayListVideo = new ArrayList<MultiGridItem>();

        File dirMedia = new File(path); //
        try {
            if (dirMedia.exists()) {
                File[] childFiles = dirMedia.listFiles();
                for (File childFile : childFiles) {
                    String fileName = childFile.getName();
                    if (!fileName.startsWith(".") && fileName.endsWith(".flv")
                            && childFile.exists()) {
                        MultiGridItem item = new MultiGridItem();

                        item.path = childFile.getPath();
                        item.isVideo = true;
                        item.displayName = childFile.getName();

                        arrayListVideo.add(item);
                    }
                }
            } else {
                MyLog.i("getVideos.Directory not exist");
            }
        } catch (Exception e) {
            e.printStackTrace();
            MyLog.e("getVideos catch Exception:" + e.toString());
        }
        Collections.reverse(arrayListVideo);
        return arrayListVideo;
    }

    public static int dpToPx(Resources res, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                res.getDisplayMetrics());
    }

}
