package com.zhoumushui.bootplayer.model;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class MultiGridItem {

    public String path;
    public String thumbnail = null;
    public boolean isSeleted = false;
    public boolean isFunctionItem = false;
    public Drawable functionItemDrawale = null;
    public int tag;

    /**
     * AZ
     */
    public String displayName = "";
    public boolean isVideo = false;
    public Bitmap bitmapThumbnail;

}
