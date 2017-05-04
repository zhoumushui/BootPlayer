package com.zhoumushui.bootplayer.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue.IdleHandler;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;

import com.zhoumushui.bootplayer.MyApp;
import com.zhoumushui.bootplayer.R;
import com.zhoumushui.bootplayer.air.Constant;
import com.zhoumushui.bootplayer.util.HintUtil;
import com.zhoumushui.bootplayer.util.MyLog;
import com.zhoumushui.bootplayer.util.TypefaceUtil;
import com.zhoumushui.bootplayer.view.SoundView;
import com.zhoumushui.bootplayer.view.VideoView;

public class VideoPlayActivity extends Activity {

    private Context context;
    private boolean isChangedVideo = false;

    private static int videoPosition = -1;
    private int playedTime;

    private VideoView videoView = null;
    private SeekBar seekTime = null;
    private TextView textTimeTotal = null;
    private TextView textTimePlayed = null;
    private GestureDetector gestureDetector = null;
    private AudioManager audioManager = null;

    private int maxVolume = 0;
    private int currentVolume = 0;

    private ImageButton btnPrevious = null;
    private ImageButton btnPlayState = null;
    private ImageButton btnNext = null;
    private ImageButton btnVolume = null;

    private View controlView = null;
    private PopupWindow popupWindowController = null;

    private SoundView soundView = null;
    private PopupWindow popupWindowSound = null;

    private View extralView = null;
    private PopupWindow popupWindowExtral = null;
    private TextView textTitle;
    private TextView textPosition;

    private static int screenWidth = 0;
    private static int screenHeight = 0;
    private static int controlHeight = 0;

    private final static int TIME = 6868;

    private boolean isControllerShow = true;
    private boolean isPaused = false;
    private boolean isFullScreen = false;
    private boolean isSilent = false;
    private boolean isSoundShow = false;

