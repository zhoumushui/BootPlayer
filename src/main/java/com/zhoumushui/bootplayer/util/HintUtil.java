package com.zhoumushui.bootplayer.util;

import android.content.Context;
import android.widget.Toast;

public class HintUtil {

    private static Toast toast = null;

    public static void showToast(Context context, String content) {
        if (toast == null) {
            toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }

}
