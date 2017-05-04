package com.zhoumushui.bootplayer.air;

import java.io.File;

import android.os.Environment;

public interface Constant {
	/** Debug：打印Log */
	boolean isDebug = true;

	/** 长按VideoView播放暂停功能 */
	boolean hasVideoViewLongClick = false;

	/**
	 * 退出程序的两次返回最大间隔
	 */
	long TIME_DOUBLE_SPAN = 2000;

	/** SharedPreferences */
	class MySP {
		/** 名称 */
		public static final String NAME = "AutoPlayer";

	}

	/** 广播 */
	class Broadcast {
		/** ACC上电 */
		public static final String ACC_ON = "com.tchip.ACC_ON";

		/** ACC下电 */
		public static final String ACC_OFF = "com.tchip.ACC_OFF";

		/** 倒车开始 */
		public static final String BACK_CAR_ON = "com.tchip.KEY_BACK_CAR_ON";
		/** 倒车结束 */
		public static final String BACK_CAR_OFF = "com.tchip.KEY_BACK_CAR_OFF";

		/** 系统设置进入格式化界面 */
		public static final String MEDIA_FORMAT = "tchip.intent.action.MEDIA_FORMAT";

		/** For AutoPlayer */
		public static final String MEDIA_UPDATE = "tchip.intent.action.MEDIA_UPDATE";

		/** 系统关机 */
		public static final String GOING_SHUTDOWN = "tchip.intent.action.GOING_SHUTDOWN";
	}

	/** 路径 */
	class Path {
		public static final String NODE_ACC_STATUS = "/sys/bus/i2c/devices/0-007f/ACC_status";
		/** CVBS 状态(5位数，最后一位标志0,1) */
		public static final String NODE_CVBS_STATUS = "/sys/bus/i2c/devices/0-007f/camera_status";

		/** SDcard Path */
		public static final String SD_CARD = Environment
				.getExternalStorageDirectory().getPath();

		public static final String SDCARD_0 = "/storage/sdcard0";
		public static final String SDCARD_1 = "/storage/sdcard1";

		/** 录像存储卡路径 */
		public static String RECORD_SDCARD = SDCARD_1 + File.separator;

		public static String VIDEO_FRONT_TOTAL = "/storage/sdcard1/DrivingRecord/VideoFront/";
		public static String VIDEO_FRONT_UNLOCK = "/storage/sdcard1/DrivingRecord/VideoFront/Unlock";
		public static String VIDEO_FRONT_LOCK = "/storage/sdcard1/DrivingRecord/VideoFront/Lock";

		public static String VIDEO_BACK_TOTAL = "/storage/sdcard1/DrivingRecord/VideoBack/";
		public static String VIDEO_BACK_UNLOCK = "/storage/sdcard1/DrivingRecord/VideoBack/Unlock";
		public static String VIDEO_BACK_LOCK = "/storage/sdcard1/DrivingRecord/VideoBack/Lock";

		public static String IMAGE_BOTH = "/storage/sdcard1/DrivingRecord/Image/";
		public static String VIDEO_THUMBNAIL = "/storage/sdcard1/DrivingRecord/.Thumbnail/";

		public static String RECORD_DIRECTORY = "/storage/sdcard1/DrivingRecord/";

		/** 字体目录 */
		public static final String FONT = "fonts/";
	}

}