    private Typeface typeface;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_video_play);

        context = getApplicationContext();
        typeface = TypefaceUtil.get(this, Constant.Path.FONT
                + "Font-Helvetica-Neue-LT-Pro.otf");

        Looper.myQueue().addIdleHandler(new IdleHandler() {

            @Override
            public boolean queueIdle() {

                if (popupWindowController != null && videoView.isShown()) {
                    popupWindowController.showAtLocation(videoView,
                            Gravity.BOTTOM, 0, 0);
                    // controler.update(screenWidth, controlHeight);
                    popupWindowController.update(0, 0, screenWidth,
                            controlHeight);
                }

                if (popupWindowExtral != null && videoView.isShown()) {
                    popupWindowExtral.showAtLocation(videoView, Gravity.TOP, 0,
                            0);
                    popupWindowExtral.update(0, 25, screenWidth, 60);
                }

                // myHandler.sendEmptyMessageDelayed(HIDE_CONTROLER, TIME);
                return false;
            }
        });

        controlView = getLayoutInflater().inflate(
                R.layout.video_play_controler, null);
        popupWindowController = new PopupWindow(controlView);
        textTimeTotal = (TextView) controlView.findViewById(R.id.textTimeTotal);
        textTimeTotal.setTypeface(typeface);
        textTimePlayed = (TextView) controlView
                .findViewById(R.id.textTimePlayed);
        textTimePlayed.setTypeface(typeface);

        soundView = new SoundView(this);
        soundView.setOnVolumeChangeListener(new SoundView.OnVolumeChangedListener() {

            @Override
            public void setYourVolume(int index) {
                cancelDelayHide();
                updateVolume(index);
                hideControllerDelay();
            }
        });

        popupWindowSound = new PopupWindow(soundView);

        extralView = getLayoutInflater().inflate(R.layout.video_play_extral,
                null);
        popupWindowExtral = new PopupWindow(extralView);

        textTitle = (TextView) extralView.findViewById(R.id.textTitle);
        textTitle.setTypeface(typeface);
        textPosition = (TextView) extralView.findViewById(R.id.textPosition);
        textPosition.setTypeface(typeface);

        btnPrevious = (ImageButton) controlView.findViewById(R.id.btnPrevious);
        btnPrevious.setOnClickListener(myOnClickListener);

        btnPlayState = (ImageButton) controlView
                .findViewById(R.id.btnPlayState);
        btnPlayState.setOnClickListener(myOnClickListener);

        btnNext = (ImageButton) controlView.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(myOnClickListener);

        btnVolume = (ImageButton) controlView.findViewById(R.id.btnVolume);
        btnVolume.setAlpha(findAlphaFromSound());
        btnVolume.setOnClickListener(myOnClickListener);
        btnVolume.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View arg0) {
                if (isSilent) {
                    btnVolume
                            .setImageResource(R.drawable.video_play_soundenable);
                } else {
                    btnVolume
                            .setImageResource(R.drawable.video_play_sounddisable);
                }
                isSilent = !isSilent;
                updateVolume(currentVolume);
                cancelDelayHide();
                hideControllerDelay();
                return true;
            }

        });

        videoView = (VideoView) findViewById(R.id.videoView);
        videoView.setOnErrorListener(new OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {

                videoView.stopPlayback();
                finish(); // AZ

                new AlertDialog.Builder(VideoPlayActivity.this)
                        .setTitle("Title")
                        .setMessage("Message")
                        .setPositiveButton("֪PositiveButton",
                                new AlertDialog.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        videoView.stopPlayback();
                                    }

                                }).setCancelable(false).show();

                return false;
            }

        });

        videoView.setMySizeChangeLinstener(new VideoView.MySizeChangeLinstener() {

            @Override
            public void doMyThings() {
                setVideoScale(SCREEN_DEFAULT);
            }

        });

        btnPrevious.setAlpha(0xBB);
        btnPlayState.setAlpha(0xBB);
        btnNext.setAlpha(0xBB);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        seekTime = (SeekBar) controlView.findViewById(R.id.seekTime);
        seekTime.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekbar, int progress,
                                          boolean fromUser) {
                if (fromUser) {
                    videoView.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {
                myHandler.removeMessages(HIDE_CONTROLER);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                myHandler.sendEmptyMessageDelayed(HIDE_CONTROLER, TIME);
            }
        });

        getScreenSize();

        gestureDetector = new GestureDetector(new SimpleOnGestureListener() {

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (isFullScreen) {
                    setVideoScale(SCREEN_DEFAULT);
                } else {
                    setVideoScale(SCREEN_FULL);
                }
                isFullScreen = !isFullScreen;
                MyLog.d("GestureDetector.onDoubleTap");

                if (isControllerShow) {
                    showController();
                }
                isPaused = false;
                btnPlayState.setImageResource(R.drawable.btn_video_pause);

                return true; // return super.onDoubleTap(e);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (!isControllerShow) {
                    showController();
                    hideControllerDelay();
                } else {
                    cancelDelayHide();
                    hideController();
                }
                return true; // return super.onSingleTapConfirmed(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                if (Constant.hasVideoViewLongClick) {
                    if (isPaused) {
                        videoView.start();
                        btnPlayState
                                .setImageResource(R.drawable.btn_video_pause);
                        cancelDelayHide();
                        hideControllerDelay();
                    } else {
                        videoView.pause();
                        btnPlayState
                                .setImageResource(R.drawable.btn_video_play);
                        cancelDelayHide();
                        showController();
                    }
                    isPaused = !isPaused;
                    // super.onLongPress(e);
                }
            }
        });

        videoView.setOnPreparedListener(new OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer arg0) {
                setVideoScale(SCREEN_DEFAULT);
                isFullScreen = false;
                if (isControllerShow) {
                    showController();
                }

                int i = videoView.getDuration();
                Log.d("onCompletion", "" + i);
                seekTime.setMax(i);
                i /= 1000;
                int minute = i / 60;
                int second = i % 60;
                minute %= 60;
                textTimeTotal.setText(String
                        .format("%02d:%02d", minute, second));

				/*
                 * controler.showAtLocation(vv, Gravity.BOTTOM, 0, 0);
				 * controler.update(screenWidth, controlHeight);
				 * myHandler.sendEmptyMessageDelayed(HIDE_CONTROLER, TIME);
				 */

                videoView.start();
                btnPlayState.setImageResource(R.drawable.btn_video_pause);
                hideControllerDelay();
                myHandler.sendEmptyMessage(PROGRESS_CHANGED);
            }
        });

        videoView.setOnCompletionListener(new OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer arg0) {
                if (videoPosition + 1 < MyApp.arrayListVideo.size()) {
                    playVideoByPosition(videoPosition + 1);
                } else {
                    videoPosition = 0;
                    playVideoByPosition(videoPosition);
                    // videoView.stopPlayback();
                    // finish();
                }
            }
        });

        videoPosition = getIntent().getIntExtra("POSITION", 0);
        MyLog.d("POSITION:" + videoPosition);
        if (videoPosition >= 0 && videoPosition <= MyApp.arrayListVideo.size()) {
            isChangedVideo = true;

            playVideoByPosition(videoPosition);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {

            videoView.stopPlayback();

            int result = data.getIntExtra("CHOOSE", -1);
            Log.d("RESULT", "" + result);

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void playVideoByPosition(int position) {
        if (position >= 0 && position <= MyApp.arrayListVideo.size()) {
            MyLog.i("playVideoByPosition:" + position);
            videoPosition = position;
            videoView.setVideoPath(MyApp.arrayListVideo.get(position).path);
            textTitle.setText(MyApp.arrayListVideo.get(position).displayName);
            textPosition.setText("(" + (position + 1) + "/"
                    + MyApp.arrayListVideo.size() + ")");
        }
    }

    private MyOnClickListener myOnClickListener = new MyOnClickListener();

    private class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnNext:
                    if (videoPosition + 1 < MyApp.arrayListVideo.size()) {
                        playVideoByPosition(videoPosition + 1);
                        cancelDelayHide();
                        hideControllerDelay();
                    } else {
                        playVideoByPosition(0);
                        cancelDelayHide();
                        hideControllerDelay();
                    }
                    break;

                case R.id.btnPrevious:
                    if (videoPosition - 1 >= 0
                            && videoPosition - 1 < MyApp.arrayListVideo.size()) {
                        playVideoByPosition(videoPosition - 1);
                        cancelDelayHide();
                        hideControllerDelay();
                    } else {
                        playVideoByPosition(MyApp.arrayListVideo.size() - 1);
                        cancelDelayHide();
                        hideControllerDelay();
                    }
                    break;

                case R.id.btnPlayState:
                    cancelDelayHide();
                    if (isPaused) {
                        videoView.start();
                        btnPlayState.setImageResource(R.drawable.btn_video_pause);
                        hideControllerDelay();
                    } else {
                        videoView.pause();
                        btnPlayState.setImageResource(R.drawable.btn_video_play);
                    }
                    isPaused = !isPaused;
                    break;

                case R.id.btnVolume:
                    cancelDelayHide();
                    if (isSoundShow) {
                        popupWindowSound.dismiss();
                    } else {
                        if (popupWindowSound.isShowing()) {
                            popupWindowSound.update(15, 0, SoundView.MY_WIDTH,
                                    SoundView.MY_HEIGHT);
                        } else {
                            popupWindowSound.showAtLocation(videoView,
                                    Gravity.RIGHT | Gravity.CENTER_VERTICAL, 15, 0);
                            popupWindowSound.update(15, 0, SoundView.MY_WIDTH,
                                    SoundView.MY_HEIGHT);
                        }
                    }
                    isSoundShow = !isSoundShow;
                    hideControllerDelay();
                    break;

                default:
                    break;
            }
        }

    }

    private final static int PROGRESS_CHANGED = 0;
    private final static int HIDE_CONTROLER = 1;

    Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PROGRESS_CHANGED:
                    int i = videoView.getCurrentPosition();
                    seekTime.setProgress(i);
                    seekTime.setSecondaryProgress(0);

                    i /= 1000;
                    int minute = i / 60;
                    int second = i % 60;
                    minute %= 60;
                    textTimePlayed.setText(String.format("%02d:%02d", minute,
                            second));

                    sendEmptyMessageDelayed(PROGRESS_CHANGED, 100);
                    break;

                case HIDE_CONTROLER:
                    hideController();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = gestureDetector.onTouchEvent(event);

        if (!result) {
            if (event.getAction() == MotionEvent.ACTION_UP) {

				/*
                 * if(!isControllerShow){ showController();
				 * hideControllerDelay(); }else { cancelDelayHide();
				 * hideController(); }
				 */
            }
            result = super.onTouchEvent(event);
        }

        return result;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        getScreenSize();
        if (isControllerShow) {
            cancelDelayHide();
            hideController();
            showController();
            hideControllerDelay();
        }

        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPause() {
        playedTime = videoView.getCurrentPosition();
        videoView.pause();
        btnPlayState.setImageResource(R.drawable.btn_video_play);
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (!isChangedVideo) {
            videoView.seekTo(playedTime);
            videoView.start();
        } else {
            isChangedVideo = false;
        }

        if (videoView.isPlaying()) {
            btnPlayState.setImageResource(R.drawable.btn_video_pause);
            hideControllerDelay();
        }

        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (popupWindowController.isShowing()) {
            popupWindowController.dismiss();
            popupWindowExtral.dismiss();
        }
        if (popupWindowSound.isShowing()) {
            popupWindowSound.dismiss();
        }

        myHandler.removeMessages(PROGRESS_CHANGED);
        myHandler.removeMessages(HIDE_CONTROLER);

        if (videoView.isPlaying()) {
            videoView.stopPlayback();
        }
        super.onDestroy();
    }

    private void getScreenSize() {
        Display display = getWindowManager().getDefaultDisplay();
        screenHeight = display.getHeight();
        screenWidth = display.getWidth();
        controlHeight = screenHeight / 4;

        // adView = (AdView) extralView.findViewById(R.id.ad);
        // LayoutParams lp = adView.getLayoutParams();
        // lp.width = screenWidth*3/5;
    }

    private void hideController() {
        if (popupWindowController.isShowing()) {
            popupWindowController.update(0, 0, 0, 0);
            popupWindowExtral.update(0, 0, 0, 0);
            isControllerShow = false;
        }
        if (popupWindowSound.isShowing()) {
            popupWindowSound.dismiss();
            isSoundShow = false;
        }
    }

    private void hideControllerDelay() {
        myHandler.sendEmptyMessageDelayed(HIDE_CONTROLER, TIME);
    }

    private void showController() {
        popupWindowController.update(0, 0, screenWidth, controlHeight);
        if (isFullScreen) {
            popupWindowExtral.update(0, 25, screenWidth, 60);
        } else {
            popupWindowExtral.update(0, 25, screenWidth, 60);
        }

        isControllerShow = true;
    }

    private void cancelDelayHide() {
        myHandler.removeMessages(HIDE_CONTROLER);
    }

    private final static int SCREEN_FULL = 0;
    private final static int SCREEN_DEFAULT = 1;

    private void setVideoScale(int flag) {
        LayoutParams layoutParams = videoView.getLayoutParams();

        switch (flag) {
            case SCREEN_FULL:
                MyLog.d("screenWidth: " + screenWidth + " screenHeight: "
                        + screenHeight);
                videoView.setVideoScale(screenWidth, screenHeight);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                break;

            case SCREEN_DEFAULT:
                int videoWidth = videoView.getVideoWidth();
                int videoHeight = videoView.getVideoHeight();
                int mWidth = screenWidth;
                int mHeight = screenHeight - 25;

                if (videoWidth > 0 && videoHeight > 0) {
                    if (videoWidth * mHeight > mWidth * videoHeight) {
                        // Log.i("@@@", "image too tall, correcting");
                        mHeight = mWidth * videoHeight / videoWidth;
                    } else if (videoWidth * mHeight < mWidth * videoHeight) {
                        // Log.i("@@@", "image too wide, correcting");
                        mWidth = mHeight * videoWidth / videoHeight;
                    } else {

                    }
                }
                videoView.setVideoScale(mWidth, mHeight);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                break;
        }
    }

    private int findAlphaFromSound() {
        if (audioManager != null) {
            // int currentVolume =
            // mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            int alpha = currentVolume * (0xCC - 0x55) / maxVolume + 0x55;
            return alpha;
        } else {
            return 0xCC;
        }
    }

    private void updateVolume(int index) {
        if (audioManager != null) {
            if (isSilent) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
            } else {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index,
                        0);
            }
            currentVolume = index;
            btnVolume.setAlpha(findAlphaFromSound());
        }
    }

}